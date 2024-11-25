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
                googleQuery.setSearchKeyword(args[0]);  // 使用传入的搜索关键字（从命令行参数获取）
            } else {
                googleQuery.setSearchKeyword("Tomato");  // 默认值，如果没有传入参数
            }
            System.out.println(googleQuery.query());  // 输出查询结果
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


