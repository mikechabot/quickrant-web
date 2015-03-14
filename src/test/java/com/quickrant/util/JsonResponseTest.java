package com.quickrant.util;

import org.testng.annotations.Test;

@Test
public class JsonResponseTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testContstructorWithNullStatus() {
        JsonResponse response = new JsonResponse(null, null, null);
    }

}