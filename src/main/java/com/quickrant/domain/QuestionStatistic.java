package com.quickrant.domain;

public class QuestionStatistic extends AbstractStatistic {

    private Emotion emotion;

    public QuestionStatistic(String label, Long value) {
        super(label, value);
    }

    public QuestionStatistic(String label, Long value, Emotion emotion, int sortOrder) {
        super(label, value);
        setSortOrder(sortOrder);
        this.emotion = emotion;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

}
