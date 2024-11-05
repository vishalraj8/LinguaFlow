package com.bnyro.translate.api.ap.obj

import kotlinx.serialization.Serializable

@Serializable
data class ApertiumResponse(
    val responseData: ApertumResponseData,
    val responseStatus: Int
)