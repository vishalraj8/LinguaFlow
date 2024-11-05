/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import com.bnyro.translate.api.deepl.DeeplEngine
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.const.TranslationEngines
import com.bnyro.translate.ext.capitalize
import com.bnyro.translate.ui.components.BlockRadioButton
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.components.SelectableItem
import com.bnyro.translate.ui.components.prefs.EditTextPreference
import com.bnyro.translate.ui.components.prefs.PreferenceItem
import com.bnyro.translate.ui.components.prefs.SwitchPreference
import com.bnyro.translate.util.Preferences

@Composable
fun EnginePref() {
    val engines = TranslationEngines.engines

    var selected by remember {
        mutableIntStateOf(Preferences.get(Preferences.apiTypeKey, 0))
    }

    var instanceUrl by remember {
        mutableStateOf(
            engines[selected].getUrl()
        )
    }

    var apiKey by remember {
        mutableStateOf(
            engines[selected].getApiKey()
        )
    }

    BlockRadioButton(
        items = engines.map { it.name },
        selected = selected,
        onSelect = {
            selected = it
            Preferences.put(Preferences.apiTypeKey, selected)
            
            instanceUrl = engines[selected].getUrl()
            apiKey = engines[selected].getApiKey()
            TranslationEngines.updateAll()
        }
    ) {
        engines[selected].let { engine ->
            Spacer(modifier = Modifier.height(5.dp))

            if (engine.urlModifiable) {
                EditTextPreference(
                    preferenceKey = engine.urlPrefKey,
                    value = instanceUrl,
                    labelText = stringResource(R.string.instance)
                ) {
                    instanceUrl = it
                    engine.createOrRecreate()
                }
            }

            if (engine.apiKeyState != ApiKeyState.DISABLED) {
                EditTextPreference(
                    preferenceKey = engine.apiPrefKey,
                    value = apiKey,
                    labelText = stringResource(
                        id = R.string.api_key
                    ) + when (engine.apiKeyState) {
                        ApiKeyState.REQUIRED -> " (${stringResource(R.string.required)})"
                        ApiKeyState.OPTIONAL -> " (${stringResource(R.string.optional)})"
                        else -> ""
                    }
                ) {
                    apiKey = it
                    engine.createOrRecreate()
                }
            }

            when {
                 engine.supportedEngines.isNotEmpty() -> {
                    var showEngineSelDialog by remember {
                        mutableStateOf(false)
                    }

                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )

                    PreferenceItem(
                        title = stringResource(R.string.selected_engine),
                        summary = stringResource(R.string.st_selected_engine)
                    ) {
                        showEngineSelDialog = true
                    }

                    if (showEngineSelDialog) {
                        AlertDialog(
                            onDismissRequest = { showEngineSelDialog = false },
                            confirmButton = {
                                DialogButton(
                                    text = stringResource(R.string.cancel)
                                ) {
                                    showEngineSelDialog = false
                                }
                            },
                            text = {
                                LazyColumn {
                                    items(engine.supportedEngines) { availableEngine ->
                                        SelectableItem(
                                            text = availableEngine.replace("_", " ").capitalize()
                                        ) {
                                            Preferences.put(engine.selEnginePrefKey, availableEngine)
                                            engine.createOrRecreate()
                                            showEngineSelDialog = false
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
                engine is DeeplEngine -> {
                    Spacer(modifier = Modifier.height(5.dp))
                    SwitchPreference(
                        preferenceKey = engine.useFreeApiKey,
                        defaultValue = true,
                        preferenceTitle = stringResource(R.string.use_free_api),
                        preferenceSummary = stringResource(R.string.use_free_api_summary)
                    ) {
                        engine.createOrRecreate()
                    }
                }
            }
        }
    }
}
