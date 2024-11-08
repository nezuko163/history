package com.nezuko.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nezuko.domain.model.UserProfile
import com.nezuko.ui.components.UserAvatar

private const val TAG = "ProfileScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userProfile: UserProfile,
    onArrowBackClick: () -> Unit,
    onProfileIconClick: () -> Unit,
) {
    Log.i(TAG, "ProfileScreen: recomp")
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "Profile", modifier = Modifier.align(Alignment.BottomEnd))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {

            TopAppBar(
                title = {
                    Text(
                        text = userProfile.name
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onArrowBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onProfileIconClick) {
                        UserAvatar(photoUrl = userProfile.photoUrl)
                    }
                }
            )
        }
    ) { paddingValues ->
        Log.i(TAG, "ProfileScreen: $paddingValues")
    }
}