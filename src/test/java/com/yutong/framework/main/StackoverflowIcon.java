package com.yutong.framework.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class StackoverflowIcon extends Application {

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        // set icon
        stage.getIcons().add(new Image("/images/Cup_Coffee_Hot_Cafe_Beverage.ico"));
        stage.setTitle("Wow!! Stackoverflow Icon");
        stage.setScene(new Scene(root, 300, 250));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
