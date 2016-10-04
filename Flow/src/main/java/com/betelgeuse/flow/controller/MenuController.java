package com.betelgeuse.flow.controller;

import com.betelgeuse.flow.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by User on 8/27/2016.
 */
public class MenuController implements Initializable {


    @FXML
    private Button startGameButton;

    public MenuController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void startGame(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(startGameButton)) {
            try {
                Main.showGame(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
