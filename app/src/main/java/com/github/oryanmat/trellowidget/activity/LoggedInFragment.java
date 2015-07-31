package com.github.oryanmat.trellowidget.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.oryanmat.trellowidget.R;
import com.github.oryanmat.trellowidget.model.User;
import com.github.oryanmat.trellowidget.util.HttpErrorListener;
import com.github.oryanmat.trellowidget.util.Json;
import com.github.oryanmat.trellowidget.util.TrelloAPIUtil;

public class LoggedInFragment extends Fragment {
    static final String USER = "com.github.oryanmat.trellowidget.activity.user";
    static final String VISIBILITY = "com.github.oryanmat.trellowidget.activity.visibility";

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logged_in, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int visibility = savedInstanceState != null ?
                savedInstanceState.getInt(VISIBILITY, View.VISIBLE) : View.VISIBLE;

        if (visibility == View.GONE) {
            setUser(Json.tryParseJson(savedInstanceState.getString(USER), User.class, new User()));
        } else {
            TrelloAPIUtil.instance.getAsync(TrelloAPIUtil.instance.user(),
                    new UserListener(), new LoginErrorListener(getActivity()));
        }
    }

    class LoginErrorListener extends HttpErrorListener {
        public LoginErrorListener(Context context) {
            super(context);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            super.onErrorResponse(error);
            // if user get request failed then logout so user can try to login again
            ((MainActivity)getActivity()).logout();
        }
    }

    class UserListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            setUser(Json.tryParseJson(response, User.class, new User()));
        }
    }

    private void setUser(User user) {
        this.user = user;
        TextView text = (TextView) getActivity().findViewById(R.id.signed_text);
        text.setText(String.format(getString(R.string.singed), user));
        getActivity().findViewById(R.id.loading_panel).setVisibility(View.GONE);
        getActivity().findViewById(R.id.signed_panel).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int visibility = getActivity().findViewById(R.id.loading_panel).getVisibility();
        outState.putInt(VISIBILITY, visibility);
        outState.putString(USER, Json.get().toJson(user));
    }
}
