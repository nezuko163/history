package com.nezuko.ui.components

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest

private const val TAG = "ImageFromInet"


@Composable
fun ImageFromInet(
    modifier: Modifier = Modifier,
    url: Any,
    errorImageResource: Int,
    contentDescription: String = "",
    crossFade: Boolean = false) {

    Log.i(TAG, "ImageFromInet: url = $url")

    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .crossfade(crossFade)
            .data(url)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier,
        imageLoader = createImageLoader(context).build(),
        error = painterResource(id = errorImageResource),
        onError = {
            Log.i(TAG, "ImageFromInet: ${it.result.throwable}")
            Log.i(TAG, "ImageFromInet: ${it.result.request}")
        },
        onSuccess = {
            Log.i(TAG, "ImageFromFirebaseStorage: ${it.result.dataSource}")
        },
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop
    )
}

fun createImageLoader(
    context: Context
): ImageLoader.Builder {
    return ImageLoader.Builder(context)
        .respectCacheHeaders(enable = false)
}