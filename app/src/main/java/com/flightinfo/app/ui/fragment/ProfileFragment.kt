package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.flightinfo.app.databinding.FragmentProfileBinding
import com.flightinfo.app.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadCurrentUser()

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvUserName.text = "Username: ${user.firstName} ${user.lastName}"
                binding.tvUserEmail.text = "Email: ${user.email}"
            } else {
                // Handle user not found case
                binding.tvUserName.text = "Username: Not available"
                binding.tvUserEmail.text = "Email: Not available"
            }
        }

        binding.btnFavoriteRoutes.setOnClickListener {
            Toast.makeText(context, "Favorite Routes clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Favorite Routes screen
        }

        binding.btnBookmarkedFlights.setOnClickListener {
            Toast.makeText(context, "Bookmarked Flights clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Bookmarked Flights screen
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
