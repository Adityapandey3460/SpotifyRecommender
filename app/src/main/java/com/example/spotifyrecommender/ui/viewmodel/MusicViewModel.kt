package com.example.spotifyrecommender.ui.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyrecommender.data.model.Track
import com.example.spotifyrecommender.data.repository.AuthRepository
import com.example.spotifyrecommender.data.repository.SpotifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {
    private val spotifyRepository = SpotifyRepository()
    private val authRepository = AuthRepository()
    private var mediaPlayer: MediaPlayer? = null

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // NEW: Track the current screen state
    private val _currentScreen = MutableStateFlow<ScreenState>(ScreenState.POPULAR)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    sealed class ScreenState {
        object POPULAR : ScreenState()
        object SEARCH_RESULTS : ScreenState()
        object RECOMMENDATIONS : ScreenState()
    }

    init {
        loadPopularTracks()
    }

    fun loadPopularTracks() {
        _isLoading.value = true
        _errorMessage.value = null
        _searchQuery.value = ""
        _currentScreen.value = ScreenState.POPULAR

        viewModelScope.launch {
            try {
                val token = authRepository.getAccessToken()
                if (token == null) {
                    _errorMessage.value = "Failed to get Spotify token"
                    return@launch
                }

                val tracks = spotifyRepository.getPopularTracks(token)
                _tracks.value = tracks

                if (tracks.isEmpty()) {
                    _errorMessage.value = "No popular tracks found"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                _tracks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // SIMPLIFIED: Just search and show direct results
    fun searchTracks(query: String) {
        if (query.isBlank()) {
            loadPopularTracks()
            return
        }

        _isLoading.value = true
        _errorMessage.value = null
        _searchQuery.value = query
        _currentScreen.value = ScreenState.SEARCH_RESULTS

        viewModelScope.launch {
            try {
                val token = authRepository.getAccessToken()
                if (token == null) {
                    _errorMessage.value = "Failed to get Spotify token"
                    return@launch
                }

                println("🔍 Searching for: '$query'")
                val searchResults = spotifyRepository.searchTracks(token, query)
                _tracks.value = searchResults

                if (searchResults.isEmpty()) {
                    _errorMessage.value = "No songs found for '$query'"
                } else {
                    println("✅ Found ${searchResults.size} tracks for '$query'")
                }

            } catch (e: Exception) {
                _errorMessage.value = "Search error: ${e.message}"
                _tracks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // SEPARATE function for recommendations
    fun getRecommendationsBasedOnTrack(trackId: String) {
        _isLoading.value = true
        _errorMessage.value = null
        _currentScreen.value = ScreenState.RECOMMENDATIONS

        viewModelScope.launch {
            try {
                val token = authRepository.getAccessToken()
                if (token == null) {
                    _errorMessage.value = "Failed to get Spotify token"
                    return@launch
                }

                val recommendations = spotifyRepository.getRecommendationsBasedOnTrack(token, trackId)
                _tracks.value = recommendations

                if (recommendations.isEmpty()) {
                    _errorMessage.value = "No recommendations found"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Recommendation error: ${e.message}"
                _tracks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // NEW: Refresh current data
    fun refreshData() {
        when (_currentScreen.value) {
            is ScreenState.POPULAR -> loadPopularTracks()
            is ScreenState.SEARCH_RESULTS -> {
                val query = _searchQuery.value
                if (query.isNotBlank()) {
                    searchTracks(query)
                } else {
                    loadPopularTracks()
                }
            }
            is ScreenState.RECOMMENDATIONS -> {
                // You might want to keep recommendations or go back to popular
                loadPopularTracks()
            }
        }
    }

    // NEW: Clear search and go back to popular
    fun clearSearch() {
        _searchQuery.value = ""
        loadPopularTracks()
    }

    fun playTrack(track: Track) {
        mediaPlayer?.release()
        _currentTrack.value = track
        _errorMessage.value = null

        track.previewUrl?.let { url ->
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(url)
                    setOnPreparedListener {
                        start()
                        _isPlaying.value = true
                    }
                    setOnCompletionListener {
                        _isPlaying.value = false
                    }
                    setOnErrorListener { _, what, extra ->
                        _errorMessage.value = "Playback error"
                        _isPlaying.value = false
                        true
                    }
                    prepareAsync()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Cannot play track preview"
            }
        } ?: run {
            _errorMessage.value = "No preview available for this track"
        }
    }

    fun pauseTrack() {
        mediaPlayer?.pause()
        _isPlaying.value = false
    }

    fun resumeTrack() {
        mediaPlayer?.start()
        _isPlaying.value = true
    }

    fun clearError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        mediaPlayer?.release()
        super.onCleared()
    }
}