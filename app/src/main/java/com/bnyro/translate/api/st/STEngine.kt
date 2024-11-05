/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.st

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonPrimitive

class STEngine : TranslationEngine(
    name = "SimplyTranslate",
    defaultUrl = "https://simplytranslate.org/",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = "auto",
    supportedEngines = listOf("google", "libre", "reverso", "iciba"),
    supportsAudio = true
) {
    lateinit var api: SimplyTranslate

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages(getSelectedEngine()).map {
            Language(it.key, it.value.jsonPrimitive.content)
        }.sortedBy { it.code }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            engine = getSelectedEngine(),
            source = sourceOrAuto(source),
            query = query,
            target = target
        )
        return Translation(
            translatedText = response.translatedText,
            detectedLanguage = response.sourceLanguage,
            transliterations = listOfNotNull(response.pronunciation?.takeIf { it.isNotBlank() })
        )
    }

    override suspend fun getAudioFile(lang: String, query: String): File? {
        val audioBytes = api.getAudioFile(
            lang = lang,
            text = query,
            engine = getSelectedEngine()
        ).body()?.bytes() ?: return null

        return withContext(Dispatchers.IO) {
            File.createTempFile("audio", ".mp3")
        }.apply {
            writeBytes(audioBytes)
        }
    }
}
