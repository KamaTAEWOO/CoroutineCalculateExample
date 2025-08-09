package com.meronacompany.coroutinecalculateexample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        viewModel = MainViewModel()
        initViews()
        setupClickListeners()

        // Collect display updates from ViewModel
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.display.collect { value ->
                    display.text = value
                }
            }
        }
    }

    private fun initViews() {
        display = findViewById(R.id.display)
        updateDisplay()
    }

    private fun setupClickListeners() {
        // 숫자 버튼들
        findViewById<Button>(R.id.btn0).setOnClickListener { inputNumber("0") }
        findViewById<Button>(R.id.btn1).setOnClickListener { inputNumber("1") }
        findViewById<Button>(R.id.btn2).setOnClickListener { inputNumber("2") }
        findViewById<Button>(R.id.btn3).setOnClickListener { inputNumber("3") }
        findViewById<Button>(R.id.btn4).setOnClickListener { inputNumber("4") }
        findViewById<Button>(R.id.btn5).setOnClickListener { inputNumber("5") }
        findViewById<Button>(R.id.btn6).setOnClickListener { inputNumber("6") }
        findViewById<Button>(R.id.btn7).setOnClickListener { inputNumber("7") }
        findViewById<Button>(R.id.btn8).setOnClickListener { inputNumber("8") }
        findViewById<Button>(R.id.btn9).setOnClickListener { inputNumber("9") }

        // 연산자 버튼들
        findViewById<Button>(R.id.btnPlus).setOnClickListener { inputOperator("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { inputOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { inputOperator("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { inputOperator("÷") }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { inputOperator("%") }

        // 기능 버튼들
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculate() }
        findViewById<Button>(R.id.btnDot).setOnClickListener { inputDot() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { delete() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clear() }
    }

    private fun inputNumber(number: String) {
        lifecycleScope.launch {
            viewModel.inputNumber(number)
        }
    }

    private fun inputOperator(operator: String) {
        lifecycleScope.launch {
            viewModel.inputOperator(operator)
        }
    }

    private fun calculate() {
        lifecycleScope.launch {
            viewModel.calculate()
        }
    }

    private fun inputDot() {
        lifecycleScope.launch {
            if (viewModel.getCurrentDisplay() != "0") {
                viewModel.inputDot()
            }
        }
    }

    private fun delete() {
        lifecycleScope.launch {
            viewModel.delete()
        }
    }

    private fun clear() {
        lifecycleScope.launch {
            viewModel.clear()
        }
    }
    
    private fun updateDisplay() {
        display.text = viewModel.getCurrentDisplay()
    }
}