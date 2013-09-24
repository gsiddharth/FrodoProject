package com.applications.frodo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity{

    private static final int LOGIN=0;
    private static final int SIGNUP=1;
    private static final int FRAGMENT_COUNT=SIGNUP+1;

    private static final int REAUTH_ACTIVITY_CODE=100;
    private static final int WRAUTH_ACTIVITY_CODE=101;
    private boolean requestingReadPermission=false;
    private boolean requestingWritePermission=false;


    private boolean isResumed=false;

    private Fragment[] fragments=new Fragment[FRAGMENT_COUNT];

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };

   /* public void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.applications.frodo",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("TEMPTAGHASH KEY:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //printHashKey();
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        FragmentManager fm=getSupportFragmentManager();
        fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
        fragments[SIGNUP] = fm.findFragmentById(R.id.signupFragment);

        FragmentTransaction transaction = fm.beginTransaction();

        for(Fragment fragment:fragments){
            transaction.hide(fragment);
        }

        transaction.commit();
    }

    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {
            //TODO code for the fragment to be shown which is authenticated
            showFragment(LOGIN, false);
        } else {
            //non authenticated fragment
            showFragment(LOGIN, false);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        uiHelper.onResume();
        isResumed=true;
    }

    @Override
    public void onPause(){
        super.onPause();
        isResumed=false;
    }

    private synchronized void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible

        if(session!=null && session.isOpened()){
            //runs on first login
            if(!requestReadPermissions()){
                requestWritePermissions();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REAUTH_ACTIVITY_CODE){

            requestingReadPermission=false;
            requestWritePermissions();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private synchronized boolean requestReadPermissions(){

        String readPermissions[]= getResources().getString(R.string.facebook_read_permissions).split(",");
        List<String> permissionList=new ArrayList<String>();
        for(int i=0;i<readPermissions.length;i++){
            permissionList.add(readPermissions[i].trim());
        }

        Session session=Session.getActiveSession();

        List<String> allowedPermissions=session.getPermissions();

        if(!allowedPermissions.containsAll(permissionList)){

            if(session!=null && !requestingReadPermission && !requestingWritePermission){
                Session.NewPermissionsRequest newPermissionsRequest=new Session.NewPermissionsRequest(this, permissionList).setRequestCode(REAUTH_ACTIVITY_CODE);
                session.requestNewReadPermissions(newPermissionsRequest);
                requestingReadPermission=true;
            }
            return true;
        }
        return false;
    }

    private synchronized boolean requestWritePermissions(){
        String writePermissions[]= getResources().getString(R.string.facebook_write_permissions).split(",");
        List<String> permissionList=new ArrayList<String>();
        for(int i=0;i<writePermissions.length;i++){
            permissionList.add(writePermissions[i].trim());
        }

        Session session=Session.getActiveSession();

        List<String> allowedPermissions=session.getPermissions();

        if(!allowedPermissions.containsAll(permissionList)){
            if(session!=null && !requestingReadPermission && !requestingWritePermission){
                Session.NewPermissionsRequest newPermissionsRequest=new Session.NewPermissionsRequest(this, permissionList).setRequestCode(WRAUTH_ACTIVITY_CODE);
                session.requestNewPublishPermissions(newPermissionsRequest);
                requestingWritePermission=true;
            }
            return true;
        }
        return false;
    }
}
