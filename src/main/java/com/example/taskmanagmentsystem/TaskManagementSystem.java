package com.example.taskmanagmentsystem;

import com.example.taskmanagmentsystem.controller.TaskController;
import com.example.taskmanagmentsystem.model.Task;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskManagementSystem extends Application {

    private TableView<Task> taskTable;
    private TextField titleField;
    private TextArea descriptionArea;
    private DatePicker dueDatePicker;
    private ComboBox<String> priorityComboBox;
    private ComboBox<String> statusComboBox;
    private TaskController controller;

    private ComboBox<String> filterStatusComboBox;


    private ObservableList<Task> tasks;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        controller = new TaskController();
        tasks = controller.getTaskList();
        primaryStage.setTitle("Task Management System");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));


        Label titleLabel = new Label("Task Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox topBox = new HBox(titleLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));
        topBox.setStyle("-fx-background-color: #ecf0f1;");


        taskTable = createTaskTable();


        VBox formBox = createFormBox();

        mainLayout.setTop(topBox);
        mainLayout.setCenter(taskTable);
        mainLayout.setRight(formBox);

        Scene scene = new Scene(mainLayout, 1200, 700);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Task> createTaskTable() {
        TableView<Task> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Task, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Task, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(150);

        TableColumn<Task, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);

        TableColumn<Task, String> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueDateCol.setPrefWidth(100);

        TableColumn<Task, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(80);

        TableColumn<Task, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, titleCol, descCol, dueDateCol, priorityCol, statusCol);
        table.setItems(tasks);


        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                controller.setSelectedTask(newVal);
                populateForm(newVal);
            }
        });

        return table;
    }

    private VBox createFormBox() {
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));
        formBox.setPrefWidth(300);
        formBox.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Label formTitle = new Label("Task Details");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");


        titleField = new TextField();
        titleField.setPromptText("Enter task title");

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Enter task description");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);

        dueDatePicker = new DatePicker();
        dueDatePicker.setValue(LocalDate.now());

        priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll("Low", "Medium", "High");
        priorityComboBox.setValue("Medium");

        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Pending", "In progress", "Completed");
        statusComboBox.setValue("Pending");


        Label filterLabel = new Label("Filter by Status:");
        filterStatusComboBox = new ComboBox<>();
        filterStatusComboBox.getItems().addAll("All", "Pending", "In progress", "Completed");
        filterStatusComboBox.setValue("All");
        filterStatusComboBox.setOnAction(e -> applyStatusFilter());


        Button addButton = createStyledButton("Add Task", "#28a745");
        addButton.setOnAction(e -> {
            controller.addTask(titleField.getText(), descriptionArea.getText(),
                    dueDatePicker.getValue(), priorityComboBox.getValue(), statusComboBox.getValue());
            clearForm();
        });

        Button updateButton = createStyledButton("Update Task", "#007bff");
        updateButton.setOnAction(e -> {
            controller.updateTask(titleField.getText(), descriptionArea.getText(),
                    dueDatePicker.getValue(), priorityComboBox.getValue(), statusComboBox.getValue());
            clearForm();
        });

        Button deleteButton = createStyledButton("Delete Task", "#dc3545");
        deleteButton.setOnAction(e -> {
            controller.deleteTask();
            clearForm();
        });

        Button clearButton = createStyledButton("Clear All Tasks", "#6c757d");
        clearButton.setOnAction(e -> {
            controller.clearAllTasks();
            clearForm();
            taskTable.refresh();
        });

        HBox buttonBox1 = new HBox(10, addButton, updateButton);
        HBox buttonBox2 = new HBox(10, deleteButton, clearButton);

        formBox.getChildren().addAll(
                formTitle,
                new Separator(),
                new Label("Title:"), titleField,
                new Label("Description:"), descriptionArea,
                new Label("Due Date:"), dueDatePicker,
                new Label("Priority:"), priorityComboBox,
                new Label("Status:"), statusComboBox,
                new Separator(),
                filterLabel, filterStatusComboBox,
                new Separator(),
                buttonBox1, buttonBox2
        );

        return formBox;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-padding: 8px 16px; -fx-cursor: hand;", color));
        button.setPrefWidth(135);
        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
        return button;
    }

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        dueDatePicker.setValue(LocalDate.now());
        priorityComboBox.setValue("Medium");
        statusComboBox.setValue("Pending");
        controller.setSelectedTask(null);
        taskTable.getSelectionModel().clearSelection();
        taskTable.refresh();
    }

    private void populateForm(Task task) {
        titleField.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());
        dueDatePicker.setValue(LocalDate.parse(task.getDueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        priorityComboBox.setValue(task.getPriority());
        statusComboBox.setValue(task.getStatus());
    }

    private void applyStatusFilter() {
        String selected = filterStatusComboBox.getValue();

        if (selected == null || selected.equals("All")) {
            taskTable.setItems(tasks);
            return;
        }

        ObservableList<Task> filtered = FXCollections.observableArrayList();
        for (Task task : tasks) {
            if (task.getStatus().equals(selected)) {
                filtered.add(task);
            }
        }
        taskTable.setItems(filtered);
    }
}
