package com.example.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        loadScene("/com/example/frontend/login-view.fxml", "Secure Login", 350, 400);
        stage.setResizable(false);
        stage.show();
    }

    public static void changeScene(String fxmlPath, String title, double width, double height) throws IOException {
        loadScene(fxmlPath, title, width, height);
    }

    private static void loadScene(String fxmlPath, String title, double width, double height) throws IOException {
        URL resource = HelloApplication.class.getResource(fxmlPath);
        if (resource == null) {
            throw new IOException("Cannot find FXML file: " + fxmlPath);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}
