package com.flightinfo.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.flightinfo.app.databinding.FragmentFavoriteRoutesBinding
import com.flightinfo.app.ui.adapter.FavoriteRouteAdapter
import com.flightinfo.app.ui.viewmodel.FavoriteRoutesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRoutesFragment : Fragment() {

    private var _binding: FragmentFavoriteRoutesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteRoutesViewModel by viewModels()
    private lateinit var adapter: FavoriteRouteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteRoutesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteRouteAdapter { route ->
            viewModel.removeFavorite(route.originAirport, route.destinationAirport)
        }

        binding.favoriteRoutesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.favoriteRoutesRecyclerView.adapter = adapter

        viewModel.favoriteRoutes.observe(viewLifecycleOwner) {
                routes ->
            adapter.submitList(routes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
