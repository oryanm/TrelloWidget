package com.github.oryanmat.trellowidget.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.oryanmat.trellowidget.R;
import com.github.oryanmat.trellowidget.model.User;
import com.github.oryanmat.trellowidget.util.Json;
import com.github.oryanmat.trellowidget.util.TrelloAPIUtil;

import static com.github.oryanmat.trellowidget.TrelloWidget.T_WIDGET;

public class LoggedInFragment extends Fragment {
    static final String USER = "com.github.oryanmat.trellowidget.activity.user";
    static final String VISIBILITY = "com.github.oryanmat.trellowidget.activity.visibility";
    static final int MAX_LOGIN_FAIL = 3;

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
                    new UserListener(), new LoginErrorListener());
        }
    }

    class LoginErrorListener implements Response.ErrorListener {
        int count = 1;

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(T_WIDGET, error.toString());

            if (count >= MAX_LOGIN_FAIL) {
                // if user get request failed N times then logout so user can try to login again
                if (isAdded()) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.logout();
                    String text = String.format(activity.getString(R.string.login_fail), error);
                    Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
                }
            } else {
                // try again. could be temp problem
                count += 1;
                TrelloAPIUtil.instance.getAsync(TrelloAPIUtil.instance.user(), new UserListener(), this);
            }
        }
    }

    class UserListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            setUser(Json.tryParseJson(response, User.class, new User()));
        }
    }

    void setUser(User user) {
        this.user = user;
        View view = getView();
        if (view == null) return;
        TextView text = (TextView) view.findViewById(R.id.signed_text);
        text.setText(String.format(getString(R.string.singed), user));
        view.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        view.findViewById(R.id.signed_panel).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        View view = getView();
        if (view == null) return;
        int visibility = view.findViewById(R.id.loading_panel).getVisibility();
        outState.putInt(VISIBILITY, visibility);
        outState.putString(USER, Json.get().toJson(user));
    }
}
