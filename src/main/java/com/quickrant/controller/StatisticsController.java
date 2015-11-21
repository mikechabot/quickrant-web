package com.quickrant.controller;

import com.quickrant.ajax.AjaxResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;


import com.quickrant.service.StatisticsServiceImpl;

@Controller
@RequestMapping(value = "/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsServiceImpl statisticsService;

    @Autowired
    private AjaxResponseFactory ajaxResponse;

    @RequestMapping(value = "/emotion", method = RequestMethod.GET)
    public ResponseEntity getEmotionStatistics() {
        return ajaxResponse.successWithData(statisticsService.getEmotionStatistics());
    }

    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public ResponseEntity getQuestionStatistics() {
        return ajaxResponse.successWithData(statisticsService.getQuestionStatistics());
    }

}
