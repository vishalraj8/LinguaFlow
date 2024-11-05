package com.bnyro.translate.api.deepl

import com.bnyro.translate.api.deepl.obj.DeeplLanguage
import com.bnyro.translate.api.deepl.obj.DeeplTranslationResponse
import com.bnyro.translate.api.deepl.obj.DeeplWebTranslationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface DeepL {
    @GET("v2/languages")
    suspend fun getLanguages(
        @Header("Authorization") apiKeyString: String
    ): List<DeeplLanguage>

    @POST("v2/translate")
    suspend fun translate(
        @Header("Authorization") apiKeyString: String,
        @Query("source_lang") source: String,
        @Query("target_lang") target: String,
        @Query("text") query: String
    ): DeeplTranslationResponse

    @Headers(
        "Accept: */*",
        "Accept-Language: en-US,en;q=0.5",
        "Authorization: None",
        "Content-Type: application/json; charset=utf-8",
        "Host: www2.deepl.com",
        "Origin: chrome-extension://cofdbpoegempjloogbagkncekinflcnj",
        "referer: https://www.deepl.com/",
        "Sec-Fetch-Dest: empty",
        "Sec-Fetch-Mode: cors",
        "Sec-Fetch-Site: none",
        "User-Agent: DeepLBrowserExtension/${DeeplEngine.WEB_CHROME_EXTENSION_VER} ${DeeplEngine.WEB_CHROME_USER_AGENT}"
    )
    @POST("jsonrpc?client=chrome-extension,${DeeplEngine.WEB_CHROME_EXTENSION_VER}")
    suspend fun webTranslate(
        @Body body: String
    ): DeeplWebTranslationResponse
}
