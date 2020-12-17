package com.pashin.compass;

import org.junit.Assert;
import org.junit.Test;

public class ExampleUnitTest {
    @Test
    public void roundTest() {
        MainActivity mainActivity = new MainActivity();
        Assert.assertEquals("360", mainActivity.roundDegree(359.98));
        Assert.assertEquals("360", mainActivity.roundDegree(360.49));
        Assert.assertEquals("0", mainActivity.roundDegree(-0.1));
    }

    @Test
    public void insertDBTest() {
        DBActivity dbActivity = new DBActivity();
        dbActivity.openDB();
        dbActivity.insert("Samara", "5");
        Assert.assertEquals(1, dbActivity.count());
        dbActivity.closeDB();
    }

    @Test
    public void clearUITest() {
        setPortOrientation();
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.username)).check(matches(withText("")));
        onView(withId(R.id.password)).check(matches(withText("")));
    }

}