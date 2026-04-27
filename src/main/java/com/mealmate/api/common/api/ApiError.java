package com.mealmate.api.common.api;

public record ApiError(
        String code,
        String message
) {
}
