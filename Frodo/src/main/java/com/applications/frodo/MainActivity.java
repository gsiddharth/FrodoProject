package com.applications.frodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.applications.frodo.db.PersistanceMap;
import com.applications.frodo.networking.BackendRequestParameters;
import com.applications.frodo.views.home.ApplicationActivity;
import com.facebook.Session;

public class MainActivity extends FragmentActivity{

    private static String TAG=MainActivity.class.toString();
    private static String PREFS_NAME="FrodosFile";

    private static final int LOGIN=0;
    private static final int LOADING=1;
    private static final int FRAGMENT_COUNT=LOADING+1;

    private Fragment[] fragments=new Fragment[FRAGMENT_COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        setContentView(R.layout.activity_main);

        FragmentManager fm=getSupportFragmentManager();
        fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
        fragments[LOADING] = fm.findFragmentById(R.id.loadingFragment);

        FragmentTransaction transaction = fm.beginTransaction();

        for(Fragment fragment:fragments){
            transaction.hide(fragment);
        }

        transaction.commit();
    }

    private void init(){

        PersistanceMap.getInstance().init(getSharedPreferences(PREFS_NAME,0));

        GlobalParameters.getInstance().setRootDir(getBaseContext().getFilesDir().getAbsolutePath());

        BackendRequestParameters.getInstance().setIp(getResources().getString(R.string.host_ip));
        BackendRequestParameters.getInstance().setPort(Integer.parseInt(getResources().getString(R.string.host_port)));
        BackendRequestParameters.getInstance().setGetUserDataQuery(getResources().getString(R.string.user_data_query));
        BackendRequestParameters.getInstance().setSingupQuery(getResources().getString(R.string.signup_query));
        BackendRequestParameters.getInstance().setLoginQuery(getResources().getString(R.string.login_query));
        BackendRequestParameters.getInstance().setShouldSignupQuery(getResources().getString(R.string.should_signup_query));
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

        if(GlobalParameters.getInstance().getUser()==null){
            showFragment(LOGIN,false);
        }else{
            Intent intent=new Intent(this, ApplicationActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
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
        fragments[LOGIN].onActivityResult(requestCode,resultCode,data);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("destroying...");
        fragments[LOGIN].onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
