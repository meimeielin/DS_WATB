package com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;

public class JavaFXMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FinDisaster");

        // 建立 WebView
        WebView webView = new WebView();

        // 載入 index.html
        String url = getClass().getResource("/static/index.html").toExternalForm();
        webView.getEngine().load(url);

        // 將 WebView 加入場景
        StackPane root = new StackPane();
        root.getChildren().add(webView);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
