/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api

import com.bnyro.translate.obj.TessLanguageResponse
import com.bnyro.translate.util.TessHelper
import retrofit2.http.GET

interface ExternalApi {
    @GET(TessHelper.tessTreePath)
    suspend fun getAvailableTessLanguages(): TessLanguageResponse
}