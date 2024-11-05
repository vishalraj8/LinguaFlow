/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.wm.obj

import kotlinx.serialization.Serializable

@Serializable
data class WmTranslationRequest(
    val text: String
)
