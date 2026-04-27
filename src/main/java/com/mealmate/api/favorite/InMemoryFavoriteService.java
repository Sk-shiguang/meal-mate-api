package com.mealmate.api.favorite;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryFavoriteService implements FavoriteService {
    private final Map<String, FavoriteResponse> favorites = new LinkedHashMap<>();

    public InMemoryFavoriteService() {
    }

    @Override
    public FavoriteListResponse list() {
        List<FavoriteResponse> items = List.copyOf(favorites.values());
        return new FavoriteListResponse(items, items.size());
    }

    @Override
    public FavoriteResponse add(String mealId) {
        if (favorites.containsKey(mealId)) {
            throw new IllegalArgumentException("已收藏");
        }
        FavoriteResponse fav = new FavoriteResponse(
                "fav_" + mealId,
                mealId,
                mealId,
                "rice",
                List.of()
        );
        favorites.put(mealId, fav);
        return fav;
    }

    @Override
    public void remove(String mealId) {
        favorites.remove(mealId);
    }
}
