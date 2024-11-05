/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BlockRadioButton(
    selected: Int = 0,
    onSelect: (Int) -> Unit,
    items: List<String>,
    content: @Composable () -> Unit
) {
    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            items(items) {
                val index = items.indexOf(it)
                BlockButton(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(4.dp, 4.dp),
                    text = it,
                    selected = selected == index
                ) {
                    onSelect.invoke(index)
                }
            }
        }
        content.invoke()
    }
}
