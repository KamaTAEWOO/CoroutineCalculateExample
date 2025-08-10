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

    private val _inputs = MutableSharedFlow<String>(extraBufferCapacity = 64)

    init {
        // 수신되는 모든 입력을 displayText에 순차적으로 누적
        viewModelScope.launch {
            _inputs.collect { digit ->
                _displayText.update { prev ->
                    // 선행 0 허용 여부에 따라 처리 (여기서는 첫 입력이 0이면 "0" 유지)
                    if (prev.isEmpty()) {
                        if (digit == "0") "0" else digit
                    } else {
                        prev + digit
                    }
                }
            }
        }
    }

    fun inputNumber(number: String) {
        _inputs.tryEmit(number)
    }

    fun inputOperator(operator: String) {

    }

    fun inputDot() {

    }

    fun calculate() {

    }

    fun delete() {

    }
}