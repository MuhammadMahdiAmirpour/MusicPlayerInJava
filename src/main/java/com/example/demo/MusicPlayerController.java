package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MusicPlayerController implements Initializable {
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label songNameLabel;
    @FXML
    private ProgressBar musicProgressBar;
    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private int songNumber;
    private final int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};
    private Timer timer;
    private TimerTask timerTask;
    private boolean running;
    private Media media;
    private MediaPlayer mediaPlayer;

    public void playMedia() {
        beginTimer();
        chooseSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    public void pauseMedia() {
        cancelTimer();
        mediaPlayer.pause();
    }

    public void resetMedia() {
        musicProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void previousMedia() {
        songNumber--;
        if (songNumber < 0) {
            songNumber += songs.size();
        }
        mediaPlayer.stop();
        if (running) {
            cancelTimer();
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songNameLabel.setText(songs.get(songNumber).getName());
        playMedia();
    }

    public void nextMedia() {
        songNumber = (songNumber + 1) % songs.size();
        mediaPlayer.stop();
        if (running) {
            cancelTimer();
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songNameLabel.setText(songs.get(songNumber).getName());
        playMedia();
    }

    public void chooseSpeed(ActionEvent event) {
        if (speedBox.getValue() == null) {
            mediaPlayer.setRate(1);
        } else {
            mediaPlayer.setRate(
                    Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
        }
    }

    public void beginTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                musicProgressBar.setProgress(current/end);
                if (current/end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public void cancelTimer() {
        running = false;
        timer.cancel();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<>();
        directory = new File("music");
        files = directory.listFiles();
        if (files != null) {
            songs.addAll(Arrays.asList(files));
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songNameLabel.setText(songs.get(songNumber).getName());
        Arrays.stream(speeds).forEach(speed -> speedBox.getItems().add(speed + "%"));
        speedBox.setOnAction(this::chooseSpeed);
        volumeSlider.valueProperty().addListener
                ((observableValue, number, t1) -> mediaPlayer.setVolume(volumeSlider.getValue() * 0.01));
        musicProgressBar.setStyle("-fx-accent: darkOrange");
    }
}
