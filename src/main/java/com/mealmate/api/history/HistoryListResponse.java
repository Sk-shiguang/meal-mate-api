package com.mealmate.api.history;

import java.util.List;

public record HistoryListResponse(List<HistoryItemResponse> items, int total) {
}
