package com.mealmate.api.meal;

import java.util.List;

public class InMemoryMealService implements MealService {
    private final List<MealResponse> meals = List.of(
            new MealResponse(
                    "meal_001",
                    "番茄牛腩饭",
                    "暖胃、有满足感，适合午餐或晚餐。",
                    "rice",
                    List.of("lunch", "dinner"),
                    List.of("savory", "light"),
                    List.of("warm", "comfort"),
                    List.of(),
                    22,
                    35,
                    List.of("takeaway", "dine_in"),
                    "within_30_min",
                    "medium",
                    null,
                    82,
                    "enabled"
            )
    );

    @Override
    public MealListResponse list(String mealTime, String taste, Integer budgetMax, String diningMode, String tag) {
        return new MealListResponse(meals, meals.size());
    }

    @Override
    public MealResponse detail(String id) {
        return meals.stream()
                .filter(meal -> meal.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("菜品不存在"));
    }
}
