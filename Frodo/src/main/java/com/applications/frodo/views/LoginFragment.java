package com.applications.frodo.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.R;
import com.applications.frodo.utils.Convertors;
import com.applications.frodo.views.home.ApplicationActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;


/**
 * Created by siddharth on 20/09/13.
 */
public class LoginFragment extends Fragment{

    private UiLifecycleHelper uiHelper;
    private boolean isResumed=false;

    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session,
                                 SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login,
                container, false);

        uiHelper = new UiLifecycleHelper(this.getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.facebook_login_button);
        String readPermissions[]= getResources().getString(R.string.facebook_read_permissions).split(",");
        authButton.setReadPermissions(Arrays.asList(readPermissions));

        return view;
    }

    private synchronized void onSessionStateChange(Session session, SessionState state, Exception exception) {

        System.out.println("sessions..");
        if (isResumed) {
            FragmentManager manager = this.getFragmentManager();
            // Get the number of entries in the back stack
            int backStackSize = manager.getBackStackEntryCount();
            // Clear the back stack
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            if (state.isOpened()) {
                if(GlobalParameters.getInstance().getUser()==null){
                    onFacebookLogin();
                } else{
                    Intent intent=new Intent(this.getActivity(), ApplicationActivity.class);
                    startActivity(intent);
                }
            }
        }
    }


    @Override
    public void onStart(){
        super.onStart();
        isResumed=true;
    }


    /**
     * This class represents the actions to be taken when the login is successful
     */
    private void onFacebookLogin(){

        final Session session=Session.getActiveSession();
        final Activity currentActivity=this.getActivity();

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

                                Intent intent=new Intent(currentActivity, ApplicationActivity.class);
                                startActivity(intent);
                            }
                        }
                        if (response.getError() != null) {
                        }
                    }
                });
        request.executeAsync();
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

    @Override
    public void onDestroy() {
        uiHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


    @Override
    public synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        uiHelper.onActivityResult(requestCode,resultCode,data);
    }
}