package com.applications.frodo.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applications.frodo.R;

/**
 * Created by siddharth on 28/09/13.
 */
public class LoadingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading,
                container, false);
        return view;
    }
}
