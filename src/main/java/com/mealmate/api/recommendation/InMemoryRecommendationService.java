package com.mealmate.api.recommendation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class InMemoryRecommendationService implements RecommendationService {
    private final List<RecommendedMealResponse> meals = List.of(
            new RecommendedMealResponse("meal_001", "番茄牛腩饭", "暖胃、有满足感，适合午餐或晚餐。", "rice", List.of("warm", "comfort"), 22, 35, "根据你的偏好推荐，暖胃且预算适中。", 82),
            new RecommendedMealResponse("meal_002", "鸡腿饭", "稳定不出错的饱腹选择。", "rice", List.of("quick", "takeaway_friendly"), 18, 30, "根据你的偏好推荐，快速且饱腹。", 78),
            new RecommendedMealResponse("meal_005", "青菜豆腐汤饭", "清淡低负担，适合想吃舒服点。", "soup", List.of("warm", "light"), 15, 25, "根据你的偏好推荐，清淡低负担。", 74)
    );

    @Override
    public TodayRecommendationResponse recommendToday(TodayRecommendationRequest request) {
        Integer budgetMax = request.budgetMax();
        List<RecommendedMealResponse> ranked = meals.stream()
                .filter(meal -> budgetMax == null || meal.budgetMin() == null || meal.budgetMin() <= budgetMax)
                .sorted(Comparator.comparingInt(RecommendedMealResponse::score).reversed())
                .toList();
        if (ranked.isEmpty()) {
            ranked = new ArrayList<>(meals).stream()
                    .sorted(Comparator.comparingInt(RecommendedMealResponse::score).reversed())
                    .toList();
        }
        RecommendedMealResponse main = ranked.getFirst();
        List<RecommendedMealResponse> alternatives = ranked.stream().skip(1).limit(3).toList();
        return new TodayRecommendationResponse(
                "rec_" + UUID.randomUUID(),
                main,
                alternatives.isEmpty() ? List.of(main) : alternatives,
                new RecommendationMetaResponse(request.mealTime(), request.scene(), "preference_weighted")
        );
    }
}
