package com.mealmate.api.meal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JdbcMealService implements MealService {
    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public JdbcMealService(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public MealListResponse list(String mealTime, String taste, Integer budgetMax, String diningMode, String tag) {
        StringBuilder sql = new StringBuilder("""
                select id, name, description, category, meal_times, tastes, tags, avoidances,
                       budget_min, budget_max, dining_modes, cooking_time, calories_level, image_url, base_score, status
                from meals
                where status = 'enabled'
                """);
        List<Object> params = new ArrayList<>();
        if (mealTime != null && !mealTime.isBlank()) {
            sql.append(" and meal_times @> ?::jsonb");
            params.add(toJsonArray(mealTime));
        }
        if (taste != null && !taste.isBlank()) {
            sql.append(" and tastes @> ?::jsonb");
            params.add(toJsonArray(taste));
        }
        if (budgetMax != null) {
            sql.append(" and (budget_min is null or budget_min <= ?)");
            params.add(budgetMax);
        }
        if (diningMode != null && !diningMode.isBlank()) {
            sql.append(" and dining_modes @> ?::jsonb");
            params.add(toJsonArray(diningMode));
        }
        if (tag != null && !tag.isBlank()) {
            sql.append(" and tags @> ?::jsonb");
            params.add(toJsonArray(tag));
        }
        sql.append(" order by base_score desc, name asc");

        List<MealResponse> items = jdbcTemplate.query(sql.toString(), this::mapMeal, params.toArray());
        return new MealListResponse(items, items.size());
    }

    @Override
    public MealResponse detail(String id) {
        return jdbcTemplate.queryForObject("""
                select id, name, description, category, meal_times, tastes, tags, avoidances,
                       budget_min, budget_max, dining_modes, cooking_time, calories_level, image_url, base_score, status
                from meals
                where id = ? and status = 'enabled'
                """, this::mapMeal, id);
    }

    private MealResponse mapMeal(ResultSet rs, int rowNum) throws SQLException {
        return new MealResponse(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("category"),
                fromJson(rs.getString("meal_times")),
                fromJson(rs.getString("tastes")),
                fromJson(rs.getString("tags")),
                fromJson(rs.getString("avoidances")),
                (Integer) rs.getObject("budget_min"),
                (Integer) rs.getObject("budget_max"),
                fromJson(rs.getString("dining_modes")),
                rs.getString("cooking_time"),
                rs.getString("calories_level"),
                rs.getString("image_url"),
                rs.getInt("base_score"),
                rs.getString("status")
        );
    }

    private String toJsonArray(String value) {
        try {
            return objectMapper.writeValueAsString(List.of(value));
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("筛选条件序列化失败", exception);
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
