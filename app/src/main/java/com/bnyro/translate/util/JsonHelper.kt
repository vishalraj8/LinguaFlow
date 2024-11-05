/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.util

import kotlinx.serialization.json.Json

object JsonHelper {
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}
