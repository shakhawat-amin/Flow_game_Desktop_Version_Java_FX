package com.betelgeuse.flow.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

/**
 * Created by User on 8/27/2016.
 */
public class Path extends Pane {

    protected static final String TOP_RECTANGLE = "topRectangle";
    protected static final String BOTTOM_RECTANGLE = "bottomRectangle";
    protected static final String LEFT_RECTANGLE = "leftRectangle";
    protected static final String RIGHT_RECTANGLE = "rightRectangle";

    protected Pane pane;
    protected Rectangle topRectangle;
    protected Rectangle bottomRectangle;
    protected Rectangle leftRectangle;
    protected Rectangle rightRectangle;
    private String rectVisible;

    protected Path(String path) {
        try {
            pane = FXMLLoader.load(getClass().getResource(path));
            for (Node node : pane.getChildren()) {
                switch (node.getId()) {
                    case TOP_RECTANGLE:
                        topRectangle = (Rectangle) node;
                        break;
                    case BOTTOM_RECTANGLE:
                        bottomRectangle = (Rectangle) node;
                        break;
                    case LEFT_RECTANGLE:
                        leftRectangle = (Rectangle) node;
                        break;
                    case RIGHT_RECTANGLE:
                        rightRectangle = (Rectangle) node;
                        break;
                }
            }
            topRectangle.setVisible(false);
            rightRectangle.setVisible(false);
            leftRectangle.setVisible(false);
            bottomRectangle.setVisible(false);
            this.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path(Pane currentPath, Pane previousPath, String color) {
        this("/path.fxml");
        System.out.println(getPosition(previousPath, currentPath));
        switch (getPosition(previousPath, currentPath)) {
            case TOP_RECTANGLE:
                topRectangle.setVisible(true);
                topRectangle.setFill(Paint.valueOf(color));
                previousPathRectVisible(TOP_RECTANGLE,previousPath, color);
                break;
            case BOTTOM_RECTANGLE:
                bottomRectangle.setVisible(true);
                bottomRectangle.setFill(Paint.valueOf(color));
                previousPathRectVisible(BOTTOM_RECTANGLE,previousPath, color);
                break;
            case LEFT_RECTANGLE:
                leftRectangle.setVisible(true);
                leftRectangle.setFill(Paint.valueOf(color));
                previousPathRectVisible(LEFT_RECTANGLE,previousPath, color);
                break;
            case RIGHT_RECTANGLE:
                rightRectangle.setVisible(true);
                rightRectangle.setFill(Paint.valueOf(color));
                previousPathRectVisible(RIGHT_RECTANGLE,previousPath, color);
                break;
        }
    }

    protected void previousPathRectVisible(String rectangleId, Pane previousPath, String color) {
        Node node;
        node = previousPath.getChildren().get(0);
        if (node instanceof Ball) {
            Ball ball = (Ball) node;
            ball.setRectVisible(rectangleId, color);
        } else if (node instanceof Path) {
            Path path = (Path) node;
            path.setRectVisible(rectangleId, color);
        }
    }

    protected String getPosition(Pane previousPane, Pane currentPane) {
        int prevX = Integer.parseInt(previousPane.getId().split(":")[1]);
        int prevY = Integer.parseInt(previousPane.getId().split(":")[2]);
        int currentX = Integer.parseInt(currentPane.getId().split(":")[1]);
        int currentY = Integer.parseInt(currentPane.getId().split(":")[2]);
        System.out.println("prevX - " + prevX + ", prevY - " + prevY);
        System.out.println("currentX - " + currentX + ", currentY - " + currentY);

        if (prevX == currentX + 1 && prevY == currentY) {
            return RIGHT_RECTANGLE;
        } else if (prevX == currentX && prevY == currentY + 1) {
            return BOTTOM_RECTANGLE;
        } else if (prevX == currentX - 1 && prevY == currentY) {
            return LEFT_RECTANGLE;
        } else if (prevX == currentX && prevY == currentY - 1) {
            return TOP_RECTANGLE;
        }
        return "None";
    }

    public void setRectVisible(final String rectVisible, String color) {
        System.out.println(rectVisible);
        switch (rectVisible) {
            case TOP_RECTANGLE:
                bottomRectangle.setVisible(true);
                bottomRectangle.setFill(Paint.valueOf(color));
                break;
            case BOTTOM_RECTANGLE:
                topRectangle.setVisible(true);
                topRectangle.setFill(Paint.valueOf(color));
                break;
            case LEFT_RECTANGLE:
                rightRectangle.setVisible(true);
                rightRectangle.setFill(Paint.valueOf(color));
                break;
            case RIGHT_RECTANGLE:
                leftRectangle.setVisible(true);
                leftRectangle.setFill(Paint.valueOf(color));
                break;
        }
    }

    public void setAllPathVisible(boolean allPathVisible) {
        topRectangle.setVisible(false);
        bottomRectangle.setVisible(false);
        leftRectangle.setVisible(false);
        rightRectangle.setVisible(false);

    }
}
