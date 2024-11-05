package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.Serializable

@Serializable
data class DeeplWebTranslationRequest(
    val jsonrpc: String,
    val method: String,
    val params: DeeplWebTranslationRequestParams,
    val id: Int
)
