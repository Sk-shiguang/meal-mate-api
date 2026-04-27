package com.mealmate.api.recommendation;

import com.mealmate.api.common.api.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecommendationControllerTest {

    @Test
    void shouldReturnTodayRecommendationWithMainMealAndAlternatives() {
        RecommendationService service = new InMemoryRecommendationService();
        RecommendationController controller = new RecommendationController(service);
        TodayRecommendationRequest request = new TodayRecommendationRequest("lunch", "busy", 35, 3);

        ApiResponse<TodayRecommendationResponse> response = controller.today(request);

        assertThat(response.success()).isTrue();
        assertThat(response.data().recommendationId()).isNotBlank();
        assertThat(response.data().main()).isNotNull();
        assertThat(response.data().main().id()).isNotBlank();
        assertThat(response.data().main().reason()).contains("推荐");
        assertThat(response.data().alternatives()).isNotEmpty();
        assertThat(response.data().meta().mealTime()).isEqualTo("lunch");
        assertThat(response.data().meta().scene()).isEqualTo("busy");
        assertThat(response.data().meta().strategy()).isEqualTo("preference_weighted");
    }

    @Test
    void shouldPreferLowerBudgetMealWhenBudgetIsLimited() {
        RecommendationService service = new InMemoryRecommendationService();
        RecommendationController controller = new RecommendationController(service);
        TodayRecommendationRequest request = new TodayRecommendationRequest("lunch", "busy", 20, 3);

        ApiResponse<TodayRecommendationResponse> response = controller.today(request);

        assertThat(response.success()).isTrue();
        assertThat(response.data().main().budgetMin()).isLessThanOrEqualTo(20);
    }
}
