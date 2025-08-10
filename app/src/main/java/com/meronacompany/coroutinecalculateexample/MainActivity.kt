package com.meronacompany.coroutinecalculateexample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

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
    }

    private fun initViews() {
        display = findViewById(R.id.display)
        // viewModel displayText
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.displayText.collect { value ->
                    display.text = value
                }
            }
        }
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
        Log.d("MainActivity", "Input number: $number")
        lifecycleScope.launch {
            try {
               viewModel.inputNumber(number)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error inputting number: $number", e)
            }
        }
    }

    private fun inputOperator(operator: String) {
        Log.d("MainActivity", "Input operator: $operator")
        lifecycleScope.launch {
            try {
                viewModel.inputOperator(operator)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error inputting operator: $operator", e)
            }
        }
    }

    private fun inputDot() {
    }

    private fun calculate() {
        Log.d("MainActivity", "Calculate result")
        lifecycleScope.launch {
            try {
                viewModel.calculate()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error calculating result", e)
            }
        }
    }

    private fun delete() {
        Log.d("MainActivity", "Delete last character")
        lifecycleScope.launch {
            try {
                viewModel.delete()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error deleting last character", e)
            }
        }
    }

    private fun clear() {
    }
}