package com.yutong.framework.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class ApplicationLauncher extends Application {

    private static Logger logger =
            LogManager.getLogger(ApplicationLauncher.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        String encoding = System.getProperty("file.encoding");
        logger.warn("此 Java 虚拟机的默认 charset:" + encoding);

        BorderPane regexpView =
                FXMLLoader.load(getClass().getResource("/fxml/Regexp.fxml"));

        TabPane tabPane = new TabPane();
        /* 1.正则表达式 */
        Tab tab1 = new Tab("正则表达式");
        tab1.setClosable(false);

        AnchorPane anchorpane1 = new AnchorPane();
        anchorpane1.getChildren().add(regexpView);

        AnchorPane.setLeftAnchor(regexpView, 0d);
        AnchorPane.setRightAnchor(regexpView, 0d);
        AnchorPane.setBottomAnchor(regexpView, 0d);
        AnchorPane.setTopAnchor(regexpView, 0d);

        tab1.setContent(anchorpane1);

        /* 2.文件操作 */
        BorderPane fileOperView =
                FXMLLoader.load(getClass().getResource("/fxml/FileOper.fxml"));
        Tab tab2 = new Tab("文件操作");
        tab2.setClosable(false);

        AnchorPane anchorpane2 = new AnchorPane();
        anchorpane2.getChildren().add(fileOperView);

        AnchorPane.setLeftAnchor(fileOperView, 0d);
        AnchorPane.setRightAnchor(fileOperView, 0d);
        AnchorPane.setBottomAnchor(fileOperView, 0d);
        AnchorPane.setTopAnchor(fileOperView, 0d);

        tab2.setContent(anchorpane2);

        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab1);
        StackPane root = new StackPane();
        root.getChildren().add(tabPane);

        Scene scene = new Scene(root, 750, 600);
        primaryStage.getIcons()
                .add(new Image("/images/Cup_Coffee_Hot_Cafe_Beverage.ico"));
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(600);
        scene.getStylesheets().add("/styles/default.css");
        /**
         * Finalise the Stage.
         */
        primaryStage.setTitle("效率工具");
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
