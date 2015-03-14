package com.quickrant.factory;

import com.quickrant.util.JsonResponse;
import com.quickrant.util.ResponseStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;

import static org.mockito.Mockito.when;

@Test
public class ResponseEntityFactoryTest {

    @InjectMocks
    private ResponseEntityFactory responseEntityFactory;

    @Mock
    private JsonResponse jsonResponse;

    @Mock
    private HttpHeaders headers;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGenerateWithNullStatusesShouldFail() {
        responseEntityFactory.generate(null, null, null, null, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGenerateWithNullResponseStatusShouldFail() {
        responseEntityFactory.generate(HttpStatus.OK, null, null, null, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "getResponseStatuses")
    public void testGenerateWithNullHttpStatusShouldFail(ResponseStatus status) {
        responseEntityFactory.generate(null, status, null, null, null);
    }

    @Test(dataProvider = "getResponseStatuses")
    public void testGenerateOkWithStatusesShouldNotFail(ResponseStatus status) {
        responseEntityFactory.generate(HttpStatus.OK, status, null, null, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGenerateCreatedWithNullHeadersShouldFail() {
        responseEntityFactory.generate(HttpStatus.CREATED, ResponseStatus.SUCCESS, null, null, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGenerateCreatedWithNullHeaderLocationShouldFail() {
        when(headers.getLocation()).thenReturn(null);
        responseEntityFactory.generate(HttpStatus.CREATED, ResponseStatus.SUCCESS, headers, null, null);
    }

    public void testGenerateCreatedWithHeaderLocationAndSuccessfulResponseShouldNotFail() {
        when(headers.getLocation()).thenReturn(URI.create("/foo/bar"));
        responseEntityFactory.generate(HttpStatus.CREATED, ResponseStatus.SUCCESS, headers, null, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "getUnsuccessfulResponseStatuses")
    public void testGenerateCreatedWithoutSuccessfulResponseStatusShouldFail(ResponseStatus status) {
        when(headers.getLocation()).thenReturn(URI.create("/foo/bar"));
        responseEntityFactory.generate(HttpStatus.CREATED, status, headers, null, null);
    }



    @DataProvider
    private static final Object[][] getUnsuccessfulResponseStatuses() {
        return new Object[][] {
                { ResponseStatus.ERROR },
                { ResponseStatus.FAIL }
        };
    }

    @DataProvider
    private static final Object[][] getResponseStatuses() {
        return new Object[][] {
                { ResponseStatus.SUCCESS },
                { ResponseStatus.ERROR },
                { ResponseStatus.FAIL }
        };
    }

}