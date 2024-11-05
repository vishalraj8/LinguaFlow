/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ext

fun Long.formatBytes(): String {
    if (this < 1024) return "$this B"
    val z = (63 - java.lang.Long.numberOfLeadingZeros(this)) / 10
    return String.format("%.1f %sB", this.toDouble() / (1L shl z * 10), " KMGTPE"[z])
}