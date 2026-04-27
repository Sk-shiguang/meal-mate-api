package com.mealmate.api.history;

import java.time.Instant;

public record HistoryItemResponse(
        String id,
        String mealId,
        String mealName,
        String category,
        String mealTime,
        Instant eatenAt,
        String source,
        Integer rating,
        String note
) {
}
