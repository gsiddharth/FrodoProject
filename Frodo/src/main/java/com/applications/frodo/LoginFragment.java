package com.applications.frodo;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
