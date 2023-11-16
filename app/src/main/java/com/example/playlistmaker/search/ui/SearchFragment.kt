package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksSearchState
import com.example.playlistmaker.search.view_model.TracksSearchViewModel
import com.example.playlistmaker.utils.debounceDelay
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<TracksSearchViewModel>()

    private val trackAdapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()

    private lateinit var onClickDebounce: (Track) -> Unit
    private lateinit var inputTextWatcher: TextWatcher

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        customizeRecyclerView()

        onClickDebounce = debounceDelay<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            viewModel.addTrackToHistory(track)
            historyAdapter.notifyDataSetChanged()
            openPlayer(track)
        }

        binding.ivSearchClear.visibility = clearButtonVisibility(viewModel.getLastSearchText())

        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.observeHistoryState().observe(viewLifecycleOwner) {
            historyAdapter.tracks.clear()
            historyAdapter.tracks.addAll(it)
            historyAdapter.notifyDataSetChanged()
        }

        if (viewModel.isHistoryEmpty()) {
            binding.searchHistoryLayout.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        inputTextWatcher.let { binding.etSearch.removeTextChangedListener(it) }
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateSearchResults(trackAdapter.tracks)
        viewModel.updateHistoryList()
    }

    private fun setListeners() {
        binding.ivSearchClear.setOnClickListener {
            binding.etSearch.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)

            trackAdapter.tracks.clear()
            viewModel.clearSearchingText()
        }


        binding.searchRefreshButton.setOnClickListener {
            viewModel.startSearch()
        }

        binding.searchClearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            historyAdapter.notifyDataSetChanged()
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)

                viewModel.startSearch()
                true
            } else false
        }


        inputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivSearchClear.visibility = clearButtonVisibility(s)
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.etSearch.setText(viewModel.getLastSearchText())
        binding.etSearch.addTextChangedListener(inputTextWatcher)
    }

    private fun customizeRecyclerView() {
        trackAdapter.onItemClick = { track ->
            onClickDebounce(track)
        }

        historyAdapter.onItemClick = { track ->
            onClickDebounce(track)
        }

        binding.rvTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTracks.adapter = trackAdapter

        binding.rvTracksHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTracksHistory.adapter = historyAdapter
        viewModel.updateHistoryList()
    }

    private fun clearButtonVisibility(s: CharSequence?) =
        if (s.isNullOrEmpty()) View.GONE else View.VISIBLE


    private fun renderState(state: TracksSearchState) {
        when (state) {
            is TracksSearchState.Loading -> showLoading()
            is TracksSearchState.NothingFound -> showNothingFound()
            is TracksSearchState.NoInternet -> showNoInternet()
            is TracksSearchState.Success -> showSuccess(state.tracks)
            is TracksSearchState.History -> showHistory()
        }
    }

    private fun showLoading() {
        binding.searchSomethingWentWrong.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.searchHistoryLayout.visibility = View.GONE
    }

    private fun showSuccess(tracks: List<Track>) {
        binding.progressBar.visibility = View.GONE
        binding.searchSomethingWentWrong.visibility = View.GONE
        binding.searchHistoryLayout.visibility = View.GONE
        binding.rvTracks.visibility = View.VISIBLE

        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showNothingFound() {
        binding.progressBar.visibility = View.GONE
        binding.searchSomethingWentWrong.visibility = View.VISIBLE
        binding.searchErrorIv.setImageResource(R.drawable.ic_nothing_found)
        binding.errorTextTv.text = getString(R.string.nothing_found_string)
        binding.searchRefreshButton.visibility = View.GONE
        binding.searchHistoryLayout.visibility = View.GONE
        trackAdapter.notifyDataSetChanged()
    }

    private fun showNoInternet() {
        binding.progressBar.visibility = View.GONE
        trackAdapter.tracks.clear()
        binding.searchSomethingWentWrong.visibility = View.VISIBLE
        binding.searchErrorIv.setImageResource(R.drawable.ic_no_internet)
        binding.errorTextTv.text = getString(R.string.no_internet_search_failure)
        binding.searchRefreshButton.visibility = View.VISIBLE
        binding.searchHistoryLayout.visibility = View.GONE
        trackAdapter.notifyDataSetChanged()
    }

    private fun showHistory() {
        binding.progressBar.visibility = View.GONE
        binding.searchSomethingWentWrong.visibility = View.GONE
        binding.rvTracks.visibility = View.GONE
        binding.searchHistoryLayout.visibility =
            if (viewModel.isHistoryEmpty()) View.GONE else View.VISIBLE
    }

    private fun openPlayer(track: Track) {
        val player = Intent(requireContext(), PlayerActivity::class.java)
        player.putExtra("track", track)
        startActivity(player)
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}
