package com.oryanmat.trellowidget.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.oryanmat.trellowidget.R;
import com.oryanmat.trellowidget.model.User;
import com.oryanmat.trellowidget.util.HttpErrorListener;
import com.oryanmat.trellowidget.util.Json;
import com.oryanmat.trellowidget.util.TrelloAPIUtil;

public class LoggedInFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logged_in, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TrelloAPIUtil.instance.getAsync(TrelloAPIUtil.instance.user(),
                new UserListener(), new HttpErrorListener(getActivity()));
    }

    class UserListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            User user = Json.tryParseJson(response, User.class, new User());
            TextView text = (TextView) getActivity().findViewById(R.id.signed_text);
            text.setText(String.format(getString(R.string.singed), user));
            getActivity().findViewById(R.id.loading_panel).setVisibility(View.GONE);
            getActivity().findViewById(R.id.signed_panel).setVisibility(View.VISIBLE);
        }
    }
}
