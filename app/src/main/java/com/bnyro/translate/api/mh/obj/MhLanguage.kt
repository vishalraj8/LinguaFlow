/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.mh.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MhLanguage(
    @SerialName("Id") val id: String,
    @SerialName("Name") val name: String
)
