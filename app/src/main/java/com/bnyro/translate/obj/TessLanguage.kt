/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.obj

import kotlinx.serialization.Serializable

@Serializable
data class TessLanguage(
    val mode: String,
    val path: String,
    val sha: String,
    val size: Long = 0,
    val type: String = "",
    val url: String = ""
)