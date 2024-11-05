/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bnyro.translate.R
import com.bnyro.translate.db.obj.HistoryItemType
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.components.SearchAppBar
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.models.HistoryModel
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.views.HistoryRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    translationModel: TranslationModel,
    itemType: HistoryItemType,
    @StringRes titleId: Int,
    @StringRes clearItemsHintId: Int
) {
    val viewModel: HistoryModel = viewModel()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    var showDeleteHistoryDialog by remember {
        mutableStateOf(false)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        viewModel.fetchItems(itemType)
    }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchAppBar(
                title = stringResource(id = titleId),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                navigationIcon = {
                    StyledIconButton(
                        imageVector = Icons.Default.ArrowBack
                    ) {
                        navController.popBackStack()
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showDeleteHistoryDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = stringResource(id = clearItemsHintId)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { pV ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pV)
            ) {
                val query = searchQuery.lowercase()
                val filteredItems = viewModel.items.filter {
                    it.insertedText.lowercase().contains(query) ||
                        it.translatedText.lowercase().contains(query)
                }

                if (filteredItems.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredItems, key = { item -> item.id }) {
                            HistoryRow(navController, translationModel, it) {
                                viewModel.deleteItem(it)
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(100.dp),
                            imageVector = Icons.Default.History,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = stringResource(R.string.nothing_here),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    )

    if (showDeleteHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteHistoryDialog = false },
            title = {
                Text(stringResource(clearItemsHintId))
            },
            text = {
                Text(stringResource(R.string.irreversible))
            },
            dismissButton = {
                DialogButton(text = stringResource(R.string.cancel)) {
                    showDeleteHistoryDialog = false
                }
            },
            confirmButton = {
                DialogButton(text = stringResource(R.string.okay)) {
                    viewModel.clearItems(itemType)
                    showDeleteHistoryDialog = false
                }
            }
        )
    }
}
