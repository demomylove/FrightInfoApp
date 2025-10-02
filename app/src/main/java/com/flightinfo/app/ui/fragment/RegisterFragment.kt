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
import com.flightinfo.app.databinding.FragmentRegisterBinding
import com.flightinfo.app.ui.viewmodel.AuthViewModel
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.buttonRegister.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

            if (validateInput(email, password, confirmPassword)) {
                viewModel.register(email, password)
            }
        }

        binding.textViewLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            binding.editTextEmail.error = "请输入邮箱"
            return false
        }
        if (password.isEmpty()) {
            binding.editTextPassword.error = "请输入密码"
            return false
        }
        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "请确认密码"
            return false
        }
        if (password != confirmPassword) {
            binding.editTextConfirmPassword.error = "两次输入的密码不一致"
            return false
        }
        if (password.length < 6) {
            binding.editTextPassword.error = "密码长度至少6位"
            return false
        }
        return true
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.buttonRegister.isEnabled = false
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonRegister.isEnabled = true
                        Toast.makeText(requireContext(), "注册成功", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.flightListFragment)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonRegister.isEnabled = true
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Idle<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonRegister.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
