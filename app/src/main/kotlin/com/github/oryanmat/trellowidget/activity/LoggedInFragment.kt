package com.github.oryanmat.trellowidget.activity

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.VolleyError
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.T_WIDGET
import com.github.oryanmat.trellowidget.model.User
import com.github.oryanmat.trellowidget.util.Json
import com.github.oryanmat.trellowidget.util.TrelloAPIUtil
import kotlinx.android.synthetic.main.fragment_logged_in.view.*

class LoggedInFragment : Fragment() {
    val USER = "com.github.oryanmat.trellowidget.activity.user"
    val VISIBILITY = "com.github.oryanmat.trellowidget.activity.visibility"
    val MAX_LOGIN_FAIL = 3

    internal var user = User()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_logged_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val visibility = savedInstanceState?.getInt(VISIBILITY, View.VISIBLE) ?: View.VISIBLE

        if (visibility == View.GONE) {
            val userJson = savedInstanceState?.getString(USER) ?: ""
            setUser(Json.tryParseJson(userJson, User::class.java, User()))
        } else {
            TrelloAPIUtil.instance.getUserAsync(UserListener(), LoginErrorListener())
        }
    }

    internal inner class LoginErrorListener : Response.ErrorListener {
        var count = 1

        override fun onErrorResponse(error: VolleyError) {
            Log.e(T_WIDGET, error.toString())

            if (count >= MAX_LOGIN_FAIL) {
                // if user get request failed N times then logout so user can try to login again
                if (isAdded) {
                    (activity as MainActivity).logout()
                    val text = String.format(activity.getString(R.string.login_fail), error)
                    Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
                }
            } else {
                // try again. could be temp problem
                count += 1
                TrelloAPIUtil.instance.getUserAsync(UserListener(), this)
            }
        }
    }

    internal inner class UserListener : Response.Listener<String> {
        override fun onResponse(response: String) {
            setUser(Json.tryParseJson(response, User::class.java, User()))
        }
    }

    private fun setUser(user: User) {
        this.user = user
        view ?: return
        view.signed_text.text = String.format(getString(R.string.singed), user)
        view.loading_panel.visibility = View.GONE
        view.signed_panel.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        view ?: return
        outState.putInt(VISIBILITY, view.loading_panel.visibility)
        outState.putString(USER, Json.gson.toJson(user))
    }
}