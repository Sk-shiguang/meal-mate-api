package com.mealmate.api.health;

import com.mealmate.api.common.api.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HealthControllerTest {

    @Test
    void shouldReturnHealthStatus() {
        HealthController controller = new HealthController();

        ApiResponse<HealthResponse> response = controller.health();

        assertThat(response.success()).isTrue();
        assertThat(response.data().status()).isEqualTo("ok");
        assertThat(response.data().service()).isEqualTo("meal-mate-api");
    }
}
