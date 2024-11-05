/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ext

import androidx.compose.ui.graphics.Color

fun String.hexToColor() = Color(android.graphics.Color.parseColor("#$this"))
