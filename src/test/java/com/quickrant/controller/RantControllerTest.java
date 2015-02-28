package com.quickrant.controller;

import com.quickrant.json.ResponseEntityFactory;
import com.quickrant.model.Rant;
import com.quickrant.model.Ranter;
import com.quickrant.service.RantService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class RantControllerTest {

    @InjectMocks
    private RantController rantController;

    @Mock
    private RantService rantService;

    @Mock
    private ResponseEntityFactory responseEntityFactory;

    @Mock
    private ResponseEntity responseEntity;

    @Mock
    private Page page;

    @Mock
    private PageRequest pageRequest;

    @Mock
    private List<Rant> rants;

    @Mock
    private Ranter ranter;

    @Mock
    private Rant rant;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPage() throws Exception {

        String name = "test-name";
        String location = "test-location";

        when(ranter.getName()).thenReturn(name);
        when(ranter.getLocation()).thenReturn(location);
        when(rant.getRanter()).thenReturn(ranter);
        when(rants.get(0)).thenReturn(rant);

        when(page.getContent()).thenReturn(rants);
        when(rantService.findAll(any(PageRequest.class))).thenReturn(page);

        when(responseEntity.getBody()).thenReturn(page);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntityFactory.ok(null, page)).thenReturn(responseEntity);

        /* Object under test */
        ResponseEntity response = rantController.getPage(1);

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), page);
        assertEquals(((Rant) page.getContent().get(0)).getRanter(), ranter);
        assertEquals(((Rant) page.getContent().get(0)).getRanter().getName(), name);
    }

    @Test
    public void testSaveSuccess() throws Exception {



    }

}