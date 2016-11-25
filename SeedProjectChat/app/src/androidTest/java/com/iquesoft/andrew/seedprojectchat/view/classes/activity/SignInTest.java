package com.iquesoft.andrew.seedprojectchat.view.classes.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.iquesoft.andrew.seedprojectchat.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignInTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public synchronized void signInTest() {
        ViewInteraction appCompatAutoCompleteTextView = onView(
                withId(R.id.email));
        appCompatAutoCompleteTextView.perform(scrollTo(), click());

        ViewInteraction appCompatAutoCompleteTextView2 = onView(
                withId(R.id.email));
        appCompatAutoCompleteTextView2.perform(scrollTo(), replaceText("AndruxaOsetrov@yandex.ru"), closeSoftKeyboard());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.password_tv));
        appCompatEditText.perform(scrollTo(), replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.sign_in_button), withText("Sign in"),
                        withParent(allOf(withId(R.id.email_login_form),
                                withParent(withId(R.id.login_form))))));
        appCompatButton.perform(scrollTo(), click());
    }
}
