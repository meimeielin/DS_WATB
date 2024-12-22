package com.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.GoogleScraperService;

@RestController
@RequestMapping("/api/google")
@CrossOrigin(origins = "http://localhost:8080")
public class GoogleScraperController {

    @Autowired
    private GoogleScraperService googleScraperService;

    /**
     * 搜索 Google 並返回標題和連結的 Map
     *
     * @param query 查詢關鍵字
     * @return 標題到連結的映射
     */
    @GetMapping("/search")
    public Map<String, String> searchGoogle(@RequestParam String query) {
        try {
            // 對查詢關鍵字 URL 編碼
            String combinedQuery = query + " news";
            String encodedQuery = URLEncoder.encode(combinedQuery, StandardCharsets.UTF_8.toString());

            // 调用 service 层的 scrape 方法
            return googleScraperService.scrapeGoogleResults(encodedQuery);
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error occurred while scraping Google results.");
            return errorResponse;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid query parameter.");
            return errorResponse;
        }
    }
}