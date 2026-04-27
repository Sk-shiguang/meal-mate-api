package com.mealmate.api.meal;

public interface MealService {
    MealListResponse list(String mealTime, String taste, Integer budgetMax, String diningMode, String tag);

    MealResponse detail(String id);
}
