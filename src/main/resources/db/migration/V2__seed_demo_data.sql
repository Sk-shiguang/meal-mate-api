insert into users (id, nickname, avatar_url, status)
values ('demo-user', '时光', '', 'active')
on conflict (id) do nothing;

insert into user_preferences (
    id, user_id, meal_times, tastes, avoidances, budget_min, budget_max, dining_modes, goals, cooking_time
) values (
    'pref-demo-user',
    'demo-user',
    '["lunch", "dinner"]'::jsonb,
    '["light", "savory"]'::jsonb,
    '[]'::jsonb,
    15,
    35,
    '["takeaway", "dine_in"]'::jsonb,
    '["comfort", "save_money"]'::jsonb,
    'within_30_min'
)
on conflict (user_id) do nothing;

insert into meals (
    id, name, description, category, meal_times, tastes, tags, avoidances,
    budget_min, budget_max, dining_modes, cooking_time, calories_level, base_score, status
) values
('meal_001', '番茄牛腩饭', '暖胃、有满足感，适合午餐或晚餐。', 'rice', '["lunch", "dinner"]', '["savory", "light"]', '["warm", "comfort"]', '[]', 22, 35, '["takeaway", "dine_in"]', 'within_30_min', 'medium', 82, 'enabled'),
('meal_002', '鸡腿饭', '稳定不出错的饱腹选择。', 'rice', '["lunch", "dinner"]', '["savory"]', '["quick", "takeaway_friendly"]', '[]', 18, 30, '["takeaway", "dine_in"]', 'within_15_min', 'medium', 78, 'enabled'),
('meal_003', '牛肉粉', '热乎、快速，适合忙碌的一餐。', 'noodle', '["breakfast", "lunch", "dinner"]', '["savory", "spicy"]', '["warm", "quick", "busy_day"]', '[]', 16, 28, '["dine_in", "takeaway"]', 'within_15_min', 'medium', 80, 'enabled'),
('meal_004', '酸汤肥牛饭', '开胃、有冲击力，适合想换口味时。', 'rice', '["lunch", "dinner"]', '["sour", "spicy"]', '["heavy_taste", "treat_yourself"]', '[]', 25, 38, '["takeaway", "dine_in"]', 'within_30_min', 'high', 76, 'enabled'),
('meal_005', '青菜豆腐汤饭', '清淡低负担，适合想吃舒服点。', 'soup', '["lunch", "dinner"]', '["light"]', '["warm", "light", "low_calorie"]', '[]', 15, 25, '["home_cooking", "dine_in"]', 'within_30_min', 'low', 74, 'enabled'),
('meal_006', '照烧鸡肉饭', '微甜咸香，接受度高。', 'rice', '["lunch", "dinner"]', '["sweet", "savory"]', '["comfort", "takeaway_friendly"]', '[]', 20, 32, '["takeaway", "dine_in"]', 'within_30_min', 'medium', 77, 'enabled'),
('meal_007', '麻辣烫', '自由搭配，适合想吃热乎又有选择感。', 'snack', '["lunch", "dinner", "night"]', '["spicy"]', '["warm", "heavy_taste", "rainy_day"]', '[]', 18, 40, '["dine_in", "takeaway"]', 'within_30_min', 'medium', 79, 'enabled'),
('meal_008', '鲜虾云吞面', '汤面类，轻盈但有满足感。', 'noodle', '["breakfast", "lunch", "dinner"]', '["light", "savory"]', '["warm", "comfort"]', '["seafood"]', 20, 34, '["dine_in", "takeaway"]', 'within_30_min', 'medium', 75, 'enabled'),
('meal_009', '全麦鸡胸肉卷', '高蛋白、低负担，适合减脂目标。', 'light_food', '["breakfast", "lunch", "dinner"]', '["light"]', '["high_protein", "low_calorie", "quick"]', '[]', 18, 30, '["takeaway", "home_cooking"]', 'within_15_min', 'low', 72, 'enabled'),
('meal_010', '皮蛋瘦肉粥', '温和、暖胃，适合早餐或不想吃太重时。', 'porridge', '["breakfast", "night"]', '["light", "savory"]', '["warm", "comfort"]', '[]', 8, 18, '["takeaway", "dine_in", "home_cooking"]', 'within_30_min', 'low', 73, 'enabled')
on conflict (id) do nothing;
