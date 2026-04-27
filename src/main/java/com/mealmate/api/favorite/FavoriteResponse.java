package com.mealmate.api.favorite;

public record FavoriteResponse(String id, String mealId, String name, String category, java.util.List<String> tags) {
}
