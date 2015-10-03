package com.quickrant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quickrant.factory.AjaxResponseFactory;
import com.quickrant.service.StatisticsServiceImpl;

@Controller
@RequestMapping(value = "/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsServiceImpl statisticsService;

    @Autowired
    private AjaxResponseFactory ajaxResponse;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getRantsSince() {
        return ajaxResponse.success(null, statisticsService.getEmotionStatistics());
    }

}
