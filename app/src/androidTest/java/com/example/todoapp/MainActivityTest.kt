package com.example.todoapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
//    @get : Rule
//    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        //initial setup code
    }

    @Test
    fun clickToAddData() {

//        onView(withId(R.id.home_screen)).perform(click())
    }

    @After
    fun tearDown() {
        //clean up code
    }
}