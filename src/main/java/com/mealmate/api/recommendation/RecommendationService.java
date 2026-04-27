package com.mealmate.api.recommendation;

public interface RecommendationService {
    TodayRecommendationResponse recommendToday(TodayRecommendationRequest request);
}
