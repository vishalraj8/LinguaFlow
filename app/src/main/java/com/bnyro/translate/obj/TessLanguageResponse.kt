/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.obj

import kotlinx.serialization.Serializable

@Serializable
data class TessLanguageResponse(
    val sha: String,
    val tree: List<TessLanguage>,
    val truncated: Boolean,
    val url: String
)