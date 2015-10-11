package com.quickrant.domain;

public class EmotionStatistic extends AbstractStatistic {

    private Emotion emotion;

    public EmotionStatistic(String label, Long value) {
        super(label, value);
    }

    public EmotionStatistic(String label, Long value, Emotion emotion) {
        super(label, value);
        this.emotion = emotion;
    }

    public EmotionStatistic(String label, Long value, Emotion emotion, int sortOrder) {
        super(label, value);
        setSortOrder(sortOrder);
        this.emotion = emotion;
    }

    public String getEmotion() {
        return emotion.name().toLowerCase();
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

}
