package com.yutong.framework.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class BJsonLauncherTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane borderPane = FXMLLoader.load(getClass().getResource("/fxml/BJson.fxml"));
        primaryStage.setTitle("正则表达式");
        Scene scene222 = new Scene(borderPane);
        primaryStage.setScene(scene222);
        primaryStage.show();
    }


    /**
     * @param args
     *        the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
