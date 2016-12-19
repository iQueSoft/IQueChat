package com.iquesoft.andrew.seedprojectchat.view.classes.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.RecyclerViewMatcher;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CheckTextMessageContent {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public synchronized void loginActivityTest2() throws InterruptedException {

        ViewInteraction appCompatButton1 = onView(
                allOf(withId(android.R.id.button1), withText("Update")));
        appCompatButton1.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.but_facebook), withText("Facebook Login")));
        appCompatButton4.perform(scrollTo(), click());

        wait(10000L);

        pressBack();

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.but_twitter), withText("Twitter  login")));
        appCompatButton3.perform(scrollTo(), click());

        wait(10000L);

        pressBack();

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

        wait(10000);

        clickOnRecyclerView(R.id.main_screen_recycler_view, 0);

        ViewInteraction emojiconEditText = onView(
                allOf(withId(R.id.messageEdit), isDisplayed()));
        emojiconEditText.perform(replaceText("ESP"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.chatSendButton), withText("Send MSG"),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(withId(R.id.container)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        wait(3000);

        scrollRecyclerView(R.id.messagesContainer, 0);

        onView(withRecyclerView(R.id.messagesContainer).atPosition(0))
                .check(matches(hasDescendant(withText("ESP "))));

        wait(3000);

    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private void editText(int viewId, String text) {
        ViewInteraction textInputEditText8 = onView(
                allOf(withId(viewId), isDisplayed()));
        textInputEditText8.perform(replaceText(text), ViewActions.closeSoftKeyboard());
    }

    private void clickOnRecyclerView(int recyclerViewId, int position) {
        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition(position, click()));
    }

    private void scrollRecyclerView(int recyclerViewId, int position) {
        onView(withId(recyclerViewId)).perform(scrollToPosition(position));
    }

    private void checkRecyclerViewHasText(int textViewId, String text) {
        ViewInteraction textView = onView(
                allOf(withId(textViewId), withText(text),
//                        childAtPosition(
//                                childAtPosition(
//                                        IsInstanceOf.instanceOf(android.widget.RelativeLayout.class),
//                                        0),
//                                1),
                        isDisplayed()));
        textView.check(matches(withText(text)));
    }

    private void checkRecyclerViewDisplayed(int recyclerViewId, int containerId) {
        ViewInteraction recyclerView = onView(
                allOf(withId(recyclerViewId),
                        childAtPosition(
                                childAtPosition(
                                        withId(containerId),
                                        0),
                                0),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
    }


    private void cleanField(int editTextId) {
        onView(withId(editTextId)).perform(replaceText(""));
    }

//    private void clickOnViewInDrawer(String textInView) {
//        if (textInView.equals(ConstantsTest.NAVIGATION_MENU_LOG_OUT)) {
//            onView(withId(R.id.nav_view_primary)).perform(swipeUp());
//        }
//        ViewInteraction appCompatCheckedTextView = onView(
//                allOf(withId(R.id.design_menu_item_text), withText(textInView), isDisplayed()));
//        appCompatCheckedTextView.perform(click());
//    }
//
//    private void openNavigationDrawer() {
//        ViewInteraction appCompatImageButton = onView(
//                allOf(withContentDescription("Open navigation drawer"),
//                        withParent(withId(R.id.toolbar_primary)),
//                        isDisplayed()));
//        appCompatImageButton.perform(click());
//    }
//
//    private void checkErrorMessage(int textInputLayoutId, String errorMessage) {
//        onView(withId(textInputLayoutId)).check(matches(hasTextInputLayoutErrorText(errorMessage)));
//    }

    private void clickOnView(int viewId) {
        onView(withId(viewId)).perform(click());
    }

    private void checkDisplayed(int viewId) {
        onView(withId(viewId)).check(matches(isDisplayed()));
    }

    private void checkDisplayed(int viewId, String viewText) {
        ViewInteraction viewInteraction = onView(
                allOf(withId(viewId), withText(viewText)));
        viewInteraction.check(matches(isDisplayed()));
        viewInteraction.check(matches(withText(viewText)));
    }

    private void checkEditText(int viewId, String inputText) throws InterruptedException {
        ViewInteraction viewInteraction = onView(withId(viewId));
        viewInteraction.check(matches(isDisplayed()));
        viewInteraction.perform(replaceText(inputText));
        closeSoftKeyboard();
        viewInteraction.check(matches(withText(inputText)));
        Thread.sleep(300);
    }

//    private Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
//        return new TypeSafeMatcher<View>() {
//
//            @Override
//            public boolean matchesSafely(View view) {
//                if (!(view instanceof TextInputLayout)) {
//                    return false;
//                }
//
//                CharSequence error = ((TextInputLayout) view).getError();
//
//                if (error == null) {
//                    return false;
//                }
//
//                String hint = error.toString();
//
//                return expectedErrorText.equals(hint);
//            }
//
//            @Override
//            public void describeTo(Description description) {
//            }
//        };
//    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {
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
