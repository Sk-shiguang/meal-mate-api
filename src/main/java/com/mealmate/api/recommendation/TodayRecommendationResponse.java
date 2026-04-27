package com.mealmate.api.recommendation;

import java.util.List;

public record TodayRecommendationResponse(
        String recommendationId,
        RecommendedMealResponse main,
        List<RecommendedMealResponse> alternatives,
        RecommendationMetaResponse meta
) {
}
