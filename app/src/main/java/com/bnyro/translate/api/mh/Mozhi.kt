/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.mh

import com.bnyro.translate.api.mh.obj.MhLanguage
import com.bnyro.translate.api.mh.obj.MhTranslationResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Mozhi {
    @GET("api/translate/")
    suspend fun translate(
        @Query("engine") engine: String? = null,
        @Query("from") source: String,
        @Query("to") target: String,
        @Query("text") query: String
    ): MhTranslationResponse

    @GET("api/target_languages/")
    suspend fun getLanguages(
        @Query("engine") engine: String?
    ): List<MhLanguage>

    @GET("api/tts")
    suspend fun getAudioFile(
        @Query("engine") engine: String? = "google",
        @Query("lang") lang: String,
        @Query("text") text: String
    ): Response<ResponseBody>
}
