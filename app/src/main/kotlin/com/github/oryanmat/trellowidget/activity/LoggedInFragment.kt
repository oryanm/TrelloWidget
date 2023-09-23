package com.github.oryanmat.trellowidget.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.volley.Response
import com.android.volley.VolleyError
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.util.Constants.T_WIDGET_TAG
import com.github.oryanmat.trellowidget.databinding.FragmentLoggedInBinding
import com.github.oryanmat.trellowidget.data.model.User
import com.github.oryanmat.trellowidget.util.Json
import com.github.oryanmat.trellowidget.TrelloWidget
import com.github.oryanmat.trellowidget.util.Constants.DELAY
import com.github.oryanmat.trellowidget.util.Constants.MAX_LOGIN_FAIL
import com.github.oryanmat.trellowidget.viewmodels.LoggedInViewModel
import com.github.oryanmat.trellowidget.viewmodels.viewModelFactory
import java.util.*
import kotlin.concurrent.schedule

class LoggedInFragment : Fragment(), Response.Listener<String>, Response.ErrorListener {

    private val viewModel: LoggedInViewModel by viewModels {
        viewModelFactory {
            LoggedInViewModel(TrelloWidget.appModule.trelloWidgetRepository)
        }
    }

    private var _binding: FragmentLoggedInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoggedInBinding.inflate(inflater, container, false)

        if (viewModel.loadingPanelVisibility == View.GONE)
            setUser()
        else
            tryLogin()
        return binding.root
    }

    override fun onResponse(response: String) {
        viewModel.user = Json.tryParseJson(response, User::class.java, User())
        setUser()
    }

    override fun onErrorResponse(error: VolleyError) {
        Log.e(T_WIDGET_TAG, error.toString())

        if (viewModel.loginAttempts >= MAX_LOGIN_FAIL) {
            // logout after N failed get requests so we can try to login later
            logout(error)
        } else {
            // try again shortly. could be temp problem
            Timer().schedule(DELAY) { tryLogin() }
        }
    }

    private fun tryLogin() {
        viewModel.loginAttempts++
        TrelloWidget.appModule.trelloWidgetRepository.getUser(this, this)
    }

    private fun logout(error: VolleyError) {
        if (isAdded) {
            (activity as MainActivity).logout()
            val text = getString(R.string.login_fail).format(error)
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
        }
    }

    private fun setUser() {
        val user = viewModel.user
        binding.signedText.text = getString(R.string.singed).format(user)
        binding.loadingPanel.visibility = View.GONE
        binding.signedPanel.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}