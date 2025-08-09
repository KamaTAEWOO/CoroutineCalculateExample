package com.meronacompany.coroutinecalculateexample

class MainViewModel {
    
    private var currentNumber = ""
    private var previousNumber = ""
    private var operator = ""
    private var result = 0.0
    
    fun getCurrentDisplay(): String {
        return if (currentNumber.isEmpty()) "0" else currentNumber
    }
    
    fun inputNumber(number: String) {
        currentNumber += number
    }
    
    fun inputOperator(op: String) {
        if (currentNumber.isNotEmpty()) {
            if (previousNumber.isNotEmpty() && operator.isNotEmpty()) {
                calculate()
            }
            previousNumber = currentNumber
            currentNumber = ""
            operator = op
        }
    }
    
    fun calculate(): Double {
        if (previousNumber.isEmpty() || currentNumber.isEmpty() || operator.isEmpty()) {
            return result
        }
        
        val prev = previousNumber.toDoubleOrNull() ?: 0.0
        val current = currentNumber.toDoubleOrNull() ?: 0.0
        
        result = when (operator) {
            "+" -> prev + current
            "-" -> prev - current
            "×" -> prev * current
            "÷" -> if (current != 0.0) prev / current else Double.POSITIVE_INFINITY
            "%" -> prev % current
            else -> current
        }
        
        currentNumber = if (result == result.toInt().toDouble()) {
            result.toInt().toString()
        } else {
            result.toString()
        }
        previousNumber = ""
        operator = ""
        
        return result
    }
    
    fun delete() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1)
        }
    }
    
    fun clear() {
        currentNumber = ""
        previousNumber = ""
        operator = ""
        result = 0.0
    }
    
    fun inputDot() {
        if (!currentNumber.contains(".")) {
            if (currentNumber.isEmpty()) {
                currentNumber = "0."
            } else {
                currentNumber += "."
            }
        }
    }
}