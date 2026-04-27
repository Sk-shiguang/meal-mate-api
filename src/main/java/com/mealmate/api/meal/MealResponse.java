package com.mealmate.api.meal;

import java.util.List;

public record MealResponse(
        String id,
        String name,
        String description,
        String category,
        List<String> mealTimes,
        List<String> tastes,
        List<String> tags,
        List<String> avoidances,
        Integer budgetMin,
        Integer budgetMax,
        List<String> diningModes,
        String cookingTime,
        String caloriesLevel,
        String imageUrl,
        Integer baseScore,
        String status
) {
}
