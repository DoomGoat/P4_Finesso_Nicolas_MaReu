
package com.openclassroom.mareu.reunion_list;

import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.rule.ActivityTestRule;

import com.openclassroom.mareu.ui.ListReunionActivity;
import com.openclassroom.mareu.di.DI;
import com.openclassroom.mareu.service.ReunionApiService;
import com.openclassroom.mareu.utils.ClickReunionViewAction;
import com.openclassroom.mareu.utils.DeleteViewAction;
import com.openclassroom.mareu.R;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassroom.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * Test class for list of neighbours
 */
public class ReunionsListTest {

    // This is fixed
    private static int ITEMS_COUNT = 10;
    private int positionTest = 1;
    private String locationName = "Mario"; // Can be "Mario", "Luigi", "Peach", "Toad", "Bowser"
    private ReunionApiService service = DI.getNewInstanceApiService();

    @Rule
    public ActivityTestRule<ListReunionActivity> mActivityRule =
            new ActivityTestRule<>(ListReunionActivity.class);

    @Before
    public void setUp() {
        ListReunionActivity activity = mActivityRule.getActivity();
        assertThat(activity, notNullValue());
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void ReunionList_shouldNotBeEmpty() {
        onView(withId(R.id.list_reunion))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void ReunionList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(withId(R.id.list_reunion)).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(withId(R.id.list_reunion))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 9
        onView(withId(R.id.list_reunion)).check(withItemCount(ITEMS_COUNT-1));
    }

    /**
     * When we click "location" filter, only show reunions with filter
     */
    @Test
    public void ReunionList_onLocationFilterClick_shouldFilterList() {
        // When perform a click on the menu -> then the location filter ->
        // then on the location we wanna test -> then OK button
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Filter by location")).perform(click());
        onView(withText(locationName)).perform(click());
        onView(withText("OK")).perform(click());
        // Count the number of meeting with the same location
        int filterCount = 0;
        for (int i = 0; i< service.getReunions().size(); i++ ){
            if (service.getReunions().get(i).getLocation().getRoom().equals(locationName)) filterCount++;
        }
        // Then : their is only the same number of meeting as those who have the location filter
        onView(withId(R.id.list_reunion)).check(withItemCount(filterCount));
    }

    /**
     * When we click "date" filter, only show reunions with filter
     */
    @Test
    public void ReunionList_onDateFilterClick_shouldFilterList() {
        // When perform a click on the menu -> then the date filter ->
        // then set the date we wanna test -> then OK button
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Filter by date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 27));
        onView(withText("OK")).perform(click());
        // Count the number of meeting with the same date
        int filterCount = 0;
        for (int i = 0; i< service.getReunions().size(); i++ ){
            String reunionDate = (String) DateFormat.format("yyyy/MM/dd", service.getReunions().get(i).getBeginTime());
            if (reunionDate.equals("2020/04/27")) filterCount++;
        }
        // Then : their is only the same number of meeting as those who have the date filter
        onView(withId(R.id.list_reunion)).check(withItemCount(filterCount));
    }

    /**
     * When we click "clear" filter, remove all filters
     */
    @Test
    public void ReunionList_onClearFilterClick_shouldFilterList() {
        // When perform a click on the menu -> then the location filter ->
        // then on the location we wanna test -> then OK button
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Filter by location")).perform(click());
        onView(withText(locationName)).perform(click());
        onView(withText("OK")).perform(click());
        // When perform a click on the menu -> then the date filter ->
        // then set the date we wanna test -> then OK button
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Filter by date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 27));
        onView(withText("OK")).perform(click());
        // When perform a click on the menu -> then clear filter
        onView(withId(R.id.menu)).perform(click());
        onView(withText("Clear filter")).perform(click());
        // Then : their is only the same number of meeting as those who have the date filter
        onView(withId(R.id.list_reunion)).check(withItemCount(ITEMS_COUNT));
    }

    /**
     * When we click + icon, the AddReunionActivity is launch
     */
    @Test
    public void ReunionList_onAddButtonClick_shouldOpenAddReunionActivity() {
        // When perform a click on + button
        onView(withId(R.id.add_reunion)).perform(click());
        // Then : the view went to AddReunionActivity
        onView(withId(R.id.add_reunion_layout)).check(matches(isDisplayed()));
    }

    /**
     * In AddReunionActivity, should fill all info before being able to create
     */
    @Test
    public void ReunionList_inAddActivity_shouldFillAllBeforeCreation() {
        // When perform a click on + button
        onView(withId(R.id.add_reunion)).perform(click());
        // Filling everything (actually give error when create button is clicked)
        // onView(withId(R.id.add_reunion_layout)).perform(new FillAddReunionViewAction());
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
        onView(withId(R.id.add_participants)).perform(clearText(), typeText("pat"));
        onView(withText("Patrick")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.add_reunion_layout)).perform(swipeUp());
        onView(withId(R.id.add_button)).perform(click());
        // Then : the button was clickable and there is 1 more meeting in the list
        onView(withId(R.id.list_reunion)).check(withItemCount(ITEMS_COUNT+1));
    }

    /**
     * On button create click, should check if other meeting overlap the one we create
     */
    @Test
    public void ReunionList_onClickCreate_shouldNotOverlapOtherReunion() {
        onView(withId(R.id.add_reunion)).perform(click());
        onView(withId(R.id.name)).perform(clearText(), typeText("Meeting Name"));
        onView(withId(R.id.add_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 27));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_room)).perform(click());
        onView(withText("Bowser")).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_duration)).perform(click());
        onView(withId(R.id.picker_hour)).perform(new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.add_participants)).perform(click());
        onView(withId(R.id.add_participants)).perform(clearText(), typeText("pat"));
        onView(withText("Patrick")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.add_reunion_layout)).perform(swipeUp());
        onView(withId(R.id.add_button)).perform(click());
        // Then : the button was clickable and an error message is display
        onView(withText("Room unavailable")).check(matches(isDisplayed()));
    }

    /**
     * When we click an item, the detailActivity is launch
     */
    @Test
    public void ReunionList_onReunionClick_shouldOpenDetailActivity() {
        // When perform a click on a reunion
        onView(allOf (withId(R.id.list_reunion), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(positionTest, new ClickReunionViewAction()));
        // Then : the view went to DetailActivity
        onView(withId(R.id.detail_reunion)).check(matches(isDisplayed()));
    }

    /**
     * When the detailActivity is launch, the item detail name is shown
     */
    @Test
    public void ReunionList_onDetailOpen_shouldDisplayReunionSubject() {
        // When perform a click on a reunion
        onView(allOf (withId(R.id.list_reunion), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(positionTest, new ClickReunionViewAction()));
        String reunionSubject = service.getReunions().get(positionTest).getName();
        // Then : the subject field is the subject of the reunion
        onView(withId(R.id.detail_subject)).check(matches(withText(reunionSubject)));
    }
}