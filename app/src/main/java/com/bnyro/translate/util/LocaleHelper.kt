/* Copyright (c) 2024 LinguaFlow*/

package com.bnyro.translate.util

import android.content.Context
import android.content.res.Configuration
import com.bnyro.translate.R
import com.bnyro.translate.db.obj.Language
import java.util.*

object LocaleHelper {
    fun updateLanguage(context: Context) {
        val langPref = Preferences.get(Preferences.appLanguageKey, "")
        val locale = when {
            langPref.isEmpty() -> Locale.getDefault()
            langPref.contains("-") -> Locale(
                langPref.substringBefore("-"),
                langPref.substringAfter("r")
            )
            else -> Locale(langPref)
        }
        updateResources(context, locale)
    }

    private fun updateResources(context: Context, locale: Locale) {
        context.resources.apply {
            val config = Configuration(configuration)

            context.createConfigurationContext(configuration)
            Locale.setDefault(locale)
            config.setLocale(locale)

            @Suppress("DEPRECATION")
            updateConfiguration(config, displayMetrics)
        }
    }

    fun getLanguages(context: Context) = listOf(
        Language("en", "English"),
        Language("ar", "Arabic"),
        Language("az", "Azerbaijani"),
        Language("be", "Belarusian"),
        Language("cs", "Czech"),
        Language("de", "German"),
        Language("es", "Spanish"),
        Language("fi", "Finnish"),
        Language("fil", "Filipino"),
        Language("fr-rFR", "French"),
        Language("he", "Hebrew"),
        Language("hi", "Hindi"),
        Language("id", "Indonesian"),
        Language("it", "Italian"),
        Language("ja", "Japanese"),
        Language("ml", "Malayalam"),
        Language("nb-rNO", "Norwegian Bokmål"),
        Language("nn", "Norwegian Nynorsk"),
        Language("or", "Odia"),
        Language("pl", "Polish"),
        Language("pt", "Portuguese"),
        Language("pt-rBR", "Portuguese (Brazil)"),
        Language("ro", "Romanian"),
        Language("ru", "Russian"),
        Language("sat", "Santali"),
        Language("sr", "Serbian"),
        Language("tr", "Turkish"),
        Language("uk", "Ukrainian"),
        Language("vi", "Vietnamese"),
        Language("zh-rCN", "Chinese (Simplified)")
    ).sortedBy { it.name }.toMutableList()
        .apply {
            add(0, Language("", context.getString(R.string.system)))
        }
}
