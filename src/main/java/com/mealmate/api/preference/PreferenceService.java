package com.mealmate.api.preference;

public interface PreferenceService {
    PreferenceResponse getCurrentUserPreference();

    PreferenceResponse saveCurrentUserPreference(SavePreferenceRequest request);
}
