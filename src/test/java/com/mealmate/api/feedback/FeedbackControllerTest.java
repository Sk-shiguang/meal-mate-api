package com.mealmate.api.feedback;

import com.mealmate.api.common.api.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FeedbackControllerTest {

    @Test
    void shouldSubmitFeedback() {
        FeedbackService service = new InMemoryFeedbackService();
        FeedbackController controller = new FeedbackController(service);

        ApiResponse<Void> response = controller.submit(new FeedbackRequest("meal_001", "like", "好吃"));

        assertThat(response.success()).isTrue();
        assertThat(service.count()).isEqualTo(1);
    }

    @Test
    void shouldRejectInvalidType() {
        FeedbackService service = new InMemoryFeedbackService();
        FeedbackController controller = new FeedbackController(service);

        assertThatThrownBy(() -> controller.submit(new FeedbackRequest("meal_001", "bad", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("反馈类型无效");
    }

    @Test
    void shouldAcceptDislike() {
        FeedbackService service = new InMemoryFeedbackService();
        FeedbackController controller = new FeedbackController(service);

        ApiResponse<Void> response = controller.submit(new FeedbackRequest("meal_001", "dislike", "太咸"));

        assertThat(response.success()).isTrue();
        assertThat(service.count()).isEqualTo(1);
    }
}
