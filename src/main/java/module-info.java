module com.example.taskmanagmentsystem {
        requires javafx.controls;
        requires javafx.fxml;
        requires java.sql;

        opens com.example.taskmanagmentsystem to javafx.fxml;
        opens com.example.taskmanagmentsystem.model to javafx.base;

        exports com.example.taskmanagmentsystem;
        exports com.example.taskmanagmentsystem.model;
        exports com.example.taskmanagmentsystem.controller;
        }


