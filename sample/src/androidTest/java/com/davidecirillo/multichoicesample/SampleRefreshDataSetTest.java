package com.davidecirillo.multichoicesample;

import android.graphics.drawable.ColorDrawable;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.davidecirillo.multichoicesample.api.BaseMultiChoiceActivityTest;
import com.davidecirillo.multichoicesample.sampleToolbar.SampleToolbarActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SampleRefreshDataSetTest extends BaseMultiChoiceActivityTest {

    public SampleToolbarActivity mActivity;

    @Override
    protected boolean isSelected(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.get_started_relative_layout);

        return !(relativeLayout == null || relativeLayout.getBackground() == null) && ((ColorDrawable) relativeLayout.getBackground()).getColor() == ContextCompat.getColor(mActivity, R.color.colorPrimaryDark);
    }

    @Rule
    public ActivityTestRule<SampleToolbarActivity> mActivityRule = new ActivityTestRule<>(SampleToolbarActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mActivity.setQuantityMode(SampleToolbarActivity.QuantityMode.PLURALS);
            }
        });

        wakeScreen(mActivity);
    }

    @Test
    public void testNotifyAdapterDataSetChangeResetTheItemStateToInactive() throws Exception {
        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()))
                .check(matches(isSelected()));

        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()))
                .check(matches(isSelected()));

        ((MultiChoiceAdapter) mActivity.getMySampleToolbarAdapter()).notifyAdapterDataSetChanged();

        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()))
                .check(matches(isSelected()));

        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()))
                .check(matches(isSelected()));
    }
}
