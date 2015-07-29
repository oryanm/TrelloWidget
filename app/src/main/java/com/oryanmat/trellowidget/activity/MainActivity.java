package com.oryanmat.trellowidget.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.oryanmat.trellowidget.R;
import com.oryanmat.trellowidget.util.TrelloAPIUtil;

import static com.oryanmat.trellowidget.TrelloWidget.INTERNAL_PREFS;
import static com.oryanmat.trellowidget.TrelloWidget.T_WIDGET;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment fragment = getUserToken().isEmpty() ? new LoginFragment() : new LoggedInFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private String getUserToken() {
        return getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)
                .getString(TrelloAPIUtil.TOKEN_PREF_KEY, "");
    }

    public void startBrowserWithAuthURL(View view) {
        Uri uri = Uri.parse(TrelloAPIUtil.AUTH_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(Intent.ACTION_VIEW)){
            saveUserToken(intent);
        }
    }

    private void saveUserToken(Intent intent) {
        String fragment = intent.getData().getFragment();

        getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(TrelloAPIUtil.TOKEN_PREF_KEY, fragment)
                .commit();

        replaceFragment(new LoggedInFragment());
    }

    public void logout(View view) {
        getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)
                .edit()
                .remove(TrelloAPIUtil.TOKEN_PREF_KEY)
                .commit();

        replaceFragment(new LoginFragment());
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
