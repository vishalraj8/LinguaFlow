package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeeplWebTranslationRequestParamsLang(
    @SerialName("target_lang") val targetLang: String,
    @SerialName("source_lang_user_selected") val sourceLangUserSelected: String,
    val preference: DeeplWebTranslationRequestParamsLangPreference
)
