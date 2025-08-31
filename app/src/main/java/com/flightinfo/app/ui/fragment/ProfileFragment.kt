package com.flightinfo.app.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.flightinfo.app.R
import com.flightinfo.app.databinding.FragmentProfileBinding
import com.flightinfo.app.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("app_preferences", android.content.Context.MODE_PRIVATE)
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

        // Setup theme selection
        setupThemeSelection()

        binding.btnFavoriteRoutes.setOnClickListener {
            Toast.makeText(context, "Favorite Routes clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Favorite Routes screen
        }

        binding.btnBookmarkedFlights.setOnClickListener {
            Toast.makeText(context, "Bookmarked Flights clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Bookmarked Flights screen
        }
    }

    private fun setupThemeSelection() {
        updateCurrentThemeText()

        binding.ivThemeArrow.setOnClickListener {
            showThemeDialog()
        }

        binding.root.findViewById<View>(R.id.theme_card).setOnClickListener {
            showThemeDialog()
        }
    }

    private fun updateCurrentThemeText() {
        val currentMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val themeText = when (currentMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> getString(R.string.theme_light)
            AppCompatDelegate.MODE_NIGHT_YES -> getString(R.string.theme_dark)
            else -> getString(R.string.theme_system)
        }
        binding.tvCurrentTheme.text = themeText
    }

    private fun showThemeDialog() {
        val currentMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.theme_settings)
            .setSingleChoiceItems(
                arrayOf(
                    getString(R.string.theme_light),
                    getString(R.string.theme_dark),
                    getString(R.string.theme_system),
                ),
                when (currentMode) {
                    AppCompatDelegate.MODE_NIGHT_NO -> 0
                    AppCompatDelegate.MODE_NIGHT_YES -> 1
                    else -> 2
                },
                null,
            )
            .setPositiveButton(R.string.restart_app) { dialog, which ->
                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                val newMode = when (selectedPosition) {
                    0 -> AppCompatDelegate.MODE_NIGHT_NO
                    1 -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }

                if (newMode != currentMode) {
                    sharedPreferences.edit().putInt("theme_mode", newMode).apply()
                    AppCompatDelegate.setDefaultNightMode(newMode)

                    // Show confirmation message
                    Toast.makeText(
                        requireContext(),
                        R.string.theme_applied_immediately,
                        Toast.LENGTH_SHORT,
                    ).show()

                    updateCurrentThemeText()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
