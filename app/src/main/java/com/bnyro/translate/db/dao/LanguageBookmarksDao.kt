/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bnyro.translate.db.obj.Language

@Dao
interface LanguageBookmarksDao {
    @Query("SELECT * FROM Language")
    suspend fun getAll(): List<Language>

    @Insert
    suspend fun insertAll(vararg languages: Language)

    @Delete
    suspend fun delete(language: Language)

    @Query("DELETE FROM Language")
    suspend fun deleteAll()
}
