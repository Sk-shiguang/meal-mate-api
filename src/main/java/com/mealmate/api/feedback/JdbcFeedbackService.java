package com.mealmate.api.feedback;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class JdbcFeedbackService implements FeedbackService {
    private static final String DEMO_USER_ID = "demo-user";
    private static final Set<String> VALID_TYPES = Set.of("like", "dislike");

    private final JdbcTemplate jdbcTemplate;

    public JdbcFeedbackService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void submit(String mealId, String type, String reason) {
        if (!VALID_TYPES.contains(type)) {
            throw new IllegalArgumentException("反馈类型无效，仅支持 like / dislike");
        }
        jdbcTemplate.queryForList(
                "select id from meals where id = ? and status = 'enabled'",
                String.class, mealId);
        if (jdbcTemplate.queryForList(
                "select id from meals where id = ? and status = 'enabled'",
                String.class, mealId).isEmpty()) {
            throw new IllegalArgumentException("菜品不存在");
        }
        jdbcTemplate.update(
                "insert into meal_feedback (id, user_id, meal_id, type, reason) values (?, ?, ?, ?, ?)",
                "fb_" + UUID.randomUUID(),
                DEMO_USER_ID,
                mealId,
                type,
                reason
        );
    }

    @Override
    public int count() {
        Integer cnt = jdbcTemplate.queryForObject(
                "select count(*) from meal_feedback where user_id = ?",
                Integer.class,
                DEMO_USER_ID
        );
        return cnt == null ? 0 : cnt;
    }
}
