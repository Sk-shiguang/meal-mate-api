package com.mealmate.api.preference;

import java.util.List;

public record PreferenceResponse(
        List<String> mealTimes,
        List<String> tastes,
        List<String> avoidances,
        Integer budgetMin,
        Integer budgetMax,
        List<String> diningModes,
        List<String> goals,
        String cookingTime
) {
}
