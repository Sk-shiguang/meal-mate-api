package com.mealmate.api.favorite;

public interface FavoriteService {
    FavoriteListResponse list();
    FavoriteResponse add(String mealId);
    void remove(String mealId);
}
