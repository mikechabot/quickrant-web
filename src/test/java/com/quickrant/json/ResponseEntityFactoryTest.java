package com.quickrant.json;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class ResponseEntityFactoryTest {

    @InjectMocks
    private ResponseEntityFactory responseEntityFactory;

    @Mock
    private JsonResponseFactory jsonResponseFactory;

    @Mock
    private HttpHeaders headers;


    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOkWithMessageAndData() throws Exception {

        String message = "This is a message";
        int data = 1;

        JsonResponseFactory.JsonResponse jsonResponse = mock(JsonResponseFactory.JsonResponse.class);
        when(jsonResponse.getMessage()).thenReturn(message);
        when(jsonResponse.getData()).thenReturn(data);
        when(jsonResponse.isSuccess()).thenReturn(true);
        when(jsonResponseFactory.success(message, data)).thenReturn(jsonResponse);

        /* Object under test */
        ResponseEntity response = responseEntityFactory.ok(message, data);

        assertNotNull(response);
        assertEquals(response.getBody(), jsonResponse);
        assertEquals(((JsonResponseFactory.JsonResponse) response.getBody()).getMessage(), message);
        assertEquals(((JsonResponseFactory.JsonResponse) response.getBody()).getData(), data);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void testOkWithNull() throws Exception {

        /* Object under test */
        ResponseEntity response = responseEntityFactory.ok(null);

        assertNotNull(response);
        assertEquals(response.getBody(), null);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testCreated() throws Exception {

        URI uri = URI.create("/url/123");
        String message = "This is a message";
        int data = 1;

        when(headers.getLocation()).thenReturn(uri);

        JsonResponseFactory.JsonResponse jsonResponse = mock(JsonResponseFactory.JsonResponse.class);
        when(jsonResponse.getMessage()).thenReturn(message);
        when(jsonResponse.getData()).thenReturn(data);
        when(jsonResponse.isSuccess()).thenReturn(true);
        when(jsonResponseFactory.success(message, data)).thenReturn(jsonResponse);

        /* Object under test */
        ResponseEntity response = responseEntityFactory.created(message, data, uri);

        assertNotNull(response);
        assertEquals(response.getBody(), jsonResponse);
        assertEquals(((JsonResponseFactory.JsonResponse) response.getBody()).getMessage(), message);
        assertEquals(((JsonResponseFactory.JsonResponse) response.getBody()).getData(), data);
        assertNotNull(response.getHeaders(), null);
        assertEquals(response.getHeaders().getLocation(), uri);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void testBadRequest() throws Exception {

        String message = "This is a bad request";

        JsonResponseFactory.JsonResponse jsonResponse = mock(JsonResponseFactory.JsonResponse.class);
        when(jsonResponse.getMessage()).thenReturn(message);
        when(jsonResponse.isSuccess()).thenReturn(false);
        when(jsonResponseFactory.failure(message, null)).thenReturn(jsonResponse);

        /* Object under test */
        ResponseEntity response = responseEntityFactory.badRequest(message);

        assertNotNull(response);
        assertEquals(((JsonResponseFactory.JsonResponse) response.getBody()).getMessage(), message);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

}