package com.example.taskmanagmentsystem.controller;

import com.example.taskmanagmentsystem.model.Task;
import com.example.taskmanagmentsystem.util.DialogUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskController {

    private final ObservableList<Task> taskList;
    private Task selectedTask;
    private final String dbUrl;

    public TaskController() {
        this.taskList = FXCollections.observableArrayList();
        this.dbUrl = "jdbc:sqlite:bd.db";
        initDatabase();
        loadFromDatabase();
    }

    public ObservableList<Task> getTaskList() {
        return taskList;
    }

    public void setSelectedTask(Task task) {
        this.selectedTask = task;
    }

    public Task getSelectedTask() {
        return selectedTask;
    }


    private void initDatabase() {
        String createTable = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "due_date TEXT NOT NULL," +
                "priority TEXT," +
                "status TEXT" +
                ");";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
        } catch (SQLException e) {
            DialogUtil.showError("DB Error", "Failed to initialize database: " + e.getMessage());
        }
    }


    private void loadFromDatabase() {
        taskList.clear();
        String sql = "SELECT id, title, description, due_date, priority, status FROM tasks ORDER BY id";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                taskList.add(new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("due_date"),
                        rs.getString("priority"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            DialogUtil.showError("DB Error", "Failed to load tasks: " + e.getMessage());
        }
    }


    public void addTask(String title, String description, LocalDate dueDate,
                        String priority, String status) {
        if (!validateInput(title, description, dueDate)) {
            return;
        }

        String sql = "INSERT INTO tasks(title, description, due_date, priority, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ps.setString(4, priority);
            ps.setString(5, status);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);

                    taskList.add(new Task(
                            id,
                            title,
                            description,
                            dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            priority,
                            status
                    ));
                }
            }

            DialogUtil.showInfo("Success", "Task added successfully!");

        } catch (SQLException e) {
            DialogUtil.showError("DB Error", "Failed to add task: " + e.getMessage());
        }
    }


    public void updateTask(String title, String description, LocalDate dueDate,
                           String priority, String status) {

        if (selectedTask == null) {
            DialogUtil.showError("Error", "Please select a task to update");
            return;
        }
        if (!validateInput(title, description, dueDate)) {
            return;
        }

        String sql = "UPDATE tasks SET title=?, description=?, due_date=?, priority=?, status=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ps.setString(4, priority);
            ps.setString(5, status);
            ps.setInt(6, selectedTask.getId());

            ps.executeUpdate();


            selectedTask.setTitle(title);
            selectedTask.setDescription(description);
            selectedTask.setDueDate(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            selectedTask.setPriority(priority);
            selectedTask.setStatus(status);

            DialogUtil.showInfo("Success", "Task updated successfully!");

        } catch (SQLException e) {
            DialogUtil.showError("DB Error", "Failed to update task: " + e.getMessage());
        }
    }


    public void deleteTask() {
        if (selectedTask == null) {
            DialogUtil.showError("Error", "Please select a task to delete");
            return;
        }

        DialogUtil.showConfirmation("Confirm Delete", "Are you sure you want to delete this task?")
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {

                        try (Connection conn = DriverManager.getConnection(dbUrl)) {

                            conn.setAutoCommit(false);


                            String deleteSql = "DELETE FROM tasks WHERE id=?";
                            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                                ps.setInt(1, selectedTask.getId());
                                ps.executeUpdate();
                            }


                            String countSql = "SELECT COUNT(*) FROM tasks";
                            boolean tableEmpty = false;

                            try (PreparedStatement ps = conn.prepareStatement(countSql);
                                 ResultSet rs = ps.executeQuery()) {
                                if (rs.next() && rs.getInt(1) == 0) {
                                    tableEmpty = true;
                                }
                            }

                            if (tableEmpty) {
                                String resetSql = "DELETE FROM sqlite_sequence WHERE name='tasks'";
                                try (PreparedStatement ps = conn.prepareStatement(resetSql)) {
                                    ps.executeUpdate();
                                }
                            }

                            conn.commit();

                            taskList.remove(selectedTask);
                            selectedTask = null;

                            DialogUtil.showInfo("Success", "Task deleted successfully!");

                        } catch (SQLException e) {
                            DialogUtil.showError("DB Error", "Failed to delete task: " + e.getMessage());
                        }
                    }
                });
    }


    public void clearAllTasks() {

        DialogUtil.showConfirmation("Delete ALL Tasks",
                        "Are you sure you want to delete ALL tasks?")
                .ifPresent(response -> {

                    if (response != ButtonType.OK) return;

                    try (Connection conn = DriverManager.getConnection(dbUrl);
                         Statement stmt = conn.createStatement()) {


                        stmt.executeUpdate("DELETE FROM tasks");

                        stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='tasks';");

                    } catch (SQLException e) {
                        DialogUtil.showError("DB Error", "Failed to clear tasks: " + e.getMessage());
                        return;
                    }

                    taskList.clear();
                    selectedTask = null;

                    DialogUtil.showInfo("Success", "All tasks deleted!");
                });
    }


    private boolean validateInput(String title, String description, LocalDate dueDate) {
        if (title == null || title.trim().isEmpty()) {
            DialogUtil.showError("Validation Error", "Please enter a task title");
            return false;
        }
        if (description == null || description.trim().isEmpty()) {
            DialogUtil.showError("Validation Error", "Please enter a task description");
            return false;
        }
        if (dueDate == null) {
            DialogUtil.showError("Validation Error", "Please select a due date");
            return false;
        }
        return true;
    }
}

