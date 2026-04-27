package com.mealmate.api.meal;

import java.util.List;

public record MealListResponse(
        List<MealResponse> items,
        int total
) {
}
