package com.mealmate.api.favorite;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class JdbcFavoriteService implements FavoriteService {
    private static final String DEMO_USER_ID = "demo-user";
    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public JdbcFavoriteService(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public FavoriteListResponse list() {
        List<FavoriteResponse> items = jdbcTemplate.query("""
                select f.id, f.meal_id, m.name, m.category, m.tags
                from favorites f
                join meals m on m.id = f.meal_id
                where f.user_id = ?
                order by f.created_at desc
                """, this::mapRow, DEMO_USER_ID);
        return new FavoriteListResponse(items, items.size());
    }

    @Override
    public FavoriteResponse add(String mealId) {
        List<String> existing = jdbcTemplate.queryForList(
                "select id from favorites where user_id = ? and meal_id = ?",
                String.class, DEMO_USER_ID, mealId);
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException("已收藏");
        }

        jdbcTemplate.queryForList(
                "select id from meals where id = ? and status = 'enabled'",
                String.class, mealId);
        if (jdbcTemplate.queryForList(
                "select id from meals where id = ? and status = 'enabled'",
                String.class, mealId).isEmpty()) {
            throw new IllegalArgumentException("菜品不存在");
        }

        String favId = "fav_" + UUID.randomUUID();
        jdbcTemplate.update(
                "insert into favorites (id, user_id, meal_id) values (?, ?, ?)",
                favId, DEMO_USER_ID, mealId);

        return jdbcTemplate.queryForObject("""
                select f.id, f.meal_id, m.name, m.category, m.tags
                from favorites f
                join meals m on m.id = f.meal_id
                where f.id = ?
                """, this::mapRow, favId);
    }

    @Override
    public void remove(String mealId) {
        jdbcTemplate.update(
                "delete from favorites where user_id = ? and meal_id = ?",
                DEMO_USER_ID, mealId);
    }

    private FavoriteResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FavoriteResponse(
                rs.getString("id"),
                rs.getString("meal_id"),
                rs.getString("name"),
                rs.getString("category"),
                fromJson(rs.getString("tags"))
        );
    }

    private List<String> fromJson(String json) {
        try {
            return objectMapper.readValue(json, STRING_LIST);
        } catch (Exception e) {
            return List.of();
        }
    }
}
