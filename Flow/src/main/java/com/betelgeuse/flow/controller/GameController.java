package com.betelgeuse.flow.controller;

import com.betelgeuse.flow.Main;
import com.betelgeuse.flow.ui.Ball;
import com.betelgeuse.flow.ui.Path;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;

public class GameController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label levelLabel;

    @FXML
    private Button restartButton;

    @FXML
    private Label pipePercentLabel;

    @FXML
    private Button nextButton;

    @FXML
    private Button previousButton;

    private Pane[][] grids;

    private String[] ballNames;

    private static final String[] ballTypes = {"one", "two"};

    private Stack<Ball> clickedBall = new Stack<>();

    private List<Ball> balls = new ArrayList<>();

    private HashMap<String, Stack<Pane>> colorsPath = new HashMap<>();

    private static final String PANE_STYLE = "-fx-background-color: black; -fx-border-color : white; -fx-border-width : 1px;";
    private static final String CIRCLE_CLICKED_STYLE = "-fx-stroke: white; -fx-stroke-width: 2px;";
    private static final String CIRCLE_NORMAL_STYLE = "-fx-stroke: black; -fx-stroke-width: 0px;";
    private ResourceBundle resources;


    public GameController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        initLevel(resources);
    }

    private void initLevel(ResourceBundle resources) {
        try {
            String levelName = resources.getBaseBundleName();
            if (levelName.equals("Level 1")) {
                previousButton.setDisable(true);
            }
            levelLabel.setText(levelName);
            ballNames = resources.getString("ball.names").split(",");
            pipePercentLabel.setText("0 %");
            int gridSize = Integer.parseInt(resources.getString("grid.size"));
            grids = new Pane[gridSize][gridSize];
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    Pane pane = new Pane();
                    pane.setPrefWidth(40);
                    pane.setPrefHeight(40);
                    pane.setOnMouseEntered(this::onGridHovered);
                    pane.setOnMouseExited(this::onGridExited);
                    pane.setOnMouseClicked(this::onGridClicked);
                    pane.setStyle(PANE_STYLE);
                    gridPane.add(pane, i, j);
                    grids[i][j] = pane;
                    pane.setId("pane:" + i + ":" + j);
                }

            }
            for (String ballName : ballNames) {
                for (String ballType : ballTypes) {
                    System.out.print(ballName + "." + ballType + ".coordinate.x - ");
                    System.out.println(resources.getString(ballName + "." + ballType + ".coordinate.x"));
                    System.out.print(ballName + "." + ballType + ".coordinate.y - ");
                    System.out.println(resources.getString(ballName + "." + ballType + ".coordinate.y"));

                    int x = Integer.parseInt(resources.getString(ballName + "." + ballType + ".coordinate.x"));
                    int y = Integer.parseInt(resources.getString(ballName + "." + ballType + ".coordinate.y"));

                    Pane pane = grids[x][y];
                    Ball ball = new Ball(ballName);

                    ball.setId("circle:" + ballName + ":" + ballType);
                    balls.add(ball);
                    pane.getChildren().add(ball);
                }
                colorsPath.put(ballName, new Stack<>());
            }

        } catch (MissingResourceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void onGridHovered(MouseEvent event) {
        Ball ball;
        if (!clickedBall.isEmpty()) {
            ball = clickedBall.peek();
            String ballColor = ball.getId().split(":")[1];
            Stack<Pane> pathDrawn = colorsPath.get(ballColor);
            System.out.println(pathDrawn);
            System.out.println(ballColor);
            Pane currentPane = (Pane) event.getSource();
            if (currentPane.getChildren().size() == 0) {
                Pane previousPane = pathDrawn.peek();
                if (isMovePossible(previousPane, currentPane)) {
                    int index = pathDrawn.indexOf(currentPane);
                    System.out.println(index);
                    if (index == -1) {
                        Path path = new Path(currentPane, pathDrawn.peek(), ballColor);
                        pathDrawn.push(currentPane);
                        path.setId(ball.getId());
                        currentPane.getChildren().add(path);
                    } else {
                        removePath(ball, ballColor, pathDrawn, currentPane, index);
                    }
                }
            } else {
                Node node = currentPane.getChildren().get(0);
                if (node instanceof Ball && !clickedBall.contains(node)) {
                    System.out.println("Final Destination!!!");
                    Ball endPathBall = (Ball) node;
                    String endPathBallColor = endPathBall.getId().split(":")[1];
                    Pane previousPane = pathDrawn.peek();
                    if (endPathBallColor.equals(ballColor) && isMovePossible(currentPane, previousPane)) {
                        pathDrawn.push(currentPane);
                        endPathBall.setBallStyle(CIRCLE_CLICKED_STYLE);
                        endPathBall.setRectVisible(currentPane, previousPane, endPathBallColor);
                    }
                } else {
                    int index = pathDrawn.indexOf(currentPane);
                    System.out.println(index);
                    if (index != -1) {
                        System.out.println("index - " + index + "size - " + pathDrawn.size());
                        removePath(ball, ballColor, pathDrawn, currentPane, index);
                    }
                }
            }
        } else {
            System.out.println("No Circle was clicked. No need to draw path.");
        }
        updatePercentage();
    }


    private void updatePipePercentage() {
    }

    private void removePath(Ball ball, String ballColor, Stack<Pane> pathDrawn, Pane currentPane, int index) {
        for (int i = index; i < pathDrawn.size(); ) {
            System.out.println(i);
            Pane pane = pathDrawn.remove(i);
            if ((pane.getChildren().get(0) instanceof Ball)) {
                ((Ball) pane.getChildren().get(0)).setAllPathVisible(false);
            } else if (pane.getChildren().get(0) instanceof Path) {
                pane.getChildren().remove(0);
            }
        }
        if (!pathDrawn.isEmpty()) {
            Path path = new Path(currentPane, pathDrawn.peek(), ballColor);
            pathDrawn.push(currentPane);

            path.setId(ball.getId());
            currentPane.getChildren().add(path);
        } else if (currentPane.getChildren().get(0) instanceof Ball) {
            Ball clickedBall = (Ball) currentPane.getChildren().get(0);
            clickedBall.setAllPathVisible(false);
            System.out.println(clickedBall);
            pathDrawn.push(currentPane);

        }
    }

    private boolean isMovePossible(Pane previousPane, Pane currentPane) {
        int prevX = Integer.parseInt(previousPane.getId().split(":")[1]);
        int prevY = Integer.parseInt(previousPane.getId().split(":")[2]);
        int currentX = Integer.parseInt(currentPane.getId().split(":")[1]);
        int currentY = Integer.parseInt(currentPane.getId().split(":")[2]);
        return (prevX == currentX + 1 && prevY == currentY) || (prevX == currentX && prevY == currentY + 1)
                || (prevX == currentX - 1 && prevY == currentY) || (prevX == currentX && prevY == currentY - 1);
    }

    private void onGridExited(MouseEvent event) {
        if (!clickedBall.isEmpty()) {
            Pane exitPane = (Pane) event.getSource();
            if (exitPane.getChildren().size() != 0) {
                Node node = exitPane.getChildren().get(0);
                if (node instanceof Ball && !clickedBall.contains(node)) {
                    Ball ball = (Ball) node;
                    ball.setBallStyle(CIRCLE_NORMAL_STYLE);
                }
            }
        }
    }

    private void onGridClicked(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        if (pane.getChildren().size() != 0) {
            Node node = pane.getChildren().get(0);
            if (node instanceof Ball) {
                Ball ball = (Ball) node;
                if (clickedBall.contains(ball)) {
                    ball.setBallStyle(CIRCLE_NORMAL_STYLE);
                    System.out.println(ball.getId());
                    clickedBall.remove(ball);
                    String ballColor = ball.getId().split(":")[1];
                    Stack<Pane> pathDrawn = colorsPath.get(ballColor);
                    pathDrawn.remove(pane);
                } else {
                    if (!clickedBall.isEmpty()) {
                        Ball oldBall = clickedBall.pop();
                        String oldBallColor = oldBall.getId().split(":")[1];
                        String ballColor = ball.getId().split(":")[1];
                        if (oldBallColor.equals(ballColor)) {
                            oldBall.setBallStyle(CIRCLE_NORMAL_STYLE);
                            ball.setBallStyle(CIRCLE_NORMAL_STYLE);
                            Stack<Pane> pathDrawn = colorsPath.get(ballColor);
                            System.out.println(pathDrawn);
                            int percentage = updatePercentage();
                            if (percentage == 100) {
                                nextButton.setDisable(false);
                            }
                            return;
                        } else {
                            oldBall.setBallStyle(CIRCLE_NORMAL_STYLE);
                        }
                    }
                    ball.setBallStyle(CIRCLE_CLICKED_STYLE);
                    while (!clickedBall.isEmpty()) {
                        Ball oldBall = clickedBall.pop();
                        oldBall.setBallStyle(CIRCLE_NORMAL_STYLE);
                        System.out.println(oldBall.getId());
                    }
                    clickedBall.push(ball);
                    String ballColor = ball.getId().split(":")[1];
                    Stack<Pane> pathDrawn = colorsPath.get(ballColor);
                    if (pathDrawn.size() != 0) {
                        removePath(ball, ballColor, pathDrawn, pane, 0);
                    }
                    if (!pathDrawn.contains(pane))
                        pathDrawn.add(pane);
                }
            } else if (node instanceof Path && clickedBall.isEmpty()) {
                System.out.println("already path drawn - " + node.getId());
                for (Ball ball : balls) {
                    if (ball.getId().equals(node.getId())) {
                        clickedBall.push(ball);
                        ball.setBallStyle(CIRCLE_CLICKED_STYLE);
                        String ballColor = ball.getId().split(":")[1];
                        Stack<Pane> pathDrawn;
                        colorsPath.get(ballColor);
                        pathDrawn = colorsPath.get(ballColor);
                        System.out.println(pathDrawn);
                        if (pathDrawn.contains(pane)) {
                            int index = pathDrawn.indexOf(pane);
                            removePath(ball, ballColor, pathDrawn, pane, index);
                        }
                        break;
                    }
                }
            } else {
                while (!clickedBall.isEmpty()) {
                    Ball oldBall = clickedBall.pop();
                    oldBall.setBallStyle(CIRCLE_NORMAL_STYLE);
                    System.out.println(oldBall.getId());
                }
            }
        }
        updatePercentage();
    }

    private int updatePercentage() {
        int count = 0;
        for (int i = 0; i < ballNames.length; i++) {
            count += colorsPath.get(ballNames[i]).size();
        }
        double totalGrids = grids.length * grids.length;
        int percentage = (int) Math.ceil((count / totalGrids) * 100);
        if (percentage != 100) {
            nextButton.setDisable(true);
        }
        pipePercentLabel.setText(Integer.toString(percentage) + " %");
        return percentage;
    }

    @FXML
    public void restartGame(ActionEvent actionEvent) {
        System.out.println("Game Restart");
        if (actionEvent.getSource().equals(restartButton)) {
            initLevel(resources);
        }
    }

    @FXML
    public void nextLevel(ActionEvent actionEvent) {
        System.out.println("Next Level");
        if (actionEvent.getSource().equals(nextButton)) {
            Main.nextLevel();
        }
    }
}
