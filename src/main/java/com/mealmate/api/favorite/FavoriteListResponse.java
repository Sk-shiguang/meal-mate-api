package com.mealmate.api.favorite;

import java.util.List;

public record FavoriteListResponse(List<FavoriteResponse> items, int total) {
}
