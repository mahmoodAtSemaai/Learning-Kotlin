package com.webkul.mobikul.marketplace.odoo;


import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.webkul.mobikul.odoo.activity.SplashScreenActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tools.fastlane.screengrab.Screengrab;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SplashScreenActivityTest2 {

    @Rule
    public ActivityTestRule<SplashScreenActivity> mActivityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);

    @Test
    public void splashScreenActivityTest2() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Screengrab.screenshot("SplashScreen");
        try {
            Thread.sleep(359);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
