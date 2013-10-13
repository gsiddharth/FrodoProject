package com.applications.frodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;

import com.applications.frodo.db.PersistanceMap;
import com.applications.frodo.networking.BackendRequestParameters;
import com.applications.frodo.socialnetworks.ILogin;
import com.applications.frodo.socialnetworks.facebook.LoginWithFacebook;
import com.applications.frodo.utils.Convertors;
import com.applications.frodo.views.home.ApplicationActivity;
import com.applications.frodo.views.SignupFragment;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class MainActivity extends FragmentActivity{

    private static String TAG=MainActivity.class.toString();
    private static String PREFS_NAME="FrodosFile";

    private static final int LOGIN=0;
    private static final int SIGNUP=1;
    private static final int LOADING=2;
    private static final int FRAGMENT_COUNT=LOADING+1;

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

    ILogin login= new LoginWithFacebook();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        FragmentManager fm=getSupportFragmentManager();
        fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
        fragments[SIGNUP] = fm.findFragmentById(R.id.signupFragment);
        fragments[LOADING] = fm.findFragmentById(R.id.loadingFragment);

        FragmentTransaction transaction = fm.beginTransaction();

        for(Fragment fragment:fragments){
            transaction.hide(fragment);
        }

        transaction.commit();
    }

    private void init(){
        GlobalParameters.getInstance().setRootDir(getBaseContext().getFilesDir().getAbsolutePath());
        BackendRequestParameters.getInstance().setIp(getResources().getString(R.string.host_ip));
        BackendRequestParameters.getInstance().setPort(Integer.parseInt(getResources().getString(R.string.host_port)));
        BackendRequestParameters.getInstance().setGetUserDataQuery(getResources().getString(R.string.user_data_query));
        BackendRequestParameters.getInstance().setSingupQuery(getResources().getString(R.string.signup_query));
        BackendRequestParameters.getInstance().setLoginQuery(getResources().getString(R.string.login_query));
        BackendRequestParameters.getInstance().setShouldSignupQuery(getResources().getString(R.string.should_signup_query));

        PersistanceMap.getInstance().init(getSharedPreferences(PREFS_NAME,0));

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

            if(GlobalParameters.getInstance().getUser()==null){
                showFragment(LOADING,false);
            }else{
                Intent intent=new Intent(this, ApplicationActivity.class);
                startActivity(intent);

                //showFragment(SIGNUP, false);
            }
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
        uiHelper.onPause();
        isResumed=false;
    }

    private synchronized void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible

        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            // Get the number of entries in the back stack
            int backStackSize = manager.getBackStackEntryCount();
            // Clear the back stack
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            if (state.isOpened()) {
                if(GlobalParameters.getInstance().getUser()==null){
                    onFacebookLogin();
                    showFragment(LOADING,false);
                } else{
                    Intent intent=new Intent(this, ApplicationActivity.class);
                    startActivity(intent);
                }
            } else if (state.isClosed()) {
                // If the session state is closed:
                // Show the login fragment
                showFragment(LOGIN, false);
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

    /**
     * This class represents the actions to be taken when the login is successful
     */
    private void onFacebookLogin(){
        final Session session=Session.getActiveSession();
        final Activity currentActivity=this;
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(final GraphUser user, final Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {

                                if(GlobalParameters.getInstance().getUser()==null){
                                    GlobalParameters.getInstance().setUser(Convertors.convertToUser(user));
                                }

                               /* if(login.getLoginStatus()!=LoginStatus.SUCCESS){
                                    login.login(user.getInnerJSONObject().toString(),new ILoginCallback() {
                                        @Override
                                        public void onLogin(LoginStatus loginStatus) {
                                            onCompleted(user,response);
                                        }
                                    });
                                }
                                else{*/
                                if(GlobalParameters.getInstance().getUser().getUsername()==null){
                                    showFragment(SIGNUP,false);
                                }else{
                                    Intent intent=new Intent(currentActivity, ApplicationActivity.class);
                                    startActivity(intent);

                                }
                                /*}*/
                            }
                        }
                        if (response.getError() != null) {
                        }
                    }
                });
        request.executeAsync();
    }

    public void onSignupButtonClick(View view) {
        ((SignupFragment) fragments[SIGNUP]).onSignupButtonClick(view);
    }
}
