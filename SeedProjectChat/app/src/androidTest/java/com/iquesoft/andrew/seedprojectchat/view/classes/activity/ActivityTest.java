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
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public synchronized void activityTest() throws Exception {
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Update")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                withId(R.id.email));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("AndruxaOsetrov@yandex.ru"), closeSoftKeyboard());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.password_tv));
        appCompatEditText.perform(scrollTo(), replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password_tv), withText("1234")));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.sign_in_button), withText("Sign in"),
                        withParent(allOf(withId(R.id.email_login_form),
                                withParent(withId(R.id.login_form))))));
        appCompatButton2.perform(scrollTo(), click());

        wait(5000);

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Friends"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withText("Invite to Friends"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("My Invites"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withText("My Friends"), isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Tools"), isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Friends"), isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab_add_friend),
                        withParent(allOf(withId(R.id.fragment_friends_container),
                                withParent(withId(R.id.pager)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

    }

}
