package com.mealmate.api.feedback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InMemoryFeedbackService implements FeedbackService {
    private final List<FeedbackRequest> items = new ArrayList<>();
    private static final Set<String> VALID_TYPES = Set.of("like", "dislike");

    @Override
    public void submit(String mealId, String type, String reason) {
        if (!VALID_TYPES.contains(type)) {
            throw new IllegalArgumentException("反馈类型无效，仅支持 like / dislike");
        }
        items.add(new FeedbackRequest(mealId, type, reason));
    }

    @Override
    public int count() {
        return items.size();
    }
}
