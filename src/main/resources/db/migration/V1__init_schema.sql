create table users (
    id varchar(64) primary key,
    open_id varchar(128),
    nickname varchar(64) not null,
    avatar_url varchar(512),
    status varchar(32) not null default 'active',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create unique index uk_users_open_id on users(open_id) where open_id is not null;

create table user_preferences (
    id varchar(64) primary key,
    user_id varchar(64) not null references users(id),
    meal_times jsonb not null default '[]'::jsonb,
    tastes jsonb not null default '[]'::jsonb,
    avoidances jsonb not null default '[]'::jsonb,
    budget_min integer,
    budget_max integer,
    dining_modes jsonb not null default '[]'::jsonb,
    goals jsonb not null default '[]'::jsonb,
    cooking_time varchar(64),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint uk_user_preferences_user_id unique(user_id)
);

create table meals (
    id varchar(64) primary key,
    name varchar(128) not null,
    description text,
    category varchar(64) not null,
    meal_times jsonb not null default '[]'::jsonb,
    tastes jsonb not null default '[]'::jsonb,
    tags jsonb not null default '[]'::jsonb,
    avoidances jsonb not null default '[]'::jsonb,
    budget_min integer,
    budget_max integer,
    dining_modes jsonb not null default '[]'::jsonb,
    cooking_time varchar(64),
    calories_level varchar(32),
    image_url varchar(512),
    base_score integer not null default 60,
    status varchar(32) not null default 'enabled',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index idx_meals_category on meals(category);
create index idx_meals_status on meals(status);
create index idx_meals_base_score on meals(base_score);

create table favorites (
    id varchar(64) primary key,
    user_id varchar(64) not null references users(id),
    meal_id varchar(64) not null references meals(id),
    note varchar(255),
    created_at timestamptz not null default now(),
    constraint uk_favorites_user_meal unique(user_id, meal_id)
);

create index idx_favorites_user_id on favorites(user_id);

create table meal_histories (
    id varchar(64) primary key,
    user_id varchar(64) not null references users(id),
    meal_id varchar(64) not null references meals(id),
    meal_time varchar(32) not null,
    eaten_at timestamptz not null default now(),
    source varchar(32) not null default 'recommendation',
    rating integer,
    note varchar(255),
    created_at timestamptz not null default now()
);

create index idx_meal_histories_user_eaten_at on meal_histories(user_id, eaten_at desc);
create index idx_meal_histories_user_meal_time on meal_histories(user_id, meal_time);

create table meal_feedback (
    id varchar(64) primary key,
    user_id varchar(64) not null references users(id),
    meal_id varchar(64) not null references meals(id),
    type varchar(64) not null,
    reason varchar(255),
    created_at timestamptz not null default now()
);

create index idx_meal_feedback_user_created_at on meal_feedback(user_id, created_at desc);
create index idx_meal_feedback_user_meal on meal_feedback(user_id, meal_id);
create index idx_meal_feedback_type on meal_feedback(type);

create table recommendation_logs (
    id varchar(64) primary key,
    user_id varchar(64) not null references users(id),
    main_meal_id varchar(64) references meals(id),
    alternative_meal_ids jsonb not null default '[]'::jsonb,
    scene varchar(64),
    meal_time varchar(32),
    reason text,
    strategy varchar(64) not null default 'preference_weighted',
    created_at timestamptz not null default now()
);

create index idx_recommendation_logs_user_created_at on recommendation_logs(user_id, created_at desc);
