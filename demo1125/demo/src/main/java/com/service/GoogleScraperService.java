package com.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.controller.GoogleQuery;
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
        ArrayList<String> allUrls = new ArrayList<>(queryResults.values());
        
        // Calculate scores for URLs
        Map<String, Double> urlScores = new HashMap<>();
        for (String url : allUrls) {
            WebPage rootPage = new WebPage(url);
            WebTree tree = new WebTree(rootPage);
            tree.crawl(1);
            urlScores.put(url, tree.getRootNodeScore());
        }
        
        // Create sorted results based on URL scores
        LinkedHashMap<String, String> sortedResults = new LinkedHashMap<>();
        queryResults.entrySet().stream()
            .sorted((entry1, entry2) -> {
                String url1 = entry1.getValue(); // Use getValue() to get URL
                String url2 = entry2.getValue();
                double score1 = urlScores.getOrDefault(url1, 0.0);
                double score2 = urlScores.getOrDefault(url2, 0.0);
                return Double.compare(score2, score1);
            })
            .forEachOrdered(entry -> sortedResults.put(entry.getKey(), entry.getValue()));
        System.out.println("新排 "+ sortedResults);
        return sortedResults;
    }

    
    public ArrayList<String> scrapeGoogleResultsInterest(String query) throws IOException {
        googleQuery.setSearchKeyword(query);  // 每次调用时都设置新的关键词
        return googleQuery.googleRelatedSearch() ;  // 获取搜索结果
    }

    
}
