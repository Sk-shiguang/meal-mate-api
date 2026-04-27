package com.mealmate.api.preference;

import com.mealmate.api.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @GetMapping("/me")
    public ApiResponse<PreferenceResponse> me() {
        return ApiResponse.ok(preferenceService.getCurrentUserPreference());
    }

    @PutMapping("/me")
    public ApiResponse<PreferenceResponse> save(@Valid @RequestBody SavePreferenceRequest request) {
        return ApiResponse.ok(preferenceService.saveCurrentUserPreference(request));
    }
}
