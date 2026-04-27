package com.mealmate.api.recommendation;

import java.util.List;

public record RecommendedMealResponse(
        String id,
        String name,
        String description,
        String category,
        List<String> tags,
        Integer budgetMin,
        Integer budgetMax,
        String reason,
        int score
) {
}
