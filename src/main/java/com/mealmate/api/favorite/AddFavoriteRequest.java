package com.mealmate.api.favorite;

import jakarta.validation.constraints.NotBlank;

public record AddFavoriteRequest(@NotBlank String mealId) {
}
