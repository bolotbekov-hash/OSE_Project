Task Management System (JavaFX + SQLite)

Система управления задачами, созданная на JavaFX с использованием SQLite как локальной постоянной базы данных.
Приложение позволяет создавать, редактировать, удалять и фильтровать задачи — полный набор CRUD-операций, фильтрацию по статусам, управление приоритетами и удобный чистый интерфейс.

Финальный проект по курсам OSE & BAP с использованием JavaFX, архитектуры MVC и подключением базы данных.

 Функции
Управление задачами (CRUD)

Добавление новых задач

Редактирование существующих

Удаление выбранной задачи

Массовое удаление (Clear All)

Автоматическое обновление TableView

Фильтрация и сортировка

Фильтр по статусу:

Pending (Ожидает)

In Progress (В процессе)

Completed (Завершено)

Автоматическое обновление UI через ObservableList

UI/UX

Чистый, современный интерфейс JavaFX

Проверка ошибок и всплывающие уведомления

Автоматическое заполнение полей при выборе задачи

ComboBox для приоритета и статуса

DatePicker для выбора даты

Хранилище

SQLite с автоматическим созданием таблицы

Постоянное хранение в файле bd.db

 Технологический стек
Компонент	Технология
Язык	Java 17+
UI Framework	JavaFX
База данных	SQLite
Сборщик	Maven
Архитектура	MVC
 Структура проекта
task-management-system/
│
├── pom.xml
├── bd.db                                   # SQLite база данных
│
└── src/
    └── main/
        ├── java/
        │   │
        │   ├── module-info.java
        │   │
        │   └── task.management/
        │       ├── controllers/
        │       │   └── TaskController.java         # CRUD + фильтрация
        │       │
        │       ├── database/
        │       │   └── Database.java               # Подключение к SQLite
        │       │
        │       ├── models/
        │       │   └── Task.java                   # Модель c JavaFX properties
        │       │
        │       └── TaskManagementSystem.java       # Точка входа JavaFX
        │
        └── resources/
            └── task.management/
                ├── main.fxml
                └── styles.css

 Как запустить проект
1. Клонировать репозиторий
git clone <your-repository-url>
cd task-management-system

2. Запуск через Maven
mvn clean javafx:run


Требования:

JDK 17+

Maven

JavaFX-зависимости в pom.xml

3. Сборка JAR (опционально)
mvn clean package


Запуск:

java -jar target/task-management-1.0.jar

 Структура базы данных
Таблица: tasks
column	type	description
id	INTEGER PK	авто-инкремент
title	TEXT	название задачи
description	TEXT	детали
due_date	TEXT	формат yyyy-MM-dd
priority	TEXT	Low / Medium / High
status	TEXT	Pending / In Progress / Completed
   Скриншоты
<img width="1674" height="975" alt="image" src="https://github.com/user-attachments/assets/555887bc-2f17-40c7-940f-3064586e507e" />

 Как это работает
Добавление задачи

Пользователь заполняет поля

Нажимает Add

Проверка данных

SQL INSERT

Таблица обновляется (ObservableList)

Редактирование

Выбор задачи

Поля заполняются автоматически

Изменение → Update

SQL UPDATE

Мгновенное обновление UI

Удаление

Диалог подтверждения

SQL DELETE

Элемент удаляется из списка

Фильтрация

Выбор статуса

Применение линейного алгоритма O(n)

Показ только подходящих задач

 Архитектурные решения
1. MVC

Model — Task.java

View — FXML

Controller — TaskController

2. JavaFX Properties

Использовано вместо обычных полей:

StringProperty title;
IntegerProperty id;

3. SQLite

Преимущества:
 не требует сервера
 быстрая локальная работа
 простое подключение через JDBC

4. ObservableList

Таблица автоматически обновляется после CRUD

 Алгоритмы и структуры данных
Алгоритмы

Линейная фильтрация O(n)

Проверка ввода O(1)

SQL CRUD

Транзакции для массового удаления

Структуры данных

ObservableList<Task>

JavaFX Properties (паттерн Observer)

JDBC ResultSet

 Проблемы и решения
1. Таблица не обновлялась

Исправлено с помощью JavaFX Properties + ObservableList.

2. AUTO_INCREMENT не сбрасывался

Решение:

DELETE FROM sqlite_sequence WHERE name='tasks';

3. Проблемы с датой

Использован единый форматер:

yyyy-MM-dd

4. Фильтр ломал основной список

Исправлено сохранением оригинального списка и отображением только результата фильтра.

 Идеи для улучшения

Поиск по названию и описанию

Сортировка по приоритету, статусу, дате

Светлая/тёмная темы

Уведомления о просроченных задачах

Экспорт в CSV

Undo/Redo

Режим администратора

 Видео

https://drive.google.com/file/d/1XO4Dcl6IngPKa1h0W4ugZTmTZI4f523H/view?usp=drive_link

 Автор
Разработал: Alibek Bolotbekov / группа  SCA 24-A
