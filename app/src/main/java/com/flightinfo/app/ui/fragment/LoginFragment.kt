package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.flightinfo.app.R
import com.flightinfo.app.databinding.FragmentLoginBinding
import com.flightinfo.app.ui.viewmodel.AuthViewModel
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var bindingNullable: FragmentLoginBinding? = null
    private val binding get() = bindingNullable!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingNullable = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (validateInput(email, password)) {
                viewModel.login(email, password)
            }
        }

        binding.textViewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.editTextEmail.error = "请输入邮箱"
            return false
        }
        if (password.isEmpty()) {
            binding.editTextPassword.error = "请输入密码"
            return false
        }
        return true
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.buttonLogin.isEnabled = false
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonLogin.isEnabled = true
                        Toast.makeText(requireContext(), "登录成功", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.flightListFragment)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonLogin.isEnabled = true
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Idle<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonLogin.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingNullable = null
    }
}
