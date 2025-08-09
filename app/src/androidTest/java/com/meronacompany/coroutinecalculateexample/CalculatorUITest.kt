package com.meronacompany.coroutinecalculateexample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorUITest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun 초기_디스플레이가_0을_표시한다() {
        onView(withId(R.id.display))
            .check(matches(withText("0")))
    }
    
    @Test
    fun 숫자_버튼_클릭시_디스플레이에_표시된다() {
        onView(withId(R.id.btn5)).perform(click())
        onView(withId(R.id.display))
            .check(matches(withText("5")))
    }
    
    @Test
    fun 여러_숫자_버튼_클릭시_연결되어_표시된다() {
        onView(withId(R.id.btn1)).perform(click())
        onView(withId(R.id.btn2)).perform(click())
        onView(withId(R.id.btn3)).perform(click())
        
        onView(withId(R.id.display))
            .check(matches(withText("123")))
    }
    
    @Test
    fun 간단한_덧셈_계산을_수행한다() {
        onView(withId(R.id.btn5)).perform(click())
        onView(withId(R.id.btnPlus)).perform(click())
        onView(withId(R.id.btn3)).perform(click())
        onView(withId(R.id.btnEquals)).perform(click())
        
        onView(withId(R.id.display))
            .check(matches(withText("8")))
    }
    
    @Test
    fun 간단한_뺄셈_계산을_수행한다() {
        onView(withId(R.id.btn1)).perform(click())
        onView(withId(R.id.btn0)).perform(click())
        onView(withId(R.id.btnMinus)).perform(click())
        onView(withId(R.id.btn4)).perform(click())
        onView(withId(R.id.btnEquals)).perform(click())
        
        onView(withId(R.id.display))
            .check(matches(withText("6")))
    }
    
    @Test
    fun 간단한_곱셈_계산을_수행한다() {
        onView(withId(R.id.btn7)).perform(click())
        onView(withId(R.id.btnMultiply)).perform(click())
        onView(withId(R.id.btn8)).perform(click())
        onView(withId(R.id.btnEquals)).perform(click())
        
        onView(withId(R.id.display))
            .check(matches(withText("56")))
    }
    
    @Test
    fun 간단한_나눗셈_계산을_수행한다() {
        onView(withId(R.id.btn1)).perform(click())
        onView(withId(R.id.btn5)).perform(click())
        onView(withId(R.id.btnDivide)).perform(click())
        onView(withId(R.id.btn3)).perform(click())
        onView(withId(R.id.btnEquals)).perform(click())
        
        onView(withId(R.id.display))
            .check(matches(withText("5")))
    }
    
    @Test
    fun 소수점_입력이_작동한다() {
        onView(withId(R.id.btn3)).perform(click())
        onView(withId(R.id.btnDot)).perform(click())
        onView(withId(R.id.btn1)).perform(click())
        onView(withId(R.id.btn4)).perform(click())
        
        onView(withId(R.id.display))
            .check(matches(withText("3.14")))
    }
    
    @Test
    fun 삭제_버튼이_마지막_문자를_제거한다() {
        onView(withId(R.id.btn1)).perform(click())
        onView(withId(R.id.btn2)).perform(click())
        onView(withId(R.id.btn3)).perform(click())
        onView(withId(R.id.btnDelete)).perform(click())
        
        onView(withId(R.id.display))
            .check(matches(withText("12")))
    }
    
    @Test
    fun 클리어_버튼이_모든_입력을_초기화한다() {
        onView(withId(R.id.btn5)).perform(click())
        onView(withId(R.id.btnPlus)).perform(click())
        onView(withId(R.id.btn3)).perform(click())
        onView(withId(R.id.btnClear)).perform(click())
        
        onView(withId(R.id.display))
            .check(matches(withText("0")))
    }
    
    @Test
    fun 복잡한_계산_시나리오_테스트() {
        // (5 + 3) × 2 = 16
        onView(withId(R.id.btn5)).perform(click())
        onView(withId(R.id.btnPlus)).perform(click())
        onView(withId(R.id.btn3)).perform(click())
        onView(withId(R.id.btnEquals)).perform(click())
        
        onView(withId(R.id.btnMultiply)).perform(click())
        onView(withId(R.id.btn2)).perform(click())
        onView(withId(R.id.btnEquals)).perform(click())
        
        onView(withId(R.id.display))
            .check(matches(withText("16")))
    }
    
    @Test
    fun 모든_숫자_버튼이_작동한다() {
        val buttonIds = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        
        buttonIds.forEachIndexed { index, buttonId ->
            onView(withId(R.id.btnClear)).perform(click())
            onView(withId(buttonId)).perform(click())
            onView(withId(R.id.display))
                .check(matches(withText(index.toString())))
        }
    }
    
    @Test
    fun 모든_연산자_버튼이_표시된다() {
        onView(withId(R.id.btnPlus)).check(matches(isDisplayed()))
        onView(withId(R.id.btnMinus)).check(matches(isDisplayed()))
        onView(withId(R.id.btnMultiply)).check(matches(isDisplayed()))
        onView(withId(R.id.btnDivide)).check(matches(isDisplayed()))
        onView(withId(R.id.btnPercent)).check(matches(isDisplayed()))
        onView(withId(R.id.btnEquals)).check(matches(isDisplayed()))
    }
}