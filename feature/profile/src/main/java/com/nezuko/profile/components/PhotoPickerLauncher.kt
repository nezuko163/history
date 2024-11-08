package com.nezuko.profile.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun photoPickerLauncher(
    onUriSelected: (Uri) -> Unit
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    return rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            onUriSelected(uri)
            Log.d("photoPickerLauncher", "Selected URI: $uri")
        } else {
            Log.d("photoPickerLauncher", "No media selected")
        }
    }
}