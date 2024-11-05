/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.lv.obj

import kotlinx.serialization.Serializable

@Serializable
data class LVAudioResponse(
    val audio: List<Int>
) {
    fun toByteArray() = this.audio.map { it.toByte() }.toByteArray()
}
