package com.nezuko.profile.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable

@Composable
fun imageCropperLauncher(
    onCropImage: (Uri) -> Unit
) =
    rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
        if (result.isSuccessful) {
            if (result.uriContent != null) {
                Log.i(
                    "imageCropperLauncher",
                    "imageCropperLauncher: croppedUri - ${result.uriContent}"
                )
                onCropImage(result.uriContent!!)
            } else {
                Log.e(
                    "imageCropperLauncher",
                    "imageCropperLauncher: uri = null"
                )
            }
        } else {
            result.error?.printStackTrace()
        }
    }
