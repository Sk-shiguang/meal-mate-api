package com.mealmate.api.user;

import com.mealmate.api.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> me() {
        return ApiResponse.ok(new UserProfileResponse("demo-user", "时光", "", "active"));
    }
}
