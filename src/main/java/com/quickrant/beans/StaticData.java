package com.quickrant.beans;

import com.quickrant.domain.Emotion;

import java.util.HashMap;
import java.util.Map;

public class StaticData {

    private Map<Emotion, String[]> questionsByEmotion = new HashMap<>();

    private String[] happy  = new String[] {
        "You know what I love?",
        "You know what I can\"t live without?",
        "You know what\"s cool?",
        "You know what makes me laugh?",
        "You know what I\"m thankful for?",
        "You know what makes me smile?",
        "You know what inspires me?",
        "You know what makes me happy?",
        "You know what I like?",
        "You know what I wish for?",
        "You know what\"s pretty good?",
        "You know what I like to think about?"
    };
    
    private String[] angry = new String[] {
        "You know what I hate?",
        "You know what makes me angry?",
        "You know what\"s bullshit?",
        "You know what sucks?",
        "You know what I don\"t like?",
        "You know what annoys me?",
        "You know what infuriates me?",
        "You know what I can\"t stand?",
        "You know what pisses me off?",
        "You know what\"s unjust?"
    };
    
    private String[] sad = new String[] {
        "You know what makes me cry?",
        "You know what\"s depressing?",
        "You know what makes me sad?",
        "You know what I wish had happened?",
        "You know what sucks?",
        "You know what I don\"t like thinking about?",
        "You know what I miss?",
        "You know what I regret?",
        "You know what scares me the most?",
        "You know what\"s the worst?"
    };

    public Map<Emotion, String[]> getQuestionsByEmotion() {
        return questionsByEmotion;
    }
    public String[] getQuestionsByEmotion(Emotion emotion) {
        if (questionsByEmotion == null || questionsByEmotion.isEmpty()) {
            questionsByEmotion.put(Emotion.ANGRY, getAngry());
            questionsByEmotion.put(Emotion.HAPPY, getHappy());
            questionsByEmotion.put(Emotion.SAD, getSad());
        }
        return questionsByEmotion.get(emotion);
    }
    
    public String[] getHappy() {
        return happy;
    }

    public String[] getAngry() {
        return angry;
    }

    public String[] getSad() {
        return sad;
    }
}
