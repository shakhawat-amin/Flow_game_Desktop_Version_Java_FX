package com.betelgeuse.flow.ui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Created by User on 8/27/2016.
 */
public class Ball extends Path {

    protected static final String MIDDLE_CIRCLE = "middleCircle";
    protected Circle middleCircle;

    public Ball(String ballColor) {
        super("/ball.fxml");
        System.out.println(ballColor);
        for (Node node : pane.getChildren()) {
            switch (node.getId()) {
                case MIDDLE_CIRCLE:
                    middleCircle = (Circle) node;
                    middleCircle.setFill(Paint.valueOf(ballColor));
                    middleCircle.setVisible(true);
                    System.out.println("middle");
                    break;
            }
        }
    }

    public void setBallStyle(String ballStyle) {
        middleCircle.setStyle(ballStyle);
    }


    public void setRectVisible(Pane currentPath, Pane previousPath, String color) {
        switch (getPosition(currentPath, previousPath)) {
            case TOP_RECTANGLE:
                bottomRectangle.setVisible(true);
                bottomRectangle.setFill(Paint.valueOf(color));
                previousPathRectVisible(BOTTOM_RECTANGLE, previousPath, color);
                break;
            case BOTTOM_RECTANGLE:
                topRectangle.setVisible(true);
                topRectangle.setFill(Paint.valueOf(color));
                previousPathRectVisible(TOP_RECTANGLE, previousPath, color);
                break;
            case LEFT_RECTANGLE:
                rightRectangle.setVisible(true);
                rightRectangle.setFill(Paint.valueOf(color));
                previousPathRectVisible(RIGHT_RECTANGLE, previousPath, color);
                break;
            case RIGHT_RECTANGLE:
                leftRectangle.setVisible(true);
                leftRectangle.setFill(Paint.valueOf(color));
                previousPathRectVisible(LEFT_RECTANGLE, previousPath, color);
                break;

        }
    }


}
