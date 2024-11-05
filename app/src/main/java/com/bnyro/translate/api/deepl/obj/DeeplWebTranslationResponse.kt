package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.Serializable

@Serializable
data class DeeplWebTranslationResponse(
    val result: DeeplWebTranslationResult = DeeplWebTranslationResult()
)
