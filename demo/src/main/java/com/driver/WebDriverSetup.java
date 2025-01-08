package com.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverSetup {
    public static WebDriver createDriver() {
        // 使用 WebDriverManager 自動下載並配置 chromedriver
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }
}
