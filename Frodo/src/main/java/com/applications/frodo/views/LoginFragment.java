package com.applications.frodo.views;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applications.frodo.R;
import com.facebook.widget.LoginButton;

import java.util.Arrays;


/**
 * Created by siddharth on 20/09/13.
 */
public class LoginFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login,
                container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.facebook_login_button);

        String readPermissions[]= getResources().getString(R.string.facebook_read_permissions).split(",");

        authButton.setReadPermissions(Arrays.asList(readPermissions));

        return view;
    }



}
