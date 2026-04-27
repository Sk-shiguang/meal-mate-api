package com.mealmate.api.health;

import com.mealmate.api.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<HealthResponse> health() {
        return ApiResponse.ok(new HealthResponse("ok", "meal-mate-api", OffsetDateTime.now()));
    }
}
