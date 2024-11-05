/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.views

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.obj.TessLanguage
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.components.SearchAppBar
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.dialogs.FullscreenDialog
import com.bnyro.translate.ui.models.TessModel
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.TessHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TessSettings(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val tessModel: TessModel = viewModel()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var query by remember {
        mutableStateOf("")
    }

    var selectedLanguage by remember {
        mutableStateOf(Preferences.get(Preferences.tessLanguageKey, ""))
    }

    LaunchedEffect(Unit) {
        tessModel.init(context)
    }

    var filteredDownloadedLanguages by remember {
        mutableStateOf(emptyList<String>())
    }
    var filteredAvailableLanguages by remember {
        mutableStateOf(emptyList<TessLanguage>())
    }

    LaunchedEffect(query, tessModel.availableLanguages, tessModel.downloadedLanguages) {
        filteredAvailableLanguages = tessModel.availableLanguages.filter { tessLang ->
            tessLang.path.replace(TessHelper.DATA_FILE_SUFFIX, "").contains(query, ignoreCase = true) &&
                    tessModel.downloadedLanguages.none {
                        tessLang.path.replace(TessHelper.DATA_FILE_SUFFIX, "") == it
                    }
        }

        filteredDownloadedLanguages = tessModel.downloadedLanguages.filter { lang ->
            lang.contains(query, ignoreCase = true)
        }
    }

    val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Refresh the downloaded languages every time a download finishes
            tessModel.refreshDownloadedLanguages(context)
            if (tessModel.downloadedLanguages.size == 1) {
                selectedLanguage = tessModel.downloadedLanguages.first()
                Preferences.put(Preferences.tessLanguageKey, selectedLanguage)
            }
        }
    }

    DisposableEffect(Unit) {
        ContextCompat.registerReceiver(
            context,
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )
        onDispose {
            context.unregisterReceiver(onDownloadComplete)
        }
    }

    FullscreenDialog(
        onDismissRequest = onDismissRequest,
        topBar = {
            SearchAppBar(
                title = stringResource(R.string.image_translation),
                value = query,
                onValueChange = { query = it },
                scrollBehavior = topAppBarBehavior,
                navigationIcon = {
                    StyledIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onDismissRequest
                    )
                },
                actions = {}
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .nestedScroll(topAppBarBehavior.nestedScrollConnection)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        SelectionContainer(
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Text(text = stringResource(R.string.tess_summary, TessHelper.tessRepoUrl))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    items(filteredDownloadedLanguages) {
                        TessSettingsRow(
                            packName = "$it${TessHelper.DATA_FILE_SUFFIX}",
                            size = null,
                            selectedLanguage = selectedLanguage,
                            onSelect = { selectedLanguage = it }
                        ) {
                            StyledIconButton(imageVector = Icons.Default.Delete) {
                                if (TessHelper.deleteLanguage(context, it)) {
                                    tessModel.refreshDownloadedLanguages(context)
                                } else {
                                    Toast.makeText(
                                        context,
                                        R.string.unknown_error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }

                    item {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(10.dp)
                                .size(70.dp, 1.dp)
                        )
                    }

                    items(filteredAvailableLanguages) {
                        TessSettingsRow(
                            packName = it.path,
                            size = it.size,
                            selectedLanguage = selectedLanguage
                        ) {
                            var downloading by remember {
                                mutableStateOf(false)
                            }
                            if (!downloading) {
                                StyledIconButton(imageVector = Icons.Default.Download) {
                                    TessHelper.downloadLanguageData(context, it.path)
                                    downloading = true
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        downloading = false
                                    }, 2000)
                                }
                            } else {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .requiredSize(27.dp),
                                    strokeWidth = 3.dp
                                )
                            }
                        }
                    }
                }

                DialogButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    text = stringResource(R.string.okay)
                ) {
                    onDismissRequest.invoke()
                }
            }
        }
    )
}
