package com.mealmate.api.meal;

import com.mealmate.api.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meals")
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping
    public ApiResponse<MealListResponse> list(
            @RequestParam(required = false) String mealTime,
            @RequestParam(required = false) String taste,
            @RequestParam(required = false) Integer budgetMax,
            @RequestParam(required = false) String diningMode,
            @RequestParam(required = false) String tag
    ) {
        return ApiResponse.ok(mealService.list(mealTime, taste, budgetMax, diningMode, tag));
    }

    @GetMapping("/{id}")
    public ApiResponse<MealResponse> detail(@PathVariable String id) {
        return ApiResponse.ok(mealService.detail(id));
    }
}
