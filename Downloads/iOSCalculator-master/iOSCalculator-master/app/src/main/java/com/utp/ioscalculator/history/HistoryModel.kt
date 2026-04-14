package com.utp.ioscalculator.history


class HistoryModel {
    companion object {
        private val history = mutableListOf<String>()

        fun getHistory(): List<String> {
            return history
        }

        fun addOperation(operation: String) {
            history.add(operation)
        }

    }
}
