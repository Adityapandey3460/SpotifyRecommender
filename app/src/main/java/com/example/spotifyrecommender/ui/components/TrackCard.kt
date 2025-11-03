//package com.example.spotifyrecommender.ui.components
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.PlayArrow
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
//import com.example.spotifyrecommender.data.model.Track
//import com.example.spotifyrecommender.ui.theme.SpotifyGreen
//
//@Composable
//fun TrackCard(
//    track: Track,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//            .padding(16.dp),
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Album Art with Play Button Overlay
//        Box(
//            modifier = Modifier.size(60.dp)
//        ) {
//            AsyncImage(
//                model = track.album.images.firstOrNull()?.url,
//                contentDescription = "Album art for ${track.album.name}",
//                modifier = Modifier
//                    .size(60.dp)
//                    .clip(RoundedCornerShape(8.dp)),
//                contentScale = ContentScale.Crop
//            )
//
//            // Play Button Overlay
//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .clip(RoundedCornerShape(8.dp))
//                    .clickable(onClick = onClick),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Default.PlayArrow,
//                    contentDescription = "Play",
//                    tint = SpotifyGreen,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//        }
//
//        // Track Info
//        Column(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            Text(
//                text = track.name,
//                style = MaterialTheme.typography.bodyLarge,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.onBackground,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            Text(
//                text = track.artists.joinToString { it.name },
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//    }
//}

package com.example.spotifyrecommender.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spotifyrecommender.data.model.Track
import com.example.spotifyrecommender.ui.theme.SpotifyGreen

@Composable
fun TrackCard(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Album Art with Play Button Overlay
        Box(
            modifier = Modifier.size(60.dp)
        ) {
            AsyncImage(
                model = track.album.images.firstOrNull()?.url,
                contentDescription = "Album art for ${track.album.name}",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Play Button Overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = SpotifyGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Track Info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = track.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = track.artists.joinToString { it.name },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}