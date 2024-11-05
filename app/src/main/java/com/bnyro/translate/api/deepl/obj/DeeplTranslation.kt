package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeeplTranslation(
    @SerialName("detected_source_language") val detectedSourceLanguage: String = "",
    val text: String = ""
)
