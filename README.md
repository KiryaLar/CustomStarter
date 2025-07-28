# Домашнее задание №3

## Описание
Проект состоит из двух частей: **synthetic-human-core-starter** — это Spring Boot стартер, который предоставляет функциональность для обработки команд, аудита действий, мониторинга и обработки ошибок для синтетических систем, и **bishop-prototype** — демонстрационный сервис, который показывает работу стартера через REST API.

### Модули стартера
| Модуль | Описание | Ключевые компоненты |
|--------|----------|---------------------|
| Обработка команд | Принимает, валидирует и исполняет команды с приоритетами (CRITICAL моментально, COMMON в очередь). | CommandDto, CommandService, ThreadPoolExecutor |
| Аудит | Логирует действия, аннотированные @WeylandWatchingYou, по умолчанию в консоль, поддерживает Kafka. | @WeylandWatchingYou, AuditAspect, AuditService |
| Мониторинг | Предоставляет метрики через Actuator: текущая нагрузка и выполненные команды по авторам. | MonitoringService, Actuator эндпоинты |
| Обработка ошибок | Возвращает унифицированные ответы на ошибки с соответствующими HTTP-кодами. | GlobalExceptionHandler, ErrorResponse |

### Обработка команд
Модуль позволяет принимать команды через REST API, валидировать их (например, проверка длины описания, формата времени) и исполнять. Команды с приоритетом CRITICAL выполняются сразу, а с COMMON — добавляются в очередь, управляемую ThreadPoolExecutor. Очередь имеет ограниченную ёмкость, и при переполнении возвращается ошибка 429 (TOO_MANY_REQUESTS).

### Аудит
Аудит активируется аннотацией @WeylandWatchingYou на методах. По умолчанию события выводятся в консоль через ConsoleAuditService. Если настроен audit.mode=kafka, события отправляются в Kafka (требуется настройка Kafka, но реализация не включена в данный проект). Пример конфигурации application.properties:
```properties
starter.audit.mode=console
```

### Мониторинг
Метрики доступны через Actuator:

- **/actuator/metrics/android.workload** — текущая нагрузка андройда (количество задач в очереди).
- **/actuator/metrics/commands.completed** — количество выполненных команд от каждого автора. 

Для доступа включите в application.properties:
```properties
management.endpoints.web.exposure.include=metrics
```

### Обработка ошибок
Модуль перехватывает различные исключения, такие как ошибки валидации (400), переполнение очереди (429) и неожиданные ошибки (500), возвращая JSON-ответ (сущность ErrorResponse) с полями status, message и timestamp.

## Инструкции по настройке

### 1. Клонируйте репозиторий:
```bash
git clone <url-репозитория>
cd CustomStarter
```

### 2. Установите стартер локально:
```bash
cd synthetic-human-core-starter
mvn clean install
```
Это сгенерирует JAR и сделает его доступным по groupId/artifactId/version в других проектах.

### 3. Добавьте зависимость в pom.xml (в сервисе-примере уже есть):
```xml
<dependency>
    <groupId>ru.larkin</groupId>
    <artifactId>synthetic-human-core-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 4. Настройте свойства в application.properties:
```properties
starter.audit.mode: "console" (по умолчанию) или "kafka".
starter.threadPool.coreSize: Размер пула потоков (по умолчанию 1).
starter.threadPool.maxSize: Максимальный размер пула (по умолчанию 4).
starter.threadPool.queueCapacity: Ёмкость очереди (по умолчанию 10).
```

### 5. Используйте модули:
- Для обработки команд: внедрите ```CommandService``` и вызовите processCommand(CommandDto).
- Для аудита: аннотируйте методы ```@WeylandWatchingYou```.
- Для мониторинга: используйте Actuator эндпоинты.

## Демонстрационный сервис: bishop-prototype

### 1. Соберите проект:
```bash
cd bishop-prototype
mvn clean package
```

### 2. Запустите приложение:
```bash
mvn spring-boot:run
```

### 3. Использование API:
- POST-запрос на /api/bishop с телом:
```json
{
    "description": "Запустить ракету в космос",
    "priority": "CRITICAL",
    "author": "Дедушка Рик",
    "time": "2025-07-26T12:00:00Z"
}
```
Используйте инструменты вроде Postman или curl для тестирования.
