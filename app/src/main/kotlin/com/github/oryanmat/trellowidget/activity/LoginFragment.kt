package com.github.oryanmat.trellowidget.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.oryanmat.trellowidget.R
import com.github.oryanmat.trellowidget.databinding.FragmentLoginBinding
import com.github.oryanmat.trellowidget.util.AUTH_URL

class LoginFragment : androidx.fragment.app.Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginBtn.setOnClickListener { startBrowserWithAuthURL() }
        return binding.root
    }

    private fun startBrowserWithAuthURL() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL))
        if (intent.resolveActivity(requireActivity().packageManager) != null)
            startActivity(intent)
        else
            Toast.makeText(activity, getString(R.string.browser_error), Toast.LENGTH_SHORT).show()
    }
}