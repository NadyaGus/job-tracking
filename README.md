# Job Tracking Application

Приложение для отслеживания вакансий и рекомендации подходящих позиций на основе навыков пользователей.

## Возможности

- Управление пользователями и вакансиями через REST API
- Система рекомендаций вакансий на основе навыков и опыта
- Статистика по вакансиям, опыту и популярности навыков
- Автоматическая рассылка рекомендаций через scheduled задачи
- База данных PostgreSQL для постоянного хранения данных

## Требования

- Java 17+
- PostgreSQL 12+
- Gradle 7.6+
- Docker (для запуска интеграционных тестов)

## Сборка проекта

Сборка в докер-контейнере
```bash
docker compose up --build -d
```

Сборка с тестами (требует запущенный Docker для интеграционных тестов):

```bash
./gradlew build
```

Сборка без тестов:

```bash
./gradlew build -x test
```

## Запуск приложения

Запуск с веб-сервером и REST API:

```bash
java -jar build/libs/job-tracking-0.0.1-SNAPSHOT.jar
```

## REST API endpoints

- `POST /users` — добавить пользователя
- `GET /users` — список всех пользователей
- `GET /users/{name}` — пользователь по имени
- `DELETE /users/{name}` — удалить пользователя
- `POST /jobs` — добавить вакансию
- `GET /jobs` — список всех вакансий
- `GET /jobs/{title}` — вакансия по названию
- `DELETE /jobs/{title}` — удалить вакансию
- `GET /suggest?username=<name>` — рекомендации для пользователя
- `GET /stat/exp?experience=<n>` — вакансии по опыту
- `GET /stat/match?count=<n>` — пользователи по количеству совпадений
- `GET /stat/top-skills?n=<n>` — топ навыков

## Архитектура

### Слои приложения

- `domain` — доменные модели (User, Job)
- `repository` — слой доступа к данным (JdbcTemplate + PostgreSQL)
- `service` — бизнес-логика и сервисы (включая ScheduledSuggester)
- `web` — REST API контроллеры

## Разработка

### Запуск в IDE
Выберите `WebApplication` как main class.

### Тестирование

Запуск модульных тестов:
```bash
./gradlew test --tests "ru.vk.education.job.service.SuggestServiceTest"
```

Запуск всех тестов (требует запущенный Docker):
```bash
./gradlew test
```

## Лицензия

MIT License
