package com.yutong.framework.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ApplicationLauncher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root =
                FXMLLoader.load(getClass().getResource("/fxml/Regexp.fxml"));

        /**
         * Finalise the Stage.
         */
        primaryStage.setTitle("正则表达式");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
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
