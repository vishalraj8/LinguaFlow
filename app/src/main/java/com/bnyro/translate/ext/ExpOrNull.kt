/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ext

inline fun <R> expOrNull(block: () -> R): R? {
    return try {
        block()
    } catch (e: Throwable) {
        null
    }
}
