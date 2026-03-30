package com.utp.ioscalculator.calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.utp.ioscalculator.history.HistoryModel
import com.utp.ioscalculator.history.HistoryViewModel

class CalculatorViewModel: ViewModel(){

    private val model = CalculatorModel()
    val result = mutableStateOf("0")
    var contentResult = mutableListOf("0")
    private var isCalculated = false

    val operations = mutableStateOf("")
    fun showContent() {
        result.value = if (contentResult.isNotEmpty()) {
            contentResult.joinToString("").replace(".", ",")
        } else {
            "0"
        }

        if(!isCalculated){
            operations.value = if (contentResult.isNotEmpty()){
                contentResult.joinToString("").replace(".", ",")
            } else{
                ""
            }
        }

    }

    private fun isOperator(s: String): Boolean = s == "÷" || s == "x" || s == "-" || s == "+" || s == "%"

    fun manageClickButtons(valueButton: String) {
        if (valueButton.isEmpty()) return

        when (valueButton) {
            "AC" -> {
                contentResult.clear()
                contentResult.add("0")
                isCalculated = false
            }
            "⁺/₋" -> {
                val lastIndex = contentResult.size - 1
                if (lastIndex >= 0) {
                    val last = contentResult[lastIndex]
                    if (!isOperator(last)) {
                        val currentVal = last.toDoubleOrNull() ?: 0.0
                        if (currentVal != 0.0) {
                            val toggled = if (last.startsWith("-")) last.substring(1) else "-$last"
                            contentResult[lastIndex] = toggled
                        }
                    }
                }
                isCalculated = false
            }
            "%","÷", "x", "-", "+" -> {
                val last = contentResult.lastOrNull()
                if (last != null) {
                    if (isOperator(last)) {
                        contentResult[contentResult.size - 1] = valueButton
                    } else {
                        contentResult.add(valueButton)
                    }
                }
                isCalculated = false
            }
            "=" -> {
                calculateResult()
            }
            "," -> {
                if (isCalculated) {
                    contentResult.clear()
                    contentResult.add("0.")
                    isCalculated = false
                } else {
                    val lastIndex = contentResult.size - 1
                    if (lastIndex >= 0) {
                        val last = contentResult[lastIndex]
                        if (!isOperator(last)) {
                            if (!last.contains(".")) {
                                contentResult[lastIndex] = "$last."
                            }
                        } else {
                            contentResult.add("0.")
                        }
                    } else {
                        contentResult.add("0.")
                    }
                }
            }
            else -> { // Digits
                if (isCalculated) {
                    contentResult.clear()
                    contentResult.add(valueButton)
                    isCalculated = false
                } else {
                    val lastIndex = contentResult.size - 1
                    if (lastIndex < 0 || isOperator(contentResult[lastIndex])) {
                        contentResult.add(valueButton)
                    } else {
                        val last = contentResult[lastIndex]
                        if (last == "0") {
                            contentResult[lastIndex] = valueButton
                        } else {
                            contentResult[lastIndex] = last + valueButton
                        }
                    }
                }
            }
        }
        showContent()
    }

    fun calculateResult() {
        val temporalcontent = contentResult.toMutableList()
        //val lastdigit = temporalcontent.lastOrNull()

        val last = contentResult.lastOrNull()
        val lastDigit = contentResult.size - 1
        //val lastDigit = contentResult.lastIndexOf()
        if (last != null) {
            if (isOperator(last)) {
                temporalcontent.removeAt(lastDigit)
            }
        }

        var i = 0 //maxOf(0, i - 1)
        while (i < temporalcontent.size){

            if (i > 0 && i < temporalcontent.size - 1) {
                val num1 = temporalcontent[i-1]
                val operador = temporalcontent[i]
                val num2 = temporalcontent[i+1]
                var temporalresult: String

                when (operador) {
                    "x" -> {
                        temporalresult = multiply(num1, num2)

                        temporalcontent.removeAt(i + 1)
                        temporalcontent.removeAt(i)
                        temporalcontent.removeAt(i - 1)
                        temporalcontent.add(i - 1, temporalresult)
                        i -= 1

                    }
                    "÷" -> {
                        temporalresult = divide(num1, num2)

                        temporalcontent.removeAt(i + 1)
                        temporalcontent.removeAt(i)
                        temporalcontent.removeAt(i - 1)
                        temporalcontent.add(i - 1, temporalresult)
                        i -= 1
                    }
                    else -> {
                        i++
                    }
                }

            } else {
                i++
            }

        }

        i=0 //maxOf(0, i - 1)
        while(i < temporalcontent.size) {

            if (i > 0 && i < temporalcontent.size - 1) {
                val num1 = temporalcontent[i - 1]
                val operador = temporalcontent[i]
                val num2 = temporalcontent[i + 1]
                var temporalresult: String

                when (operador) {
                    "+" -> {
                        temporalresult = add(num1, num2)

                        temporalcontent.removeAt(i + 1)
                        temporalcontent.removeAt(i)
                        temporalcontent.removeAt(i - 1)
                        temporalcontent.add(i - 1, temporalresult)
                        i -= 1

                    }
                    "-" -> {
                        temporalresult = subtract(num1, num2)

                        temporalcontent.removeAt(i + 1)
                        temporalcontent.removeAt(i)
                        temporalcontent.removeAt(i - 1)
                        temporalcontent.add(i - 1, temporalresult)
                        i -= 1

                    }
                    else -> {
                        i++
                    }
                }
            } else {
                i++
            }
        }

        if (temporalcontent.isNotEmpty()){
            operations.value = contentResult.joinToString("")

            val operationText = contentResult.joinToString(" ")
            val resultText = formatNumber(temporalcontent[0].toDouble())
            val historyEntry = "$operationText = $resultText"
            HistoryModel.addOperation(historyEntry)


            result.value = formatNumber(temporalcontent[0].toDouble())
            contentResult.clear()
            contentResult.add(result.value)


        }

        isCalculated = true

    }

    fun formatNumber(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }
    fun add(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.add(n1, n2).toString()
    }

    fun subtract(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.subtract(n1, n2).toString()
    }

    fun multiply(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return model.multiply(n1, n2).toString()
    }

    fun divide(num1: String?, num2: String?): String {
        val n1 = num1?.toDoubleOrNull() ?: 0.0
        val n2 = num2?.toDoubleOrNull() ?: 0.0
        return if (n2 != 0.0) {
            model.divide(n1, n2).toString()
        } else {
            "Sin definir"
        }
    }

}
