package com.algorithm;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements CommandLineRunner {

    private final GoogleQuery googleQuery;

    public Main(GoogleQuery googleQuery) {
        this.googleQuery = googleQuery;
    }

    @Override
    public void run(String... args) {
        try {
            if (args.length > 0) {
                googleQuery.setSearchKeyword(args[0]);  // 使用傳入的搜索關鍵字（從命令行參數獲取）
            } else {
                googleQuery.setSearchKeyword("Tomato");  // 默認值，如果没有傳入参数
            }
            System.out.println(googleQuery.query());  // 輸出查詢結果
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


