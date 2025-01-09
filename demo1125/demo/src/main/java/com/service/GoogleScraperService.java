package com.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.controller.GoogleQuery;
import com.controller.Keyword;
import com.controller.WebPage;
import com.controller.WebTree;

@Service
public class GoogleScraperService {
    private final GoogleQuery googleQuery; 
    public GoogleScraperService(GoogleQuery googleQuery) {
        this.googleQuery = googleQuery;
    }

    public HashMap<String, String> scrapeGoogleResults(String query) throws IOException {
       googleQuery.setSearchKeyword(query);  // 每次调用时都设置新的关键词
        HashMap<String, String> queryResults = googleQuery.query();
        ArrayList<String> allUrls = (ArrayList<String>) googleQuery.getAllUrls();
        
        //test
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

            //樹排序完後的結果
            System.out.println("Web Trees Test aftersort:");
            for (WebTree tree : webTrees) {
                System.out.println(tree + " " + tree.getRoot().getNodeScore());
            }

            //將樹轉成 Map 的格式
            // Create a map to store URL scores
            Map<String, Double> urlScores = new HashMap<>();
            System.out.println("\n" + "Sorted Tree into Map type：");

            // Iterate through webTrees and populate urlScores
            for (WebTree tree : webTrees) {
                String url = tree.getRoot().getWebPage().getUrl(); // 获取根节点的 URL
                double score = tree.getRoot().getNodeScore(); // 获取根节点的得分
                urlScores.put(url, score); // 将 URL 和分数存入 Map
                System.out.println("URL: " + url + ", Score: " + score);
            }

            // 输出 urlScores Map
            System.out.println("\n" + "URL Content Scores:");
            for (Map.Entry<String, Double> entry : urlScores.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }


        // 获取从文件加载的关键字，并为每个网站名称计算匹配分数
        File file = new File(getClass().getClassLoader().getResource("static/input.txt").getFile());
        Scanner scanner = new Scanner(file);
        int numOfKeywords = scanner.nextInt();
        ArrayList<Keyword> keywords = new ArrayList<>();
        for (int j = 0; j < numOfKeywords; j++) {
            String name = scanner.next();
            double weight = scanner.nextDouble();
            Keyword k = new Keyword(name, weight);
            keywords.add(k);
        }
        scanner.close();

        // 对每个 URL 计算基于关键字匹配的额外分数
        for (Map.Entry<String, String> entry : queryResults.entrySet()) {
            String siteName = entry.getKey().toUpperCase();
            double keywordScore = 0.0;
            // 对每个网站名称进行关键字匹配，计算额外的得分
            for (Keyword keyword : keywords) {
                if (siteName.contains(keyword.getName().toUpperCase())) {  // 检查网站名称是否包含关键字
                    keywordScore += keyword.getWeight() * 10000;  // 累加关键字权重
                }
            }

            // 将关键字匹配分数与 WebTree 分数相加
            String url = entry.getValue();
            double currentScore = urlScores.getOrDefault(url, 0.0);
            urlScores.put(url, currentScore + keywordScore);  // 更新总得分
        }

        // 输出 urlScores Map
        System.out.println("\n" + "Final URL Scores (SiteNameScore + ContentScore):");
        for (Map.Entry<String, Double> entry : urlScores.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

            // 排序逻辑
        List<Map.Entry<String, String>> sortedQueryResults = new ArrayList<>(queryResults.entrySet());

        sortedQueryResults.sort((entry1, entry2) -> {
            String url1 = entry1.getValue();
            String url2 = entry2.getValue();
            Double score1 = urlScores.getOrDefault(url1, 0.0);
            Double score2 = urlScores.getOrDefault(url2, 0.0);
            return Double.compare(score2, score1); // 降序排列
        });

        // 重新构建排序后的 queryResults
        LinkedHashMap<String, String> sortedResults = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : sortedQueryResults) {
            sortedResults.put(entry.getKey(), entry.getValue());
        }

        // 输出结果
        System.out.println("\n" + "Sorted Query Results:");
        for (Map.Entry<String, String> entry : sortedResults.entrySet()) {
            System.out.println("Name: " + entry.getKey() + ", URL: " + entry.getValue() + 
                               ", Score: " + urlScores.get(entry.getValue()));
        }
        
        return sortedResults;
    }

    
    public ArrayList<String> scrapeGoogleResultsInterest(String query) throws IOException {
        googleQuery.setSearchKeyword(query);  // 每次调用时都设置新的关键词
        return googleQuery.googleRelatedSearch() ;  // 获取搜索结果
    }

    
}
