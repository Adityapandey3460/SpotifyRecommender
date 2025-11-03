package com.example.spotifyrecommender.ui.screens
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.SideEffect


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spotifyrecommender.ui.components.LoadingShimmer
import com.example.spotifyrecommender.ui.components.PlayerControls
import com.example.spotifyrecommender.ui.components.TrackCard
import com.example.spotifyrecommender.ui.theme.SpotifyGreen
import com.example.spotifyrecommender.ui.viewmodel.MusicViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    viewModel: MusicViewModel = viewModel()
) {
    val tracks by viewModel.tracks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Search state
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // Error Dialog
    if (!errorMessage.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(errorMessage!!) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }

    val systemUiController = rememberSystemUiController()

    // 🖤 Status bar color set here
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Black,   // ← this line controls the black color
            darkIcons = false      // ← false = white icons
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Spotify Recommender",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SpotifyGreen
                ),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (searchQuery.isNotEmpty()) {
                        viewModel.searchTracks(searchQuery)
                    } else {
                        viewModel.loadPopularTracks()
                    }
                },
                containerColor = SpotifyGreen
            ) {
                Icon(
                    if (searchQuery.isNotEmpty()) Icons.Default.Search else Icons.Default.Refresh,
                    if (searchQuery.isNotEmpty()) "Search" else "Load Recommendations"
                )
            }
        },
        bottomBar = {
            PlayerControls(
                currentTrack = currentTrack,
                isPlaying = isPlaying,
                onPlayPause = {
                    if (isPlaying) viewModel.pauseTrack()
                    else currentTrack?.let { viewModel.resumeTrack() }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            SpotifyGreen.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            // Modern Search Bar
            AnimatedContent(
                targetState = isSearchActive,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = ""
            ) { active ->
                if (active) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        IconButton(onClick = { isSearchActive = false }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search songs, artists...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, shape = MaterialTheme.shapes.large),
                            singleLine = true,
                            shape = MaterialTheme.shapes.large,
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (searchQuery.isNotEmpty()) {
                                        viewModel.searchTracks(searchQuery)
                                        isSearchActive = false
                                        keyboardController?.hide()
                                    }
                                }) {
                                    Icon(Icons.Default.Search, contentDescription = "Search")
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (searchQuery.isNotEmpty()) {
                                        viewModel.searchTracks(searchQuery)
                                        isSearchActive = false
                                        keyboardController?.hide()
                                    }
                                }
                            )
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search songs, artists...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { isSearchActive = true },
                        singleLine = true
                    )
                }
            }

            // Content
            AnimatedVisibility(
                visible = !isSearchActive,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    when {
                        isLoading -> LoadingShimmer()

                        tracks.isEmpty() -> Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (searchQuery.isNotEmpty())
                                    "Search for songs to get recommendations"
                                else
                                    "Welcome to Spotify Recommender!",
                                modifier = Modifier.padding(16.dp)
                            )
                            Text(
                                text = if (searchQuery.isNotEmpty())
                                    "Enter a search term and press Enter"
                                else
                                    "Tap the refresh button to load recommendations",
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        else -> LazyColumn(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            items(tracks) { track ->
                                TrackCard(
                                    track = track,
                                    onClick = { viewModel.playTrack(track) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


