package com.mealmate.api.favorite;

import com.mealmate.api.common.api.ApiResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteControllerTest {

    @Test
    void shouldAddFavorite() {
        FavoriteService service = new InMemoryFavoriteService();
        FavoriteController controller = new FavoriteController(service);

        ApiResponse<FavoriteResponse> response = controller.add(new AddFavoriteRequest("meal_001"));

        assertThat(response.success()).isTrue();
        assertThat(response.data().mealId()).isEqualTo("meal_001");
    }

    @Test
    void shouldListFavorites() {
        FavoriteService service = new InMemoryFavoriteService();
        FavoriteController controller = new FavoriteController(service);
        controller.add(new AddFavoriteRequest("meal_001"));

        ApiResponse<FavoriteListResponse> response = controller.list();

        assertThat(response.success()).isTrue();
        assertThat(response.data().items()).hasSize(1);
        assertThat(response.data().total()).isEqualTo(1);
    }

    @Test
    void shouldRemoveFavorite() {
        FavoriteService service = new InMemoryFavoriteService();
        FavoriteController controller = new FavoriteController(service);
        controller.add(new AddFavoriteRequest("meal_001"));

        ApiResponse<Void> response = controller.remove("meal_001");

        assertThat(response.success()).isTrue();
        assertThat(service.list().items()).isEmpty();
    }

    @Test
    void shouldNotAddDuplicateFavorite() {
        FavoriteService service = new InMemoryFavoriteService();
        FavoriteController controller = new FavoriteController(service);
        controller.add(new AddFavoriteRequest("meal_001"));

        assertThatThrownBy(() -> controller.add(new AddFavoriteRequest("meal_001")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("已收藏");
    }
}
