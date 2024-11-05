package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeeplWebTranslationResult(
    val texts: List<DeeplWebTranslationTranslation> = listOf(),
    val lang: String = ""
)
