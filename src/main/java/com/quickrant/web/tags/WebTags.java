package com.quickrant.web.tags;

import com.quickrant.api.models.Emotion;
import com.quickrant.api.models.Question;

public class WebTags {

	/* Fetch an emotion given an id */
	public static String getEmotion(String id) {
		if (id == null || id.isEmpty()) return "[missing emotion_id]";
		Emotion emotion = Emotion.findById(Integer.parseInt(id));
		return emotion.getEmotion();
	}

	/* Fetch a question given an id */
	public static String getQuestion(String id) {
		if (id == null || id.isEmpty()) return "[missing question_id]";
		Question question = Question.findById(Integer.parseInt(id));
		return question.getQuestion();
	}

}
