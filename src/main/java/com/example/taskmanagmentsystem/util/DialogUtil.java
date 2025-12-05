package com.example.taskmanagmentsystem.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class DialogUtil {

    public static void showInfo(String title, String content) {
        showAlert(title, content, Alert.AlertType.INFORMATION);
    }

    public static void showError(String title, String content) {
        showAlert(title, content, Alert.AlertType.ERROR);
    }

    public static void showWarning(String title, String content) {
        showAlert(title, content, Alert.AlertType.WARNING);
    }

    public static Optional<ButtonType> showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    private static void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

