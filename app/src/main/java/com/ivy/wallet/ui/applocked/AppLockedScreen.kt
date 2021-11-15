package com.ivy.wallet.ui.applocked

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding
import com.ivy.wallet.R
import com.ivy.wallet.base.hasLockScreen
import com.ivy.wallet.ui.IvyAppPreview
import com.ivy.wallet.ui.Screen
import com.ivy.wallet.ui.theme.*
import com.ivy.wallet.ui.theme.components.IvyButton

@Composable
fun AppLockedScreen(
    screen: Screen.AppLock
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Text(
            modifier = Modifier
                .background(IvyTheme.colors.medium, Shapes.roundedFull)
                .padding(vertical = 12.dp)
                .padding(horizontal = 32.dp),
            text = "APP LOCKED",
            style = Typo.body2.style(
                fontWeight = FontWeight.ExtraBold,
            )
        )

        Spacer(Modifier.weight(1f))

        Image(
            modifier = Modifier
                .size(width = 96.dp, height = 138.dp),
            painter = painterResource(id = R.drawable.ic_fingerprint),
            colorFilter = ColorFilter.tint(IvyTheme.colors.medium),
            contentScale = ContentScale.FillBounds,
            contentDescription = "unlock icon"
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = "Authenticate to enter the app",
            style = Typo.body2.style(
                fontWeight = FontWeight.SemiBold,
                color = Gray
            )
        )

        Spacer(Modifier.height(24.dp))

        val context = LocalContext.current
        IvyButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Unlock",
            textStyle = Typo.body2.style(
                color = White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            wrapContentMode = false
        ) {
            if (hasLockScreen(context)) {
                screen.onShowOSBiometricsModal()
            } else {
                screen.onContinueWithoutAuthentication()
            }
        }
        Spacer(Modifier.height(24.dp))

        //To automatically launch the biometric screen on load of this composable
        LaunchedEffect(true) {
            if (hasLockScreen(context)) {
                screen.onShowOSBiometricsModal()
            } else {
                screen.onContinueWithoutAuthentication()
            }
        }
    }
}

@Preview
@Composable
private fun Preview_Locked() {
    IvyAppPreview {
        AppLockedScreen(
            Screen.AppLock(
                onContinueWithoutAuthentication = {},
                onShowOSBiometricsModal = {}
            )
        )
    }
}