/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.db.obj

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class HistoryItemType {
    HISTORY,
    FAVORITE
}

@Entity
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val sourceLanguageName: String,
    @ColumnInfo val sourceLanguageCode: String,
    @ColumnInfo val targetLanguageName: String,
    @ColumnInfo val targetLanguageCode: String,
    @ColumnInfo val insertedText: String,
    @ColumnInfo val translatedText: String,
    @ColumnInfo(defaultValue = "HISTORY") val itemType: HistoryItemType
)
