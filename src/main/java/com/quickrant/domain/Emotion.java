package com.quickrant.domain;

public enum Emotion {

    HAPPY("Happy"),
    SAD("Sad"),
    ANGRY("Angry");

    private String emotion;

    private Emotion(String emotion) {
        this.emotion = emotion;
    }

}
