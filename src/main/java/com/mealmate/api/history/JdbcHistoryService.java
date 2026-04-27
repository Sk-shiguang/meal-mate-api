package com.mealmate.api.history;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class JdbcHistoryService implements HistoryService {
    private static final String DEMO_USER_ID = "demo-user";

    private final JdbcTemplate jdbcTemplate;

    public JdbcHistoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HistoryListResponse list(String mealTime, Integer limit, Integer offset) {
        StringBuilder sql = new StringBuilder("""
                select h.id, h.meal_id, m.name as meal_name, m.category, h.meal_time, h.eaten_at, h.source, h.rating, h.note
                from meal_histories h
                join meals m on m.id = h.meal_id
                where h.user_id = ?
                """);
        List<Object> params = new ArrayList<>();
        params.add(DEMO_USER_ID);

        if (mealTime != null && !mealTime.isBlank()) {
            sql.append(" and h.meal_time = ?");
            params.add(mealTime);
        }

        int total = jdbcTemplate.queryForObject(
                "select count(*) from (" + sql + ") as cnt",
                Integer.class,
                params.toArray()
        );

        sql.append(" order by h.eaten_at desc");

        int actualLimit = limit == null || limit <= 0 ? 20 : Math.min(limit, 100);
        int actualOffset = offset == null || offset < 0 ? 0 : offset;
        sql.append(" limit ? offset ?");
        params.add(actualLimit);
        params.add(actualOffset);

        List<HistoryItemResponse> items = jdbcTemplate.query(
                sql.toString(),
                this::mapRow,
                params.toArray()
        );

        return new HistoryListResponse(items, total);
    }

    private HistoryItemResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp ts = rs.getTimestamp("eaten_at");
        Instant eatenAt = ts != null ? ts.toInstant() : null;
        return new HistoryItemResponse(
                rs.getString("id"),
                rs.getString("meal_id"),
                rs.getString("meal_name"),
                rs.getString("category"),
                rs.getString("meal_time"),
                eatenAt,
                rs.getString("source"),
                rs.getObject("rating", Integer.class),
                rs.getString("note")
        );
    }
}
