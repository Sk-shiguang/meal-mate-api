package com.mealmate.api.meal;

import com.mealmate.api.common.api.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MealControllerTest {

    @Test
    void shouldReturnMealList() {
        MealService service = new InMemoryMealService();
        MealController controller = new MealController(service);

        ApiResponse<MealListResponse> response = controller.list("lunch", "light", 35, null, null);

        assertThat(response.success()).isTrue();
        assertThat(response.data().items()).isNotEmpty();
        assertThat(response.data().total()).isEqualTo(response.data().items().size());
        assertThat(response.data().items().getFirst().name()).isNotBlank();
    }

    @Test
    void shouldReturnMealDetail() {
        MealService service = new InMemoryMealService();
        MealController controller = new MealController(service);

        ApiResponse<MealResponse> response = controller.detail("meal_001");

        assertThat(response.success()).isTrue();
        assertThat(response.data().id()).isEqualTo("meal_001");
        assertThat(response.data().name()).isEqualTo("番茄牛腩饭");
    }
}
