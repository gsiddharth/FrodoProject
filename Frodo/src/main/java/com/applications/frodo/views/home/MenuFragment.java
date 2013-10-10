package com.applications.frodo.views.home;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.applications.frodo.R;
import com.applications.frodo.views.checkin.CheckinActivity;
import com.facebook.widget.LoginButton;

import java.util.Arrays;


/**
 * Created by siddharth on 20/09/13.
 */
public class MenuFragment extends Fragment{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "1";

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu, container, false);
        Button b=(Button) rootView.findViewById(R.id.checkinMenuButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckinButtonClick(view);
            }
        });

        return rootView;
    }

    public void onCheckinButtonClick(View view){
        Intent intent=new Intent(this.getActivity(), CheckinActivity.class);
        startActivity(intent);
    }
}
