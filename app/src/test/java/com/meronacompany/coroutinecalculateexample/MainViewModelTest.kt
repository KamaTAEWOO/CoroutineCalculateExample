package com.meronacompany.coroutinecalculateexample

import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class MainViewModelTest {
    
    private lateinit var viewModel: MainViewModel
    
    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }
    
    @Test
    fun `초기 디스플레이는 0을 보여준다`() {
        assertEquals("0", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `숫자 입력시 디스플레이에 표시된다`() {
        viewModel.inputNumber("5")
        assertEquals("5", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `여러 숫자 입력시 연결되어 표시된다`() {
        viewModel.inputNumber("1")
        viewModel.inputNumber("2")
        viewModel.inputNumber("3")
        assertEquals("123", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `덧셈 계산이 정확하다`() {
        viewModel.inputNumber("5")
        viewModel.inputOperator("+")
        viewModel.inputNumber("3")
        val result = viewModel.calculate()
        assertEquals(8.0, result, 0.001)
        assertEquals("8", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `뺄셈 계산이 정확하다`() {
        viewModel.inputNumber("10")
        viewModel.inputOperator("-")
        viewModel.inputNumber("4")
        val result = viewModel.calculate()
        assertEquals(6.0, result, 0.001)
        assertEquals("6", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `곱셈 계산이 정확하다`() {
        viewModel.inputNumber("7")
        viewModel.inputOperator("×")
        viewModel.inputNumber("8")
        val result = viewModel.calculate()
        assertEquals(56.0, result, 0.001)
        assertEquals("56", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `나눗셈 계산이 정확하다`() {
        viewModel.inputNumber("15")
        viewModel.inputOperator("÷")
        viewModel.inputNumber("3")
        val result = viewModel.calculate()
        assertEquals(5.0, result, 0.001)
        assertEquals("5", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `소수점 나눗셈 계산이 정확하다`() {
        viewModel.inputNumber("10")
        viewModel.inputOperator("÷")
        viewModel.inputNumber("3")
        val result = viewModel.calculate()
        assertEquals(3.3333333333333335, result, 0.001)
    }
    
    @Test
    fun `0으로 나누기시 무한대를 반환한다`() {
        viewModel.inputNumber("5")
        viewModel.inputOperator("÷")
        viewModel.inputNumber("0")
        val result = viewModel.calculate()
        assertEquals(Double.POSITIVE_INFINITY, result, 0.001)
    }
    
    @Test
    fun `나머지 연산이 정확하다`() {
        viewModel.inputNumber("10")
        viewModel.inputOperator("%")
        viewModel.inputNumber("3")
        val result = viewModel.calculate()
        assertEquals(1.0, result, 0.001)
        assertEquals("1", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `소수점 입력이 정확하다`() {
        viewModel.inputNumber("3")
        viewModel.inputDot()
        viewModel.inputNumber("14")
        assertEquals("3.14", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `중복 소수점은 입력되지 않는다`() {
        viewModel.inputNumber("3")
        viewModel.inputDot()
        viewModel.inputNumber("1")
        viewModel.inputDot() // 이건 무시되어야 함
        viewModel.inputNumber("4")
        assertEquals("3.14", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `빈 상태에서 소수점 입력시 0점으로 시작한다`() {
        viewModel.inputDot()
        assertEquals("0.", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `삭제 기능이 마지막 문자를 제거한다`() {
        viewModel.inputNumber("1")
        viewModel.inputNumber("2")
        viewModel.inputNumber("3")
        viewModel.delete()
        assertEquals("12", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `빈 상태에서 삭제해도 에러가 발생하지 않는다`() {
        viewModel.delete()
        assertEquals("0", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `전체 삭제가 모든 상태를 초기화한다`() {
        viewModel.inputNumber("5")
        viewModel.inputOperator("+")
        viewModel.inputNumber("3")
        viewModel.clear()
        assertEquals("0", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `연속 계산이 가능하다`() {
        // 5 + 3 = 8, 그 다음 × 2 = 16
        viewModel.inputNumber("5")
        viewModel.inputOperator("+")
        viewModel.inputNumber("3")
        viewModel.calculate()
        
        viewModel.inputOperator("×")
        viewModel.inputNumber("2")
        val result = viewModel.calculate()
        
        assertEquals(16.0, result, 0.001)
        assertEquals("16", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `빈 값으로 연산자 입력시 무시된다`() {
        viewModel.inputOperator("+")
        assertEquals("0", viewModel.getCurrentDisplay())
        
        viewModel.inputNumber("5")
        assertEquals("5", viewModel.getCurrentDisplay())
    }
    
    @Test
    fun `불완전한 계산식에서 calculate 호출시 기존 결과를 유지한다`() {
        viewModel.inputNumber("5")
        val result = viewModel.calculate()
        assertEquals(0.0, result, 0.001) // 초기 result 값
    }
}