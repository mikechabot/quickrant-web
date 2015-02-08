package com.quickrant.web.models;

import com.quickrant.web.utils.StringUtil;

public class Selection extends Document {

    private String emotion;
    private String question;

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public boolean isValid() {
        return !StringUtil.isEmpty(emotion) && !StringUtil.isEmpty(question);
    }

    @Override
    public String toString() {
        return "Selection{" +
                "emotion='" + emotion + '\'' +
                ", question='" + question + '\'' +
                '}';
    }

}
