package com.example.playlistmaker.library.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.library.domain.models.FavoritesState
import com.example.playlistmaker.library.ui.view_models.FavoritesViewModel
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utils.debounceDelay
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel by viewModel<FavoritesViewModel>()

    private val favoritesTrackAdapter = TrackAdapter()

    private lateinit var onClickDebounce: (Track) -> Unit
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesTrackAdapter.tracks.clear()
        viewModel.getFavoritesList()

        onClickDebounce =
            debounceDelay<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false)
            { track ->
                val player = Intent(requireContext(), PlayerActivity::class.java)
                player.putExtra("track", track.copy(isFavorite = true))
                startActivity(player)
            }

        favoritesTrackAdapter.onItemClick = { track ->
            onClickDebounce(track)
        }

        binding.favoritesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.favoritesRecyclerView.adapter = favoritesTrackAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> showNothingFound()
            is FavoritesState.Success -> showSuccess(state.tracks)
        }

    }

    private fun showNothingFound() {
        binding.mediaLibEmpty.visibility = View.VISIBLE
        binding.favoritesRecyclerView.visibility = View.GONE
    }

    private fun showSuccess(tracks: List<Track>) {
        binding.mediaLibEmpty.visibility = View.GONE
        binding.favoritesRecyclerView.visibility = View.VISIBLE
        favoritesTrackAdapter.tracks.clear()
        favoritesTrackAdapter.tracks.addAll(tracks)
        favoritesTrackAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoritesList()
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}