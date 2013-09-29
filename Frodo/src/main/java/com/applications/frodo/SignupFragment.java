package com.applications.frodo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applications.frodo.blocks.SocialNetworks;
import com.applications.frodo.networking.BackendRequestParameters;
import com.applications.frodo.networking.ISignup;
import com.applications.frodo.networking.SignupFactory;
import com.applications.frodo.networking.SignupWithSocialNetworkID;
import com.facebook.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by siddharth on 24/09/13.
 */
public class SignupFragment extends Fragment {

    private ISignup signup;

    public SignupFragment(){
        super();
        signup= SignupFactory.getInstance().getSignup(SocialNetworks.FACEBOOK);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup,
                container, false);
        return view;
    }

    private void finishInit(){
    }

    public void onSignupButtonClick(View view) {

        TextView userNameTextView = (TextView) this.getActivity().findViewById(R.id.handle);
        String username=userNameTextView.getText().toString();

        TextView emailView = (TextView) this.getActivity().findViewById(R.id.email);
        String email=emailView.getText().toString();


        Map<String, String> singupData=new HashMap<String, String>();
        singupData.put("email", email);
        singupData.put("username", username);
        singupData.put("socialnetid", GlobalParameters.getInstance().getUser().getFacebookId());
    }

}
