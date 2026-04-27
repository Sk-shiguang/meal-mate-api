package com.mealmate.api.history;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryHistoryService implements HistoryService {
    private final List<HistoryItemResponse> items = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger(1);

    public InMemoryHistoryService() {
    }

    public void add(String mealId, String mealTime) {
        int n = counter.getAndIncrement();
        items.add(new HistoryItemResponse(
                "hist_" + n,
                mealId,
                mealId,
                "rice",
                mealTime,
                Instant.now(),
                "recommendation",
                null,
                null
        ));
    }

    @Override
    public HistoryListResponse list(String mealTime, Integer limit, Integer offset) {
        List<HistoryItemResponse> filtered = items.stream()
                .filter(item -> mealTime == null || item.mealTime().equals(mealTime))
                .sorted(Comparator.comparing(HistoryItemResponse::eatenAt).reversed())
                .collect(Collectors.toList());
        int total = filtered.size();
        int start = offset == null ? 0 : offset;
        int end = limit == null ? total : Math.min(start + limit, total);
        return new HistoryListResponse(filtered.subList(start, end), total);
    }
}
