package com.mealmate.api.feedback;

import com.mealmate.api.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ApiResponse<Void> submit(@Valid @RequestBody FeedbackRequest request) {
        feedbackService.submit(request.mealId(), request.type(), request.reason());
        return ApiResponse.ok(null);
    }
}
