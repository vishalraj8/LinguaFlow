

package com.bnyro.translate.api.deepl.obj

import kotlinx.serialization.Serializable

@Serializable
data class DeeplWebTranslationRequestParamsLangPreference(
    val weight: Map<String, String>
)
