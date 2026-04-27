package com.mealmate.api.feedback;

public interface FeedbackService {
    void submit(String mealId, String type, String reason);
    int count();
}
