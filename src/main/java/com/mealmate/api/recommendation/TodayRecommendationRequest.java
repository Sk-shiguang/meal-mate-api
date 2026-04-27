package com.mealmate.api.recommendation;

import jakarta.validation.constraints.Min;

public record TodayRecommendationRequest(
        String mealTime,
        String scene,
        @Min(0) Integer budgetMax,
        @Min(0) Integer excludeRecentDays
) {
}
