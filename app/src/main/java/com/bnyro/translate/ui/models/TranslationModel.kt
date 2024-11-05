/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.ui.models

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.DatabaseHolder.Companion.Db
import com.bnyro.translate.R
import com.bnyro.translate.const.TranslationEngines
import com.bnyro.translate.db.obj.HistoryItem
import com.bnyro.translate.db.obj.HistoryItemType
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.JsonHelper
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.TessHelper
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString

class TranslationModel : ViewModel() {
    var engine by mutableStateOf(getCurrentEngine())

    var simTranslationEnabled by mutableStateOf(
        Preferences.get(Preferences.simultaneousTranslationKey, false)
    )
    var enabledSimEngines = getEnabledEngines()

    var availableLanguages: List<Language> by mutableStateOf(emptyList())

    var sourceLanguage by mutableStateOf(
        getLanguageByPrefKey(Preferences.sourceLanguage) ?: Language("", "Auto")
    )

    var targetLanguage by mutableStateOf(
        getLanguageByPrefKey(Preferences.targetLanguage) ?: Language("en", "English")
    )

    var insertedText by mutableStateOf("")

    var translation by mutableStateOf(Translation(""))

    var translatedTexts = TranslationEngines.engines
            .associate { it.name to Translation("") }
            .toMutableMap()

    var bookmarkedLanguages by mutableStateOf(listOf<Language>())

    var translating by mutableStateOf(false)

    private var mediaPlayer: MediaPlayer? = null
    private var audioFile: File? = null

    private fun getLanguageByPrefKey(key: String): Language? {
        return runCatching {
            JsonHelper.json.decodeFromString<Language>(Preferences.get(key, ""))
        }.getOrNull()
    }

    fun enqueueTranslation() {
        if (!Preferences.get(Preferences.translateAutomatically, true)) return

        val insertedTextTemp = insertedText
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                if (insertedTextTemp == insertedText) translateNow()
            },
            Preferences.get(
                Preferences.fetchDelay,
                500f
            ).toLong()
        )
    }

    fun translateNow() {
        if (insertedText.isEmpty() || targetLanguage == sourceLanguage) {
            translation = Translation("")
            return
        }
        saveSelectedLanguages()

        translating = true

        translatedTexts = TranslationEngines.engines
            .associate { it.name to Translation("") }
            .toMutableMap()

        CoroutineScope(Dispatchers.IO).launch {
            val translation = try {
                engine.translate(
                    insertedText,
                    sourceLanguage.code,
                    targetLanguage.code
                )
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
                translating = false
                return@launch
            }
            translating = false

            if (insertedText.isNotEmpty()) {
                this@TranslationModel.translation = translation
                translatedTexts[engine.name] = translation
                saveToHistory()
            }
        }

        if (simTranslationEnabled) simTranslation()
    }

    private fun simTranslation() {
        enabledSimEngines.forEach {
            if (it != engine) {
                CoroutineScope(Dispatchers.IO).launch {
                    val translation = try {
                        it.translate(
                            insertedText,
                            sourceLanguage.code,
                            targetLanguage.code
                        )
                    } catch (e: Exception) {
                        return@launch
                    }
                    translatedTexts[it.name] = translation
                }
            }
        }
    }

    private fun saveToHistory() {
        if (!Preferences.get(Preferences.historyEnabledKey, true)) {
            return
        }

        save(HistoryItemType.HISTORY)
    }

    fun saveToFavorites() = save(HistoryItemType.FAVORITE)

    private fun save(itemType: HistoryItemType) {
        val historyItem = HistoryItem(
            sourceLanguageCode = sourceLanguage.code,
            sourceLanguageName = sourceLanguage.name,
            targetLanguageCode = targetLanguage.code,
            targetLanguageName = targetLanguage.name,
            insertedText = insertedText,
            translatedText = translation.translatedText,
            itemType = itemType
        )

        viewModelScope.launch(Dispatchers.IO) {
            // don't create new entry if a similar one exists
            if (Preferences.get(Preferences.skipSimilarHistoryKey, true) && Db.historyDao()
                    .existsSimilar(
                        historyItem.insertedText,
                        historyItem.sourceLanguageCode,
                        historyItem.targetLanguageCode,
                        itemType = itemType
                    )
            ) return@launch

            Db.historyDao().insertAll(historyItem)
        }
    }

    fun clearTranslation() {
        insertedText = ""
        translation = Translation("")
        translating = false
    }

    private fun fetchLanguages(onError: (Exception) -> Unit = {}) {
        viewModelScope.launch {
            val languages = try {
                Log.e("engine", engine.name)
                engine.getLanguages()
            } catch (e: Exception) {
                Log.e("Fetching languages", e.toString())
                onError.invoke(e)
                return@launch
            }
            this@TranslationModel.availableLanguages = languages
            sourceLanguage = replaceLanguageName(sourceLanguage)
            targetLanguage = replaceLanguageName(targetLanguage)
        }
    }

    private fun replaceLanguageName(language: Language): Language {
        return availableLanguages.firstOrNull { it.code == language.code } ?: language
    }

    private fun getCurrentEngine() = TranslationEngines.engines[
            Preferences.get(Preferences.apiTypeKey, 0)
    ]

    private fun getEnabledEngines() = TranslationEngines.engines.filter {
        it.isSimultaneousTranslationEnabled()
    }

    fun refresh(context: Context) {
        val newSelectedEngine = getCurrentEngine()
        if (newSelectedEngine != engine) {
            engine = newSelectedEngine
            enqueueTranslation()
        }

        enabledSimEngines = getEnabledEngines()
        simTranslationEnabled = Preferences.get(Preferences.simultaneousTranslationKey, false)

        fetchLanguages {
            Toast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show()
        }

        fetchBookmarkedLanguages()
    }

    private fun fetchBookmarkedLanguages() = viewModelScope.launch(Dispatchers.IO) {
        bookmarkedLanguages = Db.languageBookmarksDao().getAll()
    }

    fun processImage(context: Context, uri: Uri?) {
        if (!TessHelper.areLanguagesDownloaded(context)) {
            Toast.makeText(context, R.string.init_tess_first, Toast.LENGTH_SHORT).show()
            return
        }
        Thread {
            TessHelper.getText(context, uri)?.let {
                insertedText = it
                translateNow()
            }
        }.start()
    }

    fun saveSelectedLanguages() {
        Preferences.put(
            Preferences.sourceLanguage,
            JsonHelper.json.encodeToString(sourceLanguage)
        )
        Preferences.put(
            Preferences.targetLanguage,
            JsonHelper.json.encodeToString(targetLanguage)
        )
    }

    fun playAudio(languageCode: String, text: String) {
        releaseMediaPlayer()

        viewModelScope.launch {
            audioFile = runCatching {
                withContext(Dispatchers.IO) {
                    engine.getAudioFile(languageCode, text)
                }
            }.getOrNull()
            val audioFile = audioFile ?: return@launch

            mediaPlayer = MediaPlayer().apply {
                setOnCompletionListener {
                    releaseMediaPlayer()
                }

                setDataSource(audioFile.absolutePath)

                runCatching {
                    prepare()
                    start()
                }
            }
        }
    }

    private fun releaseMediaPlayer() {
        audioFile?.delete()
        audioFile = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
