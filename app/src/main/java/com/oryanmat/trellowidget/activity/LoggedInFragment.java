package com.oryanmat.trellowidget.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oryanmat.trellowidget.R;

public class LoggedInFragment extends Fragment {
    public LoggedInFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logged_in, container, false);
    }
}
