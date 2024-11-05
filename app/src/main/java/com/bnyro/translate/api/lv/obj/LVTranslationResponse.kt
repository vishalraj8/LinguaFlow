/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.lv.obj

import kotlinx.serialization.Serializable

@Serializable
data class LVTranslationResponse(
    val info: LVTranslationInfo? = null,
    val translation: String = ""
)
