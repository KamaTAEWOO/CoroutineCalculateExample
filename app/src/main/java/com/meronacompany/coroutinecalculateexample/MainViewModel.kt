package com.meronacompany.coroutinecalculateexample

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private var currentNumber = ""
    private var previousNumber = ""
    private var operator = ""
    private var result = 0.0
    private val calcMutex = Mutex()

    private val _display = MutableStateFlow("0")
    val display: StateFlow<String> = _display

    fun getCurrentDisplay(): String {
        return _display.value
    }

    suspend fun inputNumber(number: String) {
        withContext(Dispatchers.Default) {
            calcMutex.withLock {
                currentNumber += number
                _display.value = currentNumber.ifEmpty { "0" }
            }
        }
    }

    suspend fun inputOperator(op: String) {
        withContext(Dispatchers.Default) {
            calcMutex.withLock {
                if (currentNumber.isNotEmpty()) {
                    if (previousNumber.isNotEmpty() && operator.isNotEmpty()) {
                        computeLocked()
                    }
                    previousNumber = currentNumber
                    currentNumber = ""
                    _display.value = "0"
                }
                operator = op
            }
        }
    }

    private fun computeLocked(): Double {
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
        _display.value = currentNumber.ifEmpty { "0" }
        previousNumber = ""
        operator = ""

        return result
    }

    suspend fun calculate(): Double = withContext(Dispatchers.Default) {
        calcMutex.withLock {
            computeLocked()
        }
    }

    suspend fun delete() {
        withContext(Dispatchers.Default) {
            calcMutex.withLock {
                if (currentNumber.isNotEmpty()) {
                    currentNumber = currentNumber.dropLast(1)
                    _display.value = currentNumber.ifEmpty { "0" }
                }
            }
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.Default) {
            calcMutex.withLock {
                currentNumber = ""
                previousNumber = ""
                operator = ""
                result = 0.0
                _display.value = "0"
            }
        }
    }

    suspend fun inputDot() {
        withContext(Dispatchers.Default) {
            calcMutex.withLock {
                if (!currentNumber.contains(".")) {
                    currentNumber = if (currentNumber.isEmpty()) {
                        "0."
                    } else {
                        currentNumber + "."
                    }
                    _display.value = currentNumber
                }
            }
        }
    }
}