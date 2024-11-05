/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.db.obj.HistoryItemType

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryItem WHERE itemType = :type")
    suspend fun getAll(type: HistoryItemType): List<HistoryItem>

    @Query("SELECT EXISTS(SELECT * FROM HistoryItem WHERE insertedText = :insertedText AND sourceLanguageCode = :sourceLanguage AND targetLanguageCode = :targetLanguage AND itemType = :itemType)")
    suspend fun existsSimilar(
        insertedText: String,
        sourceLanguage: String,
        targetLanguage: String,
        itemType: HistoryItemType
    ): Boolean

    @Insert
    suspend fun insertAll(vararg historyItems: HistoryItem)

    @Delete
    suspend fun delete(historyItem: HistoryItem)

    @Query("DELETE FROM HistoryItem WHERE itemType = :type")
    suspend fun deleteAll(type: HistoryItemType)
}
