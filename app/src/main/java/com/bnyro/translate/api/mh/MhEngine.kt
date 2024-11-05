/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.api.mh

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MhEngine : TranslationEngine(
    name = "Mozhi",
    defaultUrl = "https://mozhi.aryak.me/",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = "auto",
    supportedEngines = listOf(
        "google",
        "libre",
        "reverso",
        "deepl",
        "duckduckgo",
        "mymemory",
        "watson",
        "yandex"
    ),
    supportsAudio = true
) {
    lateinit var api: Mozhi


    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages(getSelectedEngine())
            .map { Language(it.id, it.name) }
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
            detectedLanguage = response.detectedLanguage
        )
    }

    override suspend fun getAudioFile(lang: String, query: String): File? {
        val audioBytes = api.getAudioFile(lang = lang, text = query).body()?.bytes() ?: return null

        return withContext(Dispatchers.IO) {
            File.createTempFile("audio", ".mp3")
        }.apply {
            writeBytes(audioBytes)
        }
    }
}
