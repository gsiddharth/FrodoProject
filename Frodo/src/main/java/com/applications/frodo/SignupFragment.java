package com.applications.frodo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by siddharth on 24/09/13.
 */
public class SignupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup,
                container, false);
        return view;
    }

    private void finishInit(){
    }

    private class SignupClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v){
            //TODO: Load the new activity for handling login by user name and password

        }

    }
}