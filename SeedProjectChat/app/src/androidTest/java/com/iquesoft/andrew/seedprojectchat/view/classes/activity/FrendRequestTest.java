package com.iquesoft.andrew.seedprojectchat.view.classes.activity;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.iquesoft.andrew.seedprojectchat.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FrendRequestTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public synchronized void frendRequestTest() throws Exception {

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("Update")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                withId(R.id.email));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("Test@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                withId(R.id.password_tv));
        appCompatEditText4.perform(scrollTo(), replaceText("qwerty"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.sign_in_button), withText("Sign in"),
                        withParent(allOf(withId(R.id.email_login_form),
                                withParent(withId(R.id.login_form))))));
        appCompatButton4.perform(scrollTo(), click());

        wait(3000);

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Friends"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        wait(5000);

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab_add_friend),
                        withParent(allOf(withId(R.id.fragment_friends_container),
                                withParent(withId(R.id.pager)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.edit_text_username), isDisplayed()));
        appCompatEditText6.perform(replaceText("andruxa"), closeSoftKeyboard());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.btn_find_users), withText("Button"),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        appCompatButton6.perform(click());

        wait(3000);

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.recycler_view_find_user), isDisplayed()));
        floatingActionButton2.perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.fab_send_friend_request)));

        pressBack();

        wait(3000);

        ViewInteraction appCompatTextView = onView(
                allOf(withText("My Invites"), isDisplayed()));
        appCompatTextView.perform(click());

        wait(3000);

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.recycler_my_invite)));
        relativeLayout.check(matches(isDisplayed()));

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.recycler_my_invite), isDisplayed()));
        floatingActionButton3.perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.btn_revocation)));

        wait(3000);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction apppCompatt = onView(
                allOf(withId(R.id.title), withText("Logout"), isDisplayed()));
        apppCompatt.perform(click());

        wait(3000);

    }

    private void clickOnRecyclerView(int recyclerViewId, int position) {
        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition(position, click()));
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
