package com.mealmate.api.preference;

import com.mealmate.api.common.api.ApiResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PreferenceControllerTest {

    @Test
    void shouldReturnCurrentUserPreference() {
        PreferenceService service = new InMemoryPreferenceService();
        PreferenceController controller = new PreferenceController(service);

        ApiResponse<PreferenceResponse> response = controller.me();

        assertThat(response.success()).isTrue();
        assertThat(response.data().mealTimes()).containsExactly("lunch", "dinner");
        assertThat(response.data().budgetMin()).isEqualTo(15);
        assertThat(response.data().budgetMax()).isEqualTo(35);
    }

    @Test
    void shouldSaveCurrentUserPreference() {
        PreferenceService service = new InMemoryPreferenceService();
        PreferenceController controller = new PreferenceController(service);
        SavePreferenceRequest request = new SavePreferenceRequest(
                List.of("breakfast", "lunch"),
                List.of("light"),
                List.of("seafood"),
                10,
                28,
                List.of("takeaway"),
                List.of("save_money"),
                "within_15_min"
        );

        ApiResponse<PreferenceResponse> response = controller.save(request);

        assertThat(response.success()).isTrue();
        assertThat(response.data().mealTimes()).containsExactly("breakfast", "lunch");
        assertThat(response.data().avoidances()).containsExactly("seafood");
        assertThat(response.data().budgetMax()).isEqualTo(28);
    }
}
