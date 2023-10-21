package com.github.oryanmat.trellowidget.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.TrelloWidget
import com.github.oryanmat.trellowidget.data.remote.Error
import com.github.oryanmat.trellowidget.data.remote.Success
import com.github.oryanmat.trellowidget.databinding.FragmentLoggedInBinding
import com.github.oryanmat.trellowidget.util.Constants.DELAY
import com.github.oryanmat.trellowidget.util.Constants.MAX_LOGIN_FAIL
import com.github.oryanmat.trellowidget.util.Constants.T_WIDGET_TAG
import com.github.oryanmat.trellowidget.viewmodels.LoggedInViewModel
import com.github.oryanmat.trellowidget.viewmodels.viewModelFactory
import java.util.Timer
import kotlin.concurrent.schedule

class LoggedInFragment : Fragment() {

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

        viewModel.liveUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Success -> {
                    viewModel.loggedInUser = response.data
                    setUser()
                }
                is Error -> onErrorResponse(response.error)
            }
        }
        if (viewModel.loggedInUser != null) {
            setUser()
        } else
            viewModel.tryLogin()
        return binding.root
    }

    private fun onErrorResponse(error: String) {
        Log.e(T_WIDGET_TAG, error)

        if (viewModel.loginAttempts >= MAX_LOGIN_FAIL)
            logout(error)
        else
            Timer().schedule(DELAY) { viewModel.tryLogin() }
    }

    private fun logout(error: String) {
        if (isAdded) {
            (activity as MainActivity).logout()
            val text = getString(R.string.login_fail).format(error)
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
        }
    }

    private fun setUser() {
        binding.signedText.text = getString(R.string.singed).format(viewModel.loggedInUser)
        binding.loadingPanel.visibility = View.GONE
        binding.signedPanel.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}