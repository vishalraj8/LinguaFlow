/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.st.obj

import kotlinx.serialization.Serializable

@Serializable
data class STDefinition(
    val abbreviation: List<STAbbreviation> = listOf()
)
