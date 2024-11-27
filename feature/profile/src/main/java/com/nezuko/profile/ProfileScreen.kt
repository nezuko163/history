package com.nezuko.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.nezuko.domain.model.UserProfile
import com.nezuko.ui.components.ImageFromInet
import com.nezuko.ui.theme.LightBlue
import com.nezuko.ui.theme.Spacing

private const val TAG = "ProfileScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userProfile: UserProfile,
    onArrowBackClick: () -> Unit,
    onProfileIconClick: () -> Unit,
    onGameHistoryButtonClick: () -> Unit,
) {
    Log.i(TAG, "ProfileScreen: recomp")

    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "Profile", modifier = Modifier.align(Alignment.BottomEnd))
    }

    val context = LocalContext.current

    Scaffold(
        containerColor = Color.White,
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                title = {
                    Text(
                        text = "Профиль"
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
                    IconButton(onClick = {
                        Toast.makeText(context, "не нажимай сюда...", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(Spacing.default.large)
            ) {
                ImageFromInet(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f),
                    url = userProfile.photoUrl,
                    errorImageResource = com.nezuko.ui.R.drawable.profile,
                    onClick = { onProfileIconClick() }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(horizontal = Spacing.default.medium),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = userProfile.name, fontWeight = FontWeight.Bold, fontSize = 25.sp)
                    Text(text = "Количество матчей: 25")
                    Text(text = "Процент побед: 83%")
                }
            }

            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                    onClick = onGameHistoryButtonClick
                ) {
                    Text(text = "История игр")
                }
            }

        }
    }
}


@Preview
@Composable
private fun ProfileScreenPreview() {
    val userProfile = UserProfile(name = "гавно")
    ProfileScreen(
        userProfile = userProfile,
        onArrowBackClick = { /*TODO*/ },
        onProfileIconClick = { /*TODO*/ }) {

    }
}