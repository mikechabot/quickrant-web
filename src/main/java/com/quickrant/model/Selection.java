package com.quickrant.model;

import org.springframework.beans.factory.annotation.Required;

public class Selection {

    private String emotion;
    private String question;

    public String getEmotion() {
        return emotion;
    }

    @Required
    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getQuestion() {
        return question;
    }

    @Required
    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Selection{" +
                "emotion='" + emotion + '\'' +
                ", question='" + question + '\'' +
                '}';
    }

}
