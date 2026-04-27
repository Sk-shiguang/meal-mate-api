package com.mealmate.api.user;

public record UserProfileResponse(
        String id,
        String nickname,
        String avatarUrl,
        String status
) {
}
