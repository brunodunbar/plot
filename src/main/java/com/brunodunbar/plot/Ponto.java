package com.brunodunbar.plot;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class Ponto extends Objeto {

//    private final Circle circle;

    public Ponto(Coordenada coordenada) {
        super(coordenada);

        Image image = new Image("plane.png");
        ImageView imageView = new ImageView(image);
        getChildren().add(imageView);

        //circle = new Circle();
        //circle.setCenterX(5);
        //circle.setCenterY(5);
        //getChildren().add(circle);
        //circle.setRadius(5);
//
//        Label label = new Label("AAA");
//        //label.setLayoutX(-10);
//        //label.setLayoutY(15);
//        getChildren().add(label);


        getStyleClass().add("ponto");
    }
}
