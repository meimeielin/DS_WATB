package com.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements CommandLineRunner {

    private final GoogleQuery googleQuery;
    public static ArrayList<WebTree> webTrees;

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

            // Create a list to store WebTree objects
            ArrayList<WebTree> webTrees = new ArrayList<>();


            for(int i =0; i< 5; i++){
                
                String searchUrl = allUrls.get(i);
                System.out.println(i + " " +searchUrl);

                // root node
                WebPage rootPage = new WebPage(searchUrl);
                WebTree tree = new WebTree(rootPage);

                // 爬取深度
                int depth = 1;

                // 爬取子网页
                tree.crawl(depth);

                // 关键字和权重
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/input.txt");
                if (inputStream == null) {
                    throw new FileNotFoundException("Resource file 'static/input.txt' not found.");
                }
                Scanner scanner = new Scanner(inputStream);

                int numOfKeywords = scanner.nextInt();
                System.out.println("Number of keywords: " + numOfKeywords);

                ArrayList<Keyword> keywords = new ArrayList<>();
                for (int j = 0; j < numOfKeywords; j++) {
                    String name = scanner.next();
                    double weight = scanner.nextDouble();
                    Keyword k = new Keyword(name, weight);
                    keywords.add(k);
                    System.out.println(k.toString());
                }
                scanner.close();

                tree.setPostOrderScore(keywords);
                tree.sortTreeByScore();

                webTrees.add(tree);
                System.out.println("Tree " + i + " is created");

                tree.eularPrintTree();
            }

            System.out.println("Web Trees Test:");
            for (WebTree tree : webTrees) {
                System.out.println(tree);
            }

            webTrees.sort((tree1, tree2) -> Double.compare(tree2.getRoot().getNodeScore(), tree1.getRoot().getNodeScore()));

            System.out.println("Web Trees Test aftersort:");
            for (WebTree tree : webTrees) {
                System.out.println(tree + " " + tree.getRoot().getNodeScore());
            }

            /*
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

                
                tree.setPostOrderScore(keywords);
                tree.sortTreeByScore();
                tree.eularPrintTree();

            }
            */
            System.out.println(googleQuery.query());  // 輸出查詢結果

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }

    }
}

