package com.quickrant.rave.web;

import com.quickrant.rave.models.Emotion;
import com.quickrant.rave.models.Question;
import com.quickrant.rave.services.EmotionService;
import com.quickrant.rave.services.QuestionService;

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
