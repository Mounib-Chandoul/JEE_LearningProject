package com.example.frontend.Client;

import com.example.frontend.HelloApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final String DEFAULT_LOGIN_URL = "http://localhost:8080/JJE_PROJECT-1.0-SNAPSHOT/api/auth/login";
    // Change this string inside your JavaFX FrontEnd project:
    private static final String LOGIN_URL = "http://localhost:8080/JJE_PROJECT-1.0-SNAPSHOT/api/auth/login";    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // 1. Client-side input validation
        if (username.isEmpty() || password.isEmpty()) {
            updateErrorUi("Username and password cannot be empty.");
            return;
        }

        updateErrorUi(""); // Clear previous errors

        // 2. Build the JSON payload string securely
        // (Escaping characters manually to avoid external parsing library setup for now)
        String jsonPayload = String.format("{\"username\":\"%s\",\"password\":\"%s\"}",
                escapeJson(username), escapeJson(password));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LOGIN_URL))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        // 3. Send network request asynchronously to keep UI thread fluid and active
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        String responseBody = response.body();

                        // Parse out token data using our secure token parser helper
                        String token = extractJsonField(responseBody, "token");
                        String role = extractJsonField(responseBody, "role");

                        // Save authorization tokens to our memory cache
                        UserSession.getInstance().setToken(token);
                        UserSession.getInstance().setUsername(username);
                        UserSession.getInstance().setRole(role);

                        // 4. Update UI context / swap screens on the main JavaFX thread
                        Platform.runLater(this::navigateToDashboard);
                    } else if (response.statusCode() == 401) {
                        updateErrorUi("Invalid username or password.");
                    } else {
                        updateErrorUi("Server encountered an error (" + response.statusCode() + ").");
                    }
                })
                .exceptionally(ex -> {
                    updateErrorUi("Cannot establish connection to authentication server.");
                    return null;
                });
    }

    private void navigateToDashboard() {
        try {
            // Swap out layout context smoothly to primary tracking panel
            HelloApplication.changeScene("/com/example/frontend/student-view.fxml", "Management Dashboard", 800, 500);
        } catch (IOException e) {
            updateErrorUi("Interface engine failed to load application dashboard.");
            e.printStackTrace();
        }
    }

    private void updateErrorUi(String message) {
        // Enforces updates back onto the JavaFX main application runner thread safely
        Platform.runLater(() -> errorLabel.setText(message));
    }

    private String extractJsonField(String json, String field) {
        try {
            String pattern = "\"" + field + "\":\"";
            int startIdx = json.indexOf(pattern) + pattern.length();
            int endIdx = json.indexOf("\"", startIdx);
            return json.substring(startIdx, endIdx);
        } catch (Exception e) {
            return "";
        }
    }

    private String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}


