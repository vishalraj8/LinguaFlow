/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bnyro.translate.R
import com.bnyro.translate.const.ThemeMode
import com.bnyro.translate.obj.ListPreferenceOption
import com.bnyro.translate.ui.MainActivity
import com.bnyro.translate.util.Preferences

@Composable
fun ThemeModeDialog(
    onDismiss: () -> Unit
) {
    val activity = LocalContext.current as MainActivity
    ListPreferenceDialog(
        title = stringResource(R.string.select_theme),
        preferenceKey = Preferences.themeModeKey,
        onDismissRequest = {
            onDismiss.invoke()
        },
        options = listOf(
            ListPreferenceOption(
                name = stringResource(R.string.theme_auto),
                value = ThemeMode.AUTO.value,
                isSelected = activity.themeMode == ThemeMode.AUTO
            ),
            ListPreferenceOption(
                name = stringResource(R.string.theme_light),
                value = ThemeMode.LIGHT.value,
                isSelected = activity.themeMode == ThemeMode.LIGHT
            ),
            ListPreferenceOption(
                name = stringResource(R.string.theme_dark),
                value = ThemeMode.DARK.value,
                isSelected = activity.themeMode == ThemeMode.DARK
            ),
            ListPreferenceOption(
                name = stringResource(R.string.theme_black),
                value = ThemeMode.BLACK.value,
                isSelected = activity.themeMode == ThemeMode.BLACK
            )
        ),
        onOptionSelected = {
            activity.themeMode = ThemeMode.values()[it.value]
        }
    )
}
