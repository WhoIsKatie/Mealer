package com.uottawa.seg2105.group10;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.uottawa.seg2105.group10.backend.Cook;
import com.uottawa.seg2105.group10.backend.User;
import com.uottawa.seg2105.group10.ui.Register2;

import org.junit.Rule;
import org.junit.Test;

public class MainTest {




    @Test
    public void testFirstName() {
        User user = new Cook();
        String name = user.getFirstName();
        assertEquals("Your getFirstName method is incorrect", name, "Tess");
    }

    @Test
    public void testLastName() {
        User user = new Cook();
        String name = user.getLastName();
        assertEquals("Your getLastName method is incorrect", name, "Harper");
    }

    @Test
    public void testPassword() {
        User user = new Cook();
        String name = user.getPassword();
        assertEquals("Your getName method is incorrect", name, "pass123!");
    }



    @Rule
    public ActivityScenarioRule<Register2> mActivityTestRule = new
            ActivityScenarioRule<>(Register2.class);
    @Test
    public void nameIsInvalid() {
        onView(withId(R.id.firstNameEditText)).perform(typeText("1234notvalidname1234notvalidname1234notvalidname1234notvalidname1234notvalidname1234notvalidname"), closeSoftKeyboard());
        onView(withId(R.id.lastNameEditText)).perform(typeText("Harper"), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(typeText("cook123@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passEditText)).perform(typeText("pass123!"), closeSoftKeyboard());
        onView(withId(R.id.addressEditText)).perform(typeText("25 goldburn"), closeSoftKeyboard());

        onView(withId(R.id.reg2LoginButt)).perform(click());
        onView(withText("Field must not go over 30 characters")).check(matches(isDisplayed()));
    }

    @Test
    public void lastNameIsInvalid() {
        onView(withId(R.id.firstNameEditText)).perform(typeText("Tess"), closeSoftKeyboard());
        onView(withId(R.id.lastNameEditText)).perform(typeText("123notvalid123notvalid123notvalid123notvalid123notvalid123notvalid123notvalid123notvalid123notvalid123notvalid"), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(typeText("cook123@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passEditText)).perform(typeText("pass123!"), closeSoftKeyboard());
        onView(withId(R.id.addressEditText)).perform(typeText("25 goldburn"), closeSoftKeyboard());

        onView(withId(R.id.reg2LoginButt)).perform(click());
        onView(withText("Field must not go over 30 characters")).check(matches(isDisplayed()));
    }



}




