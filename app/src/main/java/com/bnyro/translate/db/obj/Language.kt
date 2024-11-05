/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.db.obj

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Language(
    @PrimaryKey(autoGenerate = false)
    val code: String = "",
    @ColumnInfo val name: String = ""
) {
    override fun equals(other: Any?): Boolean {
        (other as? Language)?.let { otherLang ->
            return otherLang.name.lowercase() == this.name.lowercase() ||
                    otherLang.code.lowercase() == this.code.lowercase()
        }
        return super.equals(other)
    }

    override fun hashCode() = 31 * code.hashCode() + name.hashCode()
}
