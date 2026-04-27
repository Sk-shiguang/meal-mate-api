package com.mealmate.api.favorite;

import com.mealmate.api.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ApiResponse<FavoriteListResponse> list() {
        return ApiResponse.ok(favoriteService.list());
    }

    @PostMapping
    public ApiResponse<FavoriteResponse> add(@Valid @RequestBody AddFavoriteRequest request) {
        return ApiResponse.ok(favoriteService.add(request.mealId()));
    }

    @DeleteMapping("/{mealId}")
    public ApiResponse<Void> remove(@PathVariable String mealId) {
        favoriteService.remove(mealId);
        return ApiResponse.ok(null);
    }
}
