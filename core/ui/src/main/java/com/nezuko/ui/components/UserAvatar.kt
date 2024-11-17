package com.nezuko.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nezuko.ui.R

@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    photoUrl: String
) {
    ImageFromInet(url = photoUrl, errorImageResource = R.drawable.profile)
}