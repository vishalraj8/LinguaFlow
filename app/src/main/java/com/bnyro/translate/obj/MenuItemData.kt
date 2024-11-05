/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.obj

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItemData(
    val text: String,
    val icon: ImageVector,
    val action: () -> Unit
)
