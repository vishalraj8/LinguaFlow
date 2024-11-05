/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.lv.obj

import kotlinx.serialization.Serializable

@Serializable
data class ExtraTranslation(
    val list: List<ExtraTranslationItem> = listOf(),
    val type: String = ""
)
