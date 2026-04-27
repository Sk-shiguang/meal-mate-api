package com.mealmate.api.preference;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPreferenceService implements PreferenceService {
    private PreferenceResponse current = new PreferenceResponse(
            List.of("lunch", "dinner"),
            List.of("light", "savory"),
            List.of(),
            15,
            35,
            List.of("takeaway", "dine_in"),
            List.of("comfort", "save_money"),
            "within_30_min"
    );

    @Override
    public PreferenceResponse getCurrentUserPreference() {
        return current;
    }

    @Override
    public PreferenceResponse saveCurrentUserPreference(SavePreferenceRequest request) {
        current = new PreferenceResponse(
                new ArrayList<>(request.mealTimes()),
                new ArrayList<>(request.tastes()),
                new ArrayList<>(request.avoidances()),
                request.budgetMin(),
                request.budgetMax(),
                new ArrayList<>(request.diningModes()),
                new ArrayList<>(request.goals()),
                request.cookingTime()
        );
        return current;
    }
}
