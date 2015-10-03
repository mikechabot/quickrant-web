package com.quickrant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quickrant.domain.Emotion;
import com.quickrant.domain.Statistic;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private RantService rantService;

    @Override
    public List<Statistic> getEmotionStatistics() {
        List<Statistic> stats = new ArrayList<>();
        stats.add(new Statistic(Emotion.ANGRY.name().toLowerCase(), rantService.getCountByEmotion(Emotion.ANGRY)));
        stats.add(new Statistic(Emotion.HAPPY.name().toLowerCase(), rantService.getCountByEmotion(Emotion.HAPPY)));
        stats.add(new Statistic(Emotion.SAD.name().toLowerCase(), rantService.getCountByEmotion(Emotion.SAD)));
        return stats;
    }

}
