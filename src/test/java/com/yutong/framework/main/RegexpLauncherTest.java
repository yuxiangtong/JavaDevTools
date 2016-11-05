package com.yutong.framework.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class RegexpLauncherTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root222 =
                FXMLLoader.load(getClass().getResource("/RegexpTest.fxml"));
        primaryStage.setTitle("正则表达式");
        Scene scene222 = new Scene(root222);
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
