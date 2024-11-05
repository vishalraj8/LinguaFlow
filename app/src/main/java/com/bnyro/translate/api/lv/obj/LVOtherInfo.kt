/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.lv.obj

import kotlinx.serialization.Serializable

@Serializable
data class LVOtherInfo(
    val definition: String? = null,
    val example: String? = null,
    val synonyms: List<String> = listOf()
)
