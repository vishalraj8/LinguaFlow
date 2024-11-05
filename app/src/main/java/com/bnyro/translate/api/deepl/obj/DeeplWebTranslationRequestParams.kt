package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.Serializable

@Serializable
data class DeeplWebTranslationRequestParams(
    val texts: List<DeeplWebTranslationRequestParamsText>,
    val splitting: String,
    val lang: DeeplWebTranslationRequestParamsLang,
    val commonJobParams: Map<String, String>,
    val timestamp: Long
)
