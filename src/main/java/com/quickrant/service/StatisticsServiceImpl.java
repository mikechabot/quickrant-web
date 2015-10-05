package com.quickrant.service;

import com.quickrant.beans.StaticData;
import com.quickrant.domain.AbstractStatistic;
import com.quickrant.domain.QuestionStatistic;
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

    @Autowired
    private StaticData staticData;

    @Override
    public List<AbstractStatistic> getEmotionStatistics() {
        List<AbstractStatistic> stats = new ArrayList<>();
        stats.add(new Statistic(Emotion.ANGRY.name().toLowerCase(), rantService.getCountByEmotion(Emotion.ANGRY)));
        stats.add(new Statistic(Emotion.HAPPY.name().toLowerCase(), rantService.getCountByEmotion(Emotion.HAPPY)));
        stats.add(new Statistic(Emotion.SAD.name().toLowerCase(), rantService.getCountByEmotion(Emotion.SAD)));
        return stats;
    }

    @Override
    public List<AbstractStatistic> getQuestionStatistics() {
        List<AbstractStatistic> statistics = new ArrayList<>();
        int sortOrder = 0;
        for (Emotion emotion : Emotion.values()) {
            String[] questions = staticData.getQuestionsByEmotion(emotion);
            for (String question : questions) {
                long count = rantService.getCountByQuestion(question);
                if (count > 0) {
                    statistics.add(new QuestionStatistic(question, count, emotion, sortOrder++));
                }
            }
        }
        return statistics;
    }

}
