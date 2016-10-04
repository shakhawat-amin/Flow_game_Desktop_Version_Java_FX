package com.betelgeuse.flow;

import com.betelgeuse.flow.controller.GameController;
import com.betelgeuse.flow.controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    private static int currentLevel;

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        showMenu(primaryStage);
    }

    public static void showGame(int levelNumber) throws java.io.IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("/game_grid.fxml"));
        String level = "Level " + levelNumber;
        System.out.println(level);
        ResourceBundle resourceBundle = ResourceBundle.getBundle(level);
        currentLevel = levelNumber;
        fxmlLoader.setResources(resourceBundle);
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        GameController gameController = fxmlLoader.getController();
    }

    private void showMenu(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("/menu.fxml"));
        Parent root = fxmlLoader.load();
        MenuController menuController = fxmlLoader.getController();
        primaryStage.setTitle("Flow");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setHeight(435);
        primaryStage.setWidth(605);
        primaryStage.show();
        this.primaryStage = primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void nextLevel() {
        currentLevel++;
        try {
            showGame(currentLevel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
