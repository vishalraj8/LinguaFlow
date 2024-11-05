/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ext

fun <T> concatenate(vararg lists: List<T>): List<T> {
    return listOf(*lists).flatten()
}
