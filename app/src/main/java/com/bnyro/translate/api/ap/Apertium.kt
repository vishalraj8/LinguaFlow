package com.bnyro.translate.api.ap

import com.bnyro.translate.api.ap.obj.ApertiumResponse
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Apertium {
    @Multipart
    @POST("apy/translate")
    suspend fun translate(
        // language codes, separated by "|"
        @Part("langpair") langPair: String,
        @Part("q") query: String,
        @Part("markUnknown") markUnknown: String = "no",
        @Part("prefs") prefs: String = "",
    ): ApertiumResponse
}