package com.example.bookapp;

import android.content.Intent;

import com.example.bookapp.activities.WelcomeActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;

import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SimpleTests {
    private WelcomeActivity welcomeActivity;

    @Before
    public void setUp() {
        welcomeActivity = Robolectric.buildActivity(WelcomeActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        welcomeActivity.findViewById(R.id.test_button).performClick();

        Intent expectedIntent = new Intent(welcomeActivity, MainActivity.class);
        Intent actual = Shadows.shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}
