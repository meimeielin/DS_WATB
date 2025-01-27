package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class MyMainClass {  // 类名可以随意
    public static void main(String[] args) {
        SpringApplication.run(MyMainClass.class, args);
        new Thread(() -> Application.launch(JavaFXMain.class, args)).start();
    }
}
