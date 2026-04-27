package com.mealmate.api.preference;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SavePreferenceRequest(
        @NotNull List<String> mealTimes,
        @NotNull List<String> tastes,
        @NotNull List<String> avoidances,
        @Min(0) Integer budgetMin,
        @Min(0) Integer budgetMax,
        @NotNull List<String> diningModes,
        @NotNull List<String> goals,
        String cookingTime
) {
}
