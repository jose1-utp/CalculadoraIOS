package com.utp.ioscalculator.history

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {

    var history = mutableStateListOf<String>()
        private set

    fun loadHistory() {
        history.clear()
        history.addAll(HistoryModel.getHistory())
    }

}