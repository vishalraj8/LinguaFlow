package com.bnyro.translate.api.ap.obj

import kotlinx.serialization.Serializable

@Serializable
data class ApertumResponseData(
    val translatedText: String
)