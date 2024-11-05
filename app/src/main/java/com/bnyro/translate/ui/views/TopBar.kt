package com.bnyro.translate.ui.views

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.bnyro.translate.R
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.ui.components.StyledIconButton
import com.bnyro.translate.ui.components.TopBarMenu
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.util.SpeechHelper
import com.bnyro.translate.util.SpeechResultContract
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    mainModel: TranslationModel,
    menuItems: List<MenuItemData>
) {
    val context = LocalContext.current
    val fileChooser = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        mainModel.processImage(context, it)
    }
    val speechRecognizer = rememberLauncherForActivityResult(SpeechResultContract()) {
        if (it != null) {
            mainModel.insertedText = it
            mainModel.enqueueTranslation()
        }
    }

    TopAppBar(
        title = {
            Text(
                text = "Linguflow"
            )
        },
        actions = {
            AnimatedVisibility(mainModel.insertedText.isEmpty() && SpeechRecognizer.isRecognitionAvailable(context)) {
                StyledIconButton(
                    imageVector = Icons.Default.Mic
                ) {
                    if (
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        SpeechHelper.checkPermission(context as Activity)
                        return@StyledIconButton
                    }

                    try {
                        speechRecognizer.launch(Locale.getDefault())
                    } catch (e: Exception) {
                        Log.e(this::javaClass.name, e.stackTraceToString())
                    }
                }
            }

            AnimatedVisibility(mainModel.insertedText.isEmpty()) {
                StyledIconButton(
                    imageVector = Icons.Default.Image
                ) {
                    val request = PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                    fileChooser.launch(request)
                }
            }

            AnimatedVisibility(mainModel.translation.translatedText.isNotEmpty()) {
                var favoriteIcon by remember {
                    mutableStateOf(Icons.Default.FavoriteBorder)
                }

                LaunchedEffect(mainModel.translation) {
                    favoriteIcon = Icons.Default.FavoriteBorder
                }

                StyledIconButton(
                    imageVector = favoriteIcon,
                    onClick = {
                        favoriteIcon = Icons.Default.Favorite
                        mainModel.saveToFavorites()
                    }
                )
            }

            AnimatedVisibility(mainModel.insertedText.isNotEmpty()) {
                StyledIconButton(
                    imageVector = Icons.Default.Clear,
                    onClick = {
                        mainModel.clearTranslation()
                    }
                )
            }

            TopBarMenu(menuItems)
        }
    )
}
