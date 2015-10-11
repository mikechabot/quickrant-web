package com.quickrant.service;

import com.quickrant.domain.AbstractStatistic;

import java.util.List;

public interface StatisticsService {

    public List<AbstractStatistic> getEmotionStatistics();
    public List<AbstractStatistic> getQuestionStatistics();

}
