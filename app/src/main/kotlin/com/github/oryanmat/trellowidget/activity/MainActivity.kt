package com.github.oryanmat.trellowidget.activity

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.github.oryanmat.trellowidget.INTERNAL_PREFS
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.util.AUTH_URL
import com.github.oryanmat.trellowidget.util.TOKEN_PREF_KEY

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replaceFragment(if (userToken.isEmpty()) LoginFragment() else LoggedInFragment())
        }
    }

    val userToken: String
        get() = getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)
                .getString(TOKEN_PREF_KEY, "")

    fun startBrowserWithAuthURL(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL))

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == Intent.ACTION_VIEW) {
            saveUserToken(intent)
        }
    }

    fun saveUserToken(intent: Intent) {
        getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(TOKEN_PREF_KEY, intent.data.fragment)
                .commit()

        replaceFragment(LoggedInFragment())
    }

    @JvmOverloads fun logout(view: View? = null) {
        getSharedPreferences(INTERNAL_PREFS, Context.MODE_PRIVATE)
                .edit()
                .remove(TOKEN_PREF_KEY)
                .commit()

        replaceFragment(LoginFragment())
    }

    fun replaceFragment(fragment: Fragment) = fragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
}