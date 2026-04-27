# MealMate Java 后端

MealMate / 吃什么小程序的 Java 后端服务。

## 技术栈

```text
Java 25 LTS
Spring Boot 3.5.14
Maven
PostgreSQL
Docker Compose
Flyway
MyBatis Plus
Spring Validation
SpringDoc OpenAPI
JUnit 5
```

## 本地启动

### 1. 启动数据库

```bash
docker compose up -d
```

### 2. 启动后端

```bash
mvn spring-boot:run
```

### 3. 健康检查

```bash
curl http://localhost:8080/health
```

## 接口文档

启动后访问：

```text
http://localhost:8080/swagger-ui/index.html
```

## 目录说明

```text
src/main/java/com/mealmate/api/
├── common/          # 通用返回、异常、工具
├── config/          # 配置
├── user/            # 用户模块
├── preference/      # 偏好模块
├── meal/            # 菜品模块
├── recommendation/  # 推荐模块
├── favorite/        # 收藏模块
├── history/         # 历史模块
└── feedback/        # 反馈模块
```
