package com.quickrant.web.utils;

import com.quickrant.api.models.Emotion;
import com.quickrant.api.models.Question;
import com.quickrant.api.services.EmotionService;
import com.quickrant.api.services.QuestionService;

public class Helper {

	private static EmotionService emotionSvc = new EmotionService();
	private static QuestionService questionSvc = new QuestionService();

	/* Fetch an emotion given an id */
	public static String getEmotion(String id) {
		Emotion emotion = (Emotion) emotionSvc.fetchById(Integer.parseInt(id));
		return emotion.getEmotion();
	}
	
	/* Fetch a question given an id */
	public static String getQuestion(String id) {
		Question question = (Question) questionSvc.fetchById(Integer.parseInt(id));
		return question.getQuestion();
	}

}
