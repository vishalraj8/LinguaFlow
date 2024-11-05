/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bnyro.translate.util.Preferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextPreference(
    modifier: Modifier = Modifier,
    preferenceKey: String,
    value: String,
    labelText: String,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            Preferences.put(preferenceKey, it)

            onValueChange.invoke(it)
        },
        label = {
            Text(
                text = labelText
            )
        },
        modifier = modifier
            .fillMaxWidth()
    )
}
