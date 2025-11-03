package com.example.spotifyrecommender

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spotifyrecommender.ui.screens.HomeScreen
import com.example.spotifyrecommender.ui.theme.SpotifyRecommenderTheme
import com.example.spotifyrecommender.ui.viewmodel.MusicViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContent {
                SpotifyRecommenderTheme {
                    val viewModel: MusicViewModel = viewModel()

                    // Show error messages
                    LaunchedEffect(viewModel.errorMessage) {
                        viewModel.errorMessage.collect { error ->
                            error?.let {
                                Toast.makeText(
                                    this@MainActivity,
                                    it,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    HomeScreen(viewModel = viewModel)
                }
            }
        } catch (e: Exception) {
            // Show crash reason
            Toast.makeText(this, "App crashed: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}