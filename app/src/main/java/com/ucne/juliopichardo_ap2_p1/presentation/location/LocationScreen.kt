package com.ucne.juliopichardo_ap2_p1.presentation.location

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen(
    viewModel: LocationViewModel = hiltViewModel(),
    goBackListScreen: () -> Unit
) {
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(key1 = locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            viewModel.getCurrentLocation()
        }
    }

    val currentLocation = viewModel.currentLocation

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = locationPermissions.allPermissionsGranted,
            label = ""
        ) { areGranted ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (areGranted) {
                    Text(text = "${currentLocation?.latitude ?: 0.0} ${currentLocation?.longitude ?: 0.0}")
                    Button(
                        onClick = { viewModel.getCurrentLocation() }
                    ) {
                        Text(text = "Get current location")
                    }
                } else {
                    Text(text = "We need location permissions for this app.")
                    Button(
                        onClick = { locationPermissions.launchMultiplePermissionRequest() }
                    ) {
                        Text(text = "Accept")
                    }
                }
                FloatingActionButton(onClick = { goBackListScreen() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Articles List")
                }
            }
        }
    }
}