/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun RoundIconButton(
    backgroundColor: Color,
    contentDescription: Int,
    iconResourceId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(7.dp, 0.dp)
            .size(60.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(iconResourceId),
            contentDescription = stringResource(
                contentDescription
            ),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}
