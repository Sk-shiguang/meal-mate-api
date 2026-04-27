package com.mealmate.api.recommendation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class JdbcRecommendationService implements RecommendationService {
    private static final String DEMO_USER_ID = "demo-user";
    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public JdbcRecommendationService(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public TodayRecommendationResponse recommendToday(TodayRecommendationRequest request) {
        List<String> tastes = loadPreferenceTags("tastes");
        List<String> goals = loadPreferenceTags("goals");
        List<String> avoidances = loadPreferenceTags("avoidances");
        List<RecommendationCandidate> candidates = loadCandidates(request);

        List<RecommendedMealResponse> ranked = candidates.stream()
                .filter(candidate -> avoidances.stream().noneMatch(candidate.avoidances()::contains))
                .map(candidate -> score(candidate, request, tastes, goals))
                .sorted(Comparator.comparingInt(RecommendedMealResponse::score).reversed())
                .toList();

        if (ranked.isEmpty()) {
            throw new IllegalStateException("暂无可推荐菜品");
        }

        RecommendedMealResponse main = ranked.getFirst();
        List<RecommendedMealResponse> alternatives = ranked.stream().skip(1).limit(3).toList();
        String recommendationId = "rec_" + UUID.randomUUID();
        saveLog(recommendationId, main, alternatives, request);

        return new TodayRecommendationResponse(
                recommendationId,
                main,
                alternatives,
                new RecommendationMetaResponse(request.mealTime(), request.scene(), "preference_weighted")
        );
    }

    private List<String> loadPreferenceTags(String column) {
        String json = jdbcTemplate.queryForObject(
                "select " + column + " from user_preferences where user_id = ?",
                String.class,
                DEMO_USER_ID
        );
        return fromJson(json);
    }

    private List<RecommendationCandidate> loadCandidates(TodayRecommendationRequest request) {
        StringBuilder sql = new StringBuilder("""
                select id, name, description, category, tastes, tags, avoidances, budget_min, budget_max, base_score
                from meals
                where status = 'enabled'
                """);
        List<Object> params = new ArrayList<>();
        if (request.mealTime() != null && !request.mealTime().isBlank()) {
            sql.append(" and meal_times @> ?::jsonb");
            params.add(toJsonArray(request.mealTime()));
        }
        if (request.budgetMax() != null) {
            sql.append(" and (budget_min is null or budget_min <= ?)");
            params.add(request.budgetMax());
        }
        List<String> recentMealIds = loadRecentMealIds(request.excludeRecentDays());
        if (!recentMealIds.isEmpty()) {
            sql.append(" and id <> all (?)");
            params.add(recentMealIds.toArray(String[]::new));
        }
        return jdbcTemplate.query(sql.toString(), this::mapCandidate, params.toArray());
    }

    private List<String> loadRecentMealIds(Integer excludeRecentDays) {
        int days = excludeRecentDays == null ? 0 : excludeRecentDays;
        if (days <= 0) {
            return List.of();
        }
        return jdbcTemplate.queryForList("""
                select distinct meal_id
                from meal_histories
                where user_id = ? and eaten_at >= now() - (? * interval '1 day')
                """, String.class, DEMO_USER_ID, days);
    }

    private RecommendationCandidate mapCandidate(ResultSet rs, int rowNum) throws SQLException {
        return new RecommendationCandidate(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("category"),
                fromJson(rs.getString("tastes")),
                fromJson(rs.getString("tags")),
                fromJson(rs.getString("avoidances")),
                (Integer) rs.getObject("budget_min"),
                (Integer) rs.getObject("budget_max"),
                rs.getInt("base_score")
        );
    }

    private RecommendedMealResponse score(RecommendationCandidate candidate, TodayRecommendationRequest request, List<String> tastes, List<String> goals) {
        int score = candidate.baseScore();
        for (String taste : tastes) {
            if (candidate.tastes().contains(taste)) {
                score += 10;
            }
        }
        for (String goal : goals) {
            if (candidate.tags().contains(goal)) {
                score += 10;
            }
        }
        if ("busy".equals(request.scene()) && candidate.tags().contains("quick")) {
            score += 15;
        }
        if ("comfort".equals(request.scene()) && candidate.tags().contains("comfort")) {
            score += 15;
        }
        String reason = "根据你的偏好推荐：" + candidate.name() + " 匹配当前餐次、预算和口味。";
        return new RecommendedMealResponse(
                candidate.id(),
                candidate.name(),
                candidate.description(),
                candidate.category(),
                candidate.tags(),
                candidate.budgetMin(),
                candidate.budgetMax(),
                reason,
                score
        );
    }

    private void saveLog(String recommendationId, RecommendedMealResponse main, List<RecommendedMealResponse> alternatives, TodayRecommendationRequest request) {
        jdbcTemplate.update("""
                insert into recommendation_logs (id, user_id, main_meal_id, alternative_meal_ids, scene, meal_time, reason, strategy)
                values (?, ?, ?, ?::jsonb, ?, ?, ?, ?)
                """,
                recommendationId,
                DEMO_USER_ID,
                main.id(),
                toJson(alternatives.stream().map(RecommendedMealResponse::id).toList()),
                request.scene(),
                request.mealTime(),
                main.reason(),
                "preference_weighted"
        );
    }

    private String toJsonArray(String value) {
        return toJson(List.of(value));
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

    private record RecommendationCandidate(
            String id,
            String name,
            String description,
            String category,
            List<String> tastes,
            List<String> tags,
            List<String> avoidances,
            Integer budgetMin,
            Integer budgetMax,
            int baseScore
    ) {
    }
}
