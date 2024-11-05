/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bnyro.translate.obj.MenuItemData

@Composable
fun TopBarMenu(
    menuItems: List<MenuItemData>
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box {
        // 3 vertical dots icon
        StyledIconButton(
            imageVector = Icons.Default.MoreVert,
            onClick = {
                expanded = true
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            // adding each menu item
            menuItems.forEach { menuItemData ->
                DropDownItem(menuItemData) { newState ->
                    expanded = newState
                }
            }
        }
    }
}
