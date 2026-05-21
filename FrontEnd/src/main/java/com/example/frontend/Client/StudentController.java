package com.example.frontend.Client;

import com.example.frontend.HelloApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Long> idColumn;
    @FXML private TableColumn<Student, String> firstNameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, String> emailColumn;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final String BASE_URL = "http://localhost:8080/JJE_PROJECT-1.0-SNAPSHOT/api/students";

    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        studentTable.setItems(studentList);

        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                firstNameField.setText(newSelection.getFirstName());
                lastNameField.setText(newSelection.getLastName());
                emailField.setText(newSelection.getEmail());
            }
        });

        loadStudents();
    }

    @FXML
    private void loadStudents() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Accept", "application/json")
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        List<Student> students = parseStudentList(response.body());
                        Platform.runLater(() -> {
                            studentList.setAll(students);
                        });
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    @FXML
    private void handleSave() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) return;

        Student student = new Student(selected != null ? selected.getId() : null, firstName, lastName, email);
        String jsonPayload = String.format("{\"id\":%s,\"firstName\":\"%s\",\"lastName\":\"%s\",\"email\":\"%s\"}",
                student.getId() == null ? "null" : student.getId(), firstName, lastName, email);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 201 || response.statusCode() == 200) {
                        Platform.runLater(() -> {
                            loadStudents();
                            handleClear();
                        });
                    }
                });
    }

    @FXML
    private void handleDelete() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + selected.getId()))
                .DELETE()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 204) {
                        Platform.runLater(this::loadStudents);
                    }
                });
    }

    @FXML
    private void handleClear() {
        studentTable.getSelectionModel().clearSelection();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().cleanSession();
        try {
            HelloApplication.changeScene("/com/example/frontend/login-view.fxml", "Secure Login", 350, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Student> parseStudentList(String json) {
        // Very basic JSON list parser since we don't have Jackson/Gson easily available in context
        // This splits by the start of each object
        if (json == null || json.equals("[]")) return List.of();
        
        return Stream.of(json.substring(1, json.length() - 1).split("\\},\\{"))
                .map(s -> {
                    String clean = s.replace("{", "").replace("}", "");
                    Long id = Long.valueOf(extractField(clean, "id"));
                    String fn = extractField(clean, "firstName");
                    String ln = extractField(clean, "lastName");
                    String em = extractField(clean, "email");
                    return new Student(id, fn, ln, em);
                })
                .collect(Collectors.toList());
    }

    private String extractField(String segment, String field) {
        try {
            String pattern = "\"" + field + "\":";
            int start = segment.indexOf(pattern) + pattern.length();
            if (segment.charAt(start) == '\"') {
                start++;
                int end = segment.indexOf("\"", start);
                return segment.substring(start, end);
            } else {
                int end = segment.indexOf(",", start);
                if (end == -1) end = segment.length();
                return segment.substring(start, end).trim();
            }
        } catch (Exception e) {
            return "";
        }
    }
}


