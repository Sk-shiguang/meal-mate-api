package com.mealmate.api.feedback;

import jakarta.validation.constraints.NotBlank;

public record FeedbackRequest(@NotBlank String mealId, @NotBlank String type, String reason) {
}
