package com.utp.ioscalculator.calculator

class CalculatorModel {

    fun add(num1: Double?, num2: Double?): Double{
        return (num1 ?: 0.0) + (num2 ?: 0.0)
    }

    fun subtract(num1: Double?, num2: Double?): Double{
        return (num1 ?: 0.0) - (num2 ?: 0.0)
    }

    fun multiply(num1: Double?, num2: Double?): Double{
        return (num1 ?: 0.0) * (num2 ?: 0.0)
    }

    fun divide(num1: Double?, num2: Double?): Double{
        return (num1 ?: 0.0) / (num2 ?: 0.0)
    }

}