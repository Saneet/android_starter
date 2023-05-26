@file:OptIn(FlowPreview::class)

package com.saneet.demo.feature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class FeatureViewModel @Inject constructor() : ViewModel(), Listener {
    private val eventChannel = Channel<Int>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val sharedFlow = eventChannel.consumeAsFlow()
    init {
        viewModelScope.launch(Dispatchers.Main) {
            sharedFlow.sample(5.seconds).collect {
                Log.e("Sampled", it.toString())
            }
        }
    }
    override fun method(value: Int) {
        Log.d("Received", value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            eventChannel.send(value)
        }
    }

}

interface Listener {
    fun method(value: Int)
}