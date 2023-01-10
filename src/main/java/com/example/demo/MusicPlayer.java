package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MusicPlayer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicPlayerFXML.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("MusicPlayer");
        stage.setScene(scene);
        Image icon = new Image("F:\\university\\AdvancedProgramming\\javafxtutorial\\demo\\icon\\images.png");
        stage.getIcons().add(icon);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}