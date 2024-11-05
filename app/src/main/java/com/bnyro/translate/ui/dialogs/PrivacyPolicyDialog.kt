/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.dialogs

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.bnyro.translate.R
import com.bnyro.translate.const.AboutLinks
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PrivacyPolicyDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    var privacyPolicyHtml by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            privacyPolicyHtml = try {
                URL(AboutLinks.PRIVACY_POLICY).readText()
            } catch (e: Exception) {
                e.localizedMessage ?: ""
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(R.string.privacy_policy))
        },
        text = {
            val scrollState = rememberScrollState()
            Text(
                text = privacyPolicyHtml,
                modifier = Modifier
                    .verticalScroll(scrollState),
                minLines = 20
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest.invoke()
                }
            ) {
                Text(stringResource(R.string.okay))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    clipboard.setText(AnnotatedString(text = privacyPolicyHtml))
                    onDismissRequest.invoke()
                }
            ) {
                Text(stringResource(R.string.copy))
            }
        }
    )
}
