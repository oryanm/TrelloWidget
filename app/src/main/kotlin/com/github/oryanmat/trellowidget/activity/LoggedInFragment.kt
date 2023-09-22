package com.github.oryanmat.trellowidget.activity

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
import com.github.oryanmat.trellowidget.databinding.FragmentLoggedInBinding
import com.github.oryanmat.trellowidget.data.model.User
import com.github.oryanmat.trellowidget.util.Json
import com.github.oryanmat.trellowidget.data.TrelloWidgetRepository
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class LoggedInFragment : androidx.fragment.app.Fragment(), Response.Listener<String>, Response.ErrorListener {
    private val USER = "com.github.oryanmat.trellowidget.activity.user"
    private val VISIBILITY = "com.github.oryanmat.trellowidget.activity.visibility"
    private val MAX_LOGIN_FAIL = 3
    private val DELAY = TimeUnit.SECONDS.toMillis(1)

    private var user = User()
    private var loginAttempts = 0

    private var _binding: FragmentLoggedInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoggedInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val visibility = savedInstanceState?.getInt(VISIBILITY, View.VISIBLE) ?: View.VISIBLE

        if (visibility == View.GONE) {
            val userJson = savedInstanceState?.getString(USER) ?: ""
            setUser(Json.tryParseJson(userJson, User::class.java, User()))
        } else {
            login()
        }
    }

    override fun onErrorResponse(error: VolleyError) {
        Log.e(T_WIDGET, error.toString())

        if (loginAttempts >= MAX_LOGIN_FAIL) {
            // logout after N failed get requests so we can try to login later
            logout(error)
        } else {
            // try again shortly. could be temp problem
            Timer().schedule(DELAY, { login() })
        }
    }

    private fun login() {
        loginAttempts++
        TrelloWidgetRepository.instance.getUser(this, this)
    }

    private fun logout(error: VolleyError) {
        if (isAdded) {
            (activity as MainActivity).logout()
            val text = getString(R.string.login_fail).format(error)
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
        }
    }

    override fun onResponse(response: String) = setUser(Json.tryParseJson(response, User::class.java, User()))

    private fun setUser(user: User) {
        this.user = user
        binding.signedText.text = getString(R.string.singed).format(user)
        binding.loadingPanel.visibility = View.GONE
        binding.signedPanel.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(VISIBILITY, binding.loadingPanel.visibility)
        outState.putString(USER, Json.toJson(user))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}