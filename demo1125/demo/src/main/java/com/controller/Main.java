package com.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
            googleQuery.setSearchKeyword("Disaster news");  // 默認值，如果没有傳入参数
            }

            ArrayList<String> allUrls = (ArrayList<String>) googleQuery.getAllUrls();

            if (allUrls == null || allUrls.isEmpty()) {
            System.out.println("No URLs found from Google query."); // 如果沒有找到URL，則返回
            return;
            }
            
            for (String url : allUrls) {
                System.out.println(url);
            }

            for (String searchUrl : allUrls) {
            // root node
            WebPage rootPage = new WebPage(searchUrl);
            WebTree tree = new WebTree(rootPage);

            // 爬取深度
            int depth = 1;

            // 爬取子网页
            tree.crawl(depth);

            // 关键字和权重
            File file = new File(getClass().getClassLoader().getResource("static/input.txt").getFile());
            Scanner scanner = new Scanner(file);

            int numOfKeywords = scanner.nextInt();
            System.out.println("Number of keywords: " + numOfKeywords);

            ArrayList<Keyword> keywords = new ArrayList<>();
            for (int i = 0; i < numOfKeywords; i++) {
                String name = scanner.next();
                double weight = scanner.nextDouble();
                Keyword k = new Keyword(name, weight);
                keywords.add(k);
                System.out.println(k.toString());
            }
            scanner.close();

            System.out.println(googleQuery.query());  // 輸出查詢結果
            //
            tree.setPostOrderScore(keywords);
            
            tree.eularPrintTree();

            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }

    }
}

