package com.mealmate.api.preference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class JdbcPreferenceService implements PreferenceService {
    private static final String DEMO_USER_ID = "demo-user";
    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public JdbcPreferenceService(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public PreferenceResponse getCurrentUserPreference() {
        return jdbcTemplate.queryForObject("""
                select meal_times, tastes, avoidances, budget_min, budget_max, dining_modes, goals, cooking_time
                from user_preferences
                where user_id = ?
                """, this::mapPreference, DEMO_USER_ID);
    }

    @Override
    public PreferenceResponse saveCurrentUserPreference(SavePreferenceRequest request) {
        jdbcTemplate.update("""
                insert into user_preferences (
                    id, user_id, meal_times, tastes, avoidances, budget_min, budget_max, dining_modes, goals, cooking_time, updated_at
                ) values (
                    ?, ?, ?::jsonb, ?::jsonb, ?::jsonb, ?, ?, ?::jsonb, ?::jsonb, ?, now()
                )
                on conflict (user_id) do update set
                    meal_times = excluded.meal_times,
                    tastes = excluded.tastes,
                    avoidances = excluded.avoidances,
                    budget_min = excluded.budget_min,
                    budget_max = excluded.budget_max,
                    dining_modes = excluded.dining_modes,
                    goals = excluded.goals,
                    cooking_time = excluded.cooking_time,
                    updated_at = now()
                """,
                "pref-" + DEMO_USER_ID,
                DEMO_USER_ID,
                toJson(request.mealTimes()),
                toJson(request.tastes()),
                toJson(request.avoidances()),
                request.budgetMin(),
                request.budgetMax(),
                toJson(request.diningModes()),
                toJson(request.goals()),
                request.cookingTime()
        );
        return getCurrentUserPreference();
    }

    private PreferenceResponse mapPreference(ResultSet rs, int rowNum) throws SQLException {
        return new PreferenceResponse(
                fromJson(rs.getString("meal_times")),
                fromJson(rs.getString("tastes")),
                fromJson(rs.getString("avoidances")),
                (Integer) rs.getObject("budget_min"),
                (Integer) rs.getObject("budget_max"),
                fromJson(rs.getString("dining_modes")),
                fromJson(rs.getString("goals")),
                rs.getString("cooking_time")
        );
    }

    private String toJson(List<String> values) {
        try {
            return objectMapper.writeValueAsString(values == null ? List.of() : values);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("列表字段序列化失败", exception);
        }
    }

    private List<String> fromJson(String json) {
        try {
            return objectMapper.readValue(json, STRING_LIST);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("列表字段解析失败", exception);
        }
    }
}
