package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.Serializable

@Serializable
data class DeeplWebTranslationRequestParamsText(
    val text: String
)
