package com.mealmate.api.history;

import com.mealmate.api.common.api.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HistoryControllerTest {

    @Test
    void shouldReturnHistoryList() {
        InMemoryHistoryService service = new InMemoryHistoryService();
        service.add("meal_001", "lunch");
        HistoryController controller = new HistoryController(service);

        ApiResponse<HistoryListResponse> response = controller.list(null, null, null);

        assertThat(response.success()).isTrue();
        assertThat(response.data().items()).hasSize(1);
        assertThat(response.data().items().getFirst().mealName()).isNotBlank();
        assertThat(response.data().total()).isEqualTo(1);
    }

    @Test
    void shouldFilterByMealTime() {
        InMemoryHistoryService service = new InMemoryHistoryService();
        service.add("meal_001", "lunch");
        service.add("meal_002", "dinner");
        HistoryController controller = new HistoryController(service);

        ApiResponse<HistoryListResponse> response = controller.list("lunch", null, null);

        assertThat(response.data().items()).hasSize(1);
        assertThat(response.data().items().getFirst().mealTime()).isEqualTo("lunch");
    }

    @Test
    void shouldPaginate() {
        InMemoryHistoryService service = new InMemoryHistoryService();
        service.add("meal_001", "lunch");
        service.add("meal_002", "dinner");
        service.add("meal_003", "lunch");
        HistoryController controller = new HistoryController(service);

        ApiResponse<HistoryListResponse> response = controller.list(null, 2, 0);

        assertThat(response.data().items()).hasSize(2);
        assertThat(response.data().total()).isEqualTo(3);
    }

    @Test
    void shouldReturnEmptyForNoData() {
        InMemoryHistoryService service = new InMemoryHistoryService();
        HistoryController controller = new HistoryController(service);

        ApiResponse<HistoryListResponse> response = controller.list(null, null, null);

        assertThat(response.success()).isTrue();
        assertThat(response.data().items()).isEmpty();
        assertThat(response.data().total()).isEqualTo(0);
    }
}
