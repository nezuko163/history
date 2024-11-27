package com.nezuko.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.nezuko.profile.components.imageCropperLauncher
import com.nezuko.profile.components.photoPickerLauncher

private const val TAG = "ProfileRoute"

@Composable
fun ProfileRoute(
    onGameHistoryNavigate: (userId: String) -> Unit,
    modifier: Modifier = Modifier,
    vm: ProfileViewModel = hiltViewModel()
) {
    val user by vm.me.collectAsState()
    val context = LocalContext.current

    val cropLauncher = imageCropperLauncher { croppedImageUri ->
        Log.i(TAG, "cropLauncher: croppedUri - $croppedImageUri")
        vm.setUserAvatar(croppedImageUri)
    }

    val pickerLauncher = photoPickerLauncher { uri: Uri ->
        Log.i(TAG, "ProfileRoute: uri - $uri")
        cropLauncher.launch(
            CropImageContractOptions(
                uri = uri,
                cropImageOptions = CropImageOptions(
                    maxCropResultHeight = 1000,
                    maxCropResultWidth = 1000,
                    guidelines = CropImageView.Guidelines.ON,
                    fixAspectRatio = true,
                    aspectRatioX = 1,
                    aspectRatioY = 1
                )
            )

        )
    }


    ProfileScreen(
        modifier = modifier,
        userProfile = user!!,
        onArrowBackClick = {
            Toast.makeText(context, "не нажимай сюда...", Toast.LENGTH_SHORT).show()
        },
        onProfileIconClick = {
            pickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onGameHistoryButtonClick = { onGameHistoryNavigate(user!!.id) }
    )
}