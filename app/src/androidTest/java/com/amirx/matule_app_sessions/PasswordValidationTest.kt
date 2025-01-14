package com.amirx.matule_app_sessions


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PasswordValidationTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun passwordValidationTest() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.emailET),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.baseContainer),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("hell"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.emailET), withText("hell"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.baseContainer),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.emailET), withText("hell"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.baseContainer),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("hellhellk@gmail.com"))

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.emailET), withText("hellhellk@gmail.com"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.baseContainer),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.loginBt), withText("Войти"),
                childAtPosition(
                    allOf(
                        withId(R.id.baseContainer),
                        childAtPosition(
                            withId(R.id.mainContainer),
                            1
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.tvConfirmMessage), withText("Пароль не может быть пустым"),
                withParent(withParent(IsInstanceOf.instanceOf(androidx.cardview.widget.CardView::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Пароль не может быть пустым")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
