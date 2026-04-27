package com.mealmate.api.history;

public interface HistoryService {
    HistoryListResponse list(String mealTime, Integer limit, Integer offset);
}
