package com.example.todoapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val activityRule = createAndroidComposeRule(MainActivity::class.java)

//    @Test
//    fun testButtonClick(buttonTag: String) {
//        val button = activityRule.onNode(hasTestTag(buttonTag), useUnmergedTree = true)
//        button.assertIsDisplayed()
//        button.performClick()
//    }
//
//    @Test
//    fun testTodoTextFill(fieldTag: String, inputText: String) {
//        val field = activityRule.onNode(hasTestTag(fieldTag), useUnmergedTree = true)
//        field.assertIsDisplayed()
//        field.performTextInput(inputText)
//    }

    @Test
    fun testTodoCreate() {
        var button = activityRule.onNode(hasTestTag("plusButtonTag"), useUnmergedTree = true)
        button.assertIsDisplayed()
        button.performClick()
        val field = activityRule.onNode(hasTestTag("todoTextTag"), useUnmergedTree = true)
        field.assertIsDisplayed()
        Thread.sleep(1_000)
        field.performTextInput("test todo 00100")
        button = activityRule.onNode(hasTestTag("createButtonTag"), useUnmergedTree = true)
        button.assertIsDisplayed()
        button.assertTextContains("Save")
        button.performClick()
        val todosList = activityRule.onNode(hasTestTag("todosListTag"))
        Thread.sleep(3000)
        todosList.assertIsDisplayed()
        Thread.sleep(3000)
        todosList
            .onChildren()
            .onLast()
            .assert(hasText("test todo 00100"))

    }
}