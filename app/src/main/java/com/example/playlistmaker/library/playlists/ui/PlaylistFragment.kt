package com.example.playlistmaker.library.playlists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.favorites.domain.models.Playlist
import com.example.playlistmaker.library.favorites.domain.models.PlaylistState
import com.example.playlistmaker.library.playlists.createdPlaylist.ui.CreatedPlaylistFragment
import com.example.playlistmaker.utils.debounceDelay
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()

    private val playlistsAdapter = PlaylistCardAdapter()
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        onPlaylistClickDebounce = debounceDelay<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist ->
            playlist.id?.let {
                findNavController()
                    .navigate(
                        R.id.action_libraryFragment_to_createdPlaylistFragment,
                        CreatedPlaylistFragment.createArgs(it)
                    )
            }
        }

        customizeRecyclerView()
    }

    override fun onResume() {
        super.onResume()

        playlistsAdapter.playlists.clear()
        viewModel.getPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun customizeRecyclerView() {
        binding.playlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistRecyclerView.adapter = playlistsAdapter

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
        }
        playlistsAdapter.onItemClick = { playlist ->
            onPlaylistClickDebounce(playlist)
        }
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Empty -> showNoPlaylists()
            is PlaylistState.Success -> showSuccess(state.playlists)
        }
    }

    private fun showSuccess(playlists: List<Playlist>) {
        binding.emptyPlaylistLayout.visibility = View.GONE
        binding.playlistRecyclerView.visibility = View.VISIBLE

        playlistsAdapter.playlists.clear()
        playlistsAdapter.playlists.addAll(playlists)
        playlistsAdapter.notifyDataSetChanged()
    }

    private fun showNoPlaylists() {
        binding.emptyPlaylistLayout.visibility = View.VISIBLE
        binding.playlistRecyclerView.visibility = View.GONE
    }

    companion object {
        fun newInstance() = PlaylistFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}