package com.example.taskmanagmentsystem.model;

import javafx.beans.property.*;

public class Task {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty dueDate;
    private final StringProperty priority;
    private final StringProperty status;

    public Task(int id, String title, String description, String dueDate,
                String priority, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.priority = new SimpleStringProperty(priority);
        this.status = new SimpleStringProperty(status);
    }


    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }


    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }
    public StringProperty titleProperty() { return title; }


    public String getDescription() { return description.get(); }
    public void setDescription(String value) { description.set(value); }
    public StringProperty descriptionProperty() { return description; }


    public String getDueDate() { return dueDate.get(); }
    public void setDueDate(String value) { dueDate.set(value); }
    public StringProperty dueDateProperty() { return dueDate; }


    public String getPriority() { return priority.get(); }
    public void setPriority(String value) { priority.set(value); }
    public StringProperty priorityProperty() { return priority; }


    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }
    public StringProperty statusProperty() { return status; }
}

