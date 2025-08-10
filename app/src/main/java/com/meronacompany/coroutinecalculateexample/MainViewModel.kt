package com.meronacompany.coroutinecalculateexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _displayText = MutableStateFlow("")
    val displayText: StateFlow<String> get() = _displayText

    fun inputNumber(number: String) {
        viewModelScope.launch {
            _displayText.update { currentText ->
                if (currentText.isEmpty() && number == "0") {
                    currentText
                } else {
                    if (currentText == "0") {
                        number
                    } else {
                        currentText.plus(number)
                    }
                }
            }
        }
    }

    fun inputOperator(operator: String) {
        viewModelScope.launch {
            _displayText.update { currentText ->
                if (currentText.isEmpty() || currentText.last().isDigit()) {
                    if (currentText.isNotEmpty() && currentText.last() == operator.first()) {
                        currentText.dropLast(1).plus(operator)
                    } else {
                        currentText.plus(operator)
                    }
                } else {
                    currentText
                }
            }
        }
    }

    fun inputDot() {

    }

    fun calculate() {
        viewModelScope.launch {
            val raw = _displayText.value
            if (raw.isBlank()) return@launch

            val result: Any = try {
                withContext(Dispatchers.Default) {
                    val sanitized = raw.replace('×', '*').replace('÷', '/')
                    val tokens = tokenize(sanitized)
                    if (tokens.isEmpty()) return@withContext "Invalid input"
                    evaluateTokens(tokens.toMutableList())
                }
            } catch (e: Exception) {
                "Error"
            }

            _displayText.value = when (result) {
                is Double -> if (result.isFinite()) {
                    if (result == result.toLong().toDouble()) result.toLong().toString() else result.toString()
                } else {
                    "Error"
                }
                is String -> result
                else -> "Error"
            }
        }
    }

    // Split expression into tokens (numbers/operators) and support unary minus and decimal points
    private fun tokenize(expression: String): List<String> {
        if (expression.isBlank()) return emptyList()
        val tokens = mutableListOf<String>()
        val sb = StringBuilder()
        var lastWasOperator = true // treat leading '-' as unary
        for (ch in expression) {
            when (ch) {
                '+', '-', '*', '/', '%' -> {
                    if (ch == '-' && lastWasOperator) {
                        // unary minus, attach to the upcoming number
                        if (sb.isEmpty()) sb.append('-') else sb.append(ch)
                    } else {
                        if (sb.isNotEmpty()) {
                            tokens.add(sb.toString())
                            sb.clear()
                        }
                        tokens.add(ch.toString())
                        lastWasOperator = true
                        continue
                    }
                }
                ' ' -> { /* ignore stray spaces */ }
                else -> {
                    sb.append(ch)
                }
            }
            lastWasOperator = false
        }
        if (sb.isNotEmpty()) tokens.add(sb.toString())
        return tokens
    }

    // Evaluate tokens with operator precedence: (*, /, %) before (+, -)
    private fun evaluateTokens(tokens: MutableList<String>): Any {
        if (tokens.isEmpty()) return "Invalid input"

        fun applyOp(a: Double, op: String, b: Double): Any {
            return when (op) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> if (b != 0.0) a / b else "Division by zero"
                "%" -> if (b != 0.0) a % b else "Division by zero"
                else -> "Invalid input"
            }
        }

        // First pass: handle *, /, %
        var i = 0
        while (i < tokens.size) {
            val t = tokens[i]
            if (t == "*" || t == "/" || t == "%") {
                if (i - 1 < 0 || i + 1 >= tokens.size) return "Invalid input"
                val left = tokens[i - 1].toDoubleOrNull() ?: return "Invalid input"
                val right = tokens[i + 1].toDoubleOrNull() ?: return "Invalid input"
                val res = applyOp(left, t, right)
                if (res is String) return res
                // replace (left op right) with result
                tokens.removeAt(i + 1)
                tokens.removeAt(i)
                tokens[i - 1] = (res as Double).toString()
                i = maxOf(0, i - 1)
            } else {
                i++
            }
        }

        // Second pass: handle + and - left-to-right
        if (tokens.isEmpty()) return "Invalid input"
        var acc = tokens[0].toDoubleOrNull() ?: return "Invalid input"
        i = 1
        while (i < tokens.size) {
            if (i + 1 >= tokens.size) return "Invalid input"
            val op = tokens[i]
            val next = tokens[i + 1].toDoubleOrNull() ?: return "Invalid input"
            val res = applyOp(acc, op, next)
            if (res is String) return res
            acc = res as Double
            i += 2
        }
        return acc
    }

    fun delete() {
        viewModelScope.launch {
            _displayText.update { currentText ->
                if (currentText.isNotEmpty()) {
                    currentText.dropLast(1)
                } else {
                    currentText
                }
            }
        }
    }
}