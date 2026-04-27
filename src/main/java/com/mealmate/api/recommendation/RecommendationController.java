package com.mealmate.api.recommendation;

import com.mealmate.api.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/today")
    public ApiResponse<TodayRecommendationResponse> today(@Valid @RequestBody TodayRecommendationRequest request) {
        return ApiResponse.ok(recommendationService.recommendToday(request));
    }
}
