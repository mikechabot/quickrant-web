package com.quickrant.json;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class JsonResponseFactoryTest {

    @InjectMocks
    private JsonResponseFactory jsonResponseFactory;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSuccess() throws Exception {
        /* Set up data */
        String message = "This is a success.";
        int data = 1;

        /* Run assertions */
        JsonResponseFactory.JsonResponse response = jsonResponseFactory.success(message, data);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(response.getData(), data);
        assertEquals(response.getMessage(), message);
    }

    @Test
    public void testFailure() throws Exception {
        /* Set up data */
        String message = "This is a failure.";
        int data = 1;

        /* Run assertions */
        JsonResponseFactory.JsonResponse response = jsonResponseFactory.failure(message, data);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(response.getData(), data);
        assertEquals(response.getMessage(), message);
    }
}