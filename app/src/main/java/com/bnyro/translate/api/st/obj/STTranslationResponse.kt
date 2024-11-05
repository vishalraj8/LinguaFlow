/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.st.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class STTranslationResponse (
    @SerialName("definitions")
    val definitions: STDefinition? = null,
    @SerialName("pronunciation")
    val pronunciation: String? = null,
    @SerialName("source_language")
    val sourceLanguage: String? = null,
    @SerialName("translated_text")
    val translatedText: String = ""
)
