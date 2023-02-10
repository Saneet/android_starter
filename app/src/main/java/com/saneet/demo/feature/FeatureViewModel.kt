package com.saneet.demo.feature

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

class FeatureViewModel @Inject constructor() : ViewModel() {
    data class ViewState(
        val imageBitmap: Bitmap? = null,
    )

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()
    fun setImageUri(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            context.contentResolver.let { contentResolver: ContentResolver ->
                val readUriPermission: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, readUriPermission)
                contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
                    _viewState.emit(ViewState(imageBitmap = BitmapFactory.decodeStream(inputStream)))
                }
            }
        }
    }
}

