package com.openclassroom.mareu.utils;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;

import com.openclassroom.mareu.R;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class FillAddReunionViewAction implements ViewAction {
    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Fill everything in AddReunionActivity";
    }

    @Override
    public void perform(UiController uiController, View view) {
        onView(withId(R.id.name)).perform(clearText(), typeText("Meeting Name"));
        onView(withId(R.id.add_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 27));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_room)).perform(click());
        onView(withText("Mario")).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(14, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_duration)).perform(click());
        onView(withId(R.id.picker_hour)).perform(new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_participants)).perform(click());
        onView(withId(R.id.add_participants)).perform(clearText(), typeText("p"));
        onView(withText("Patrick")).inRoot(RootMatchers.isPlatformPopup()).perform(click());

    }
}


