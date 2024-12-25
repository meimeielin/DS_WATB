<<<<<<< HEAD
package com.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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

            // 調用 service 層的 scrape 方法
            return googleScraperService.scrapeGoogleResults(encodedQuery);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IOException occurred while scraping Google results.");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch Google results due to an IOException.");
            return errorResponse;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected error occurred.");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unexpected error occurred while fetching results.");
            return errorResponse;
        }
    }

    @GetMapping("/related-searches")
    public List<Map<String, String>> getRelatedSearches(@RequestParam String query) {
        try {
            String combinedQuery = query + " news";
            String encodedQuery = URLEncoder.encode(combinedQuery, StandardCharsets.UTF_8.toString());

            return googleScraperService.scrapeGoogleResultsInterest(encodedQuery);
        } catch (IOException e) {
            e.printStackTrace();
            // 返回帶有錯誤訊息的清單
            return List.of(Map.of("text", "Error occurred while fetching related searches.", "url", ""));
        } catch (Exception e) {
            e.printStackTrace();
            // 返回帶有錯誤訊息的清單
            return List.of(Map.of("text", "Unexpected error occurred.", "url", ""));
        }
    }


=======
package com.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

            // 調用 service 層的 scrape 方法
            return googleScraperService.scrapeGoogleResults(encodedQuery);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IOException occurred while scraping Google results.");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch Google results due to an IOException.");
            return errorResponse;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected error occurred.");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unexpected error occurred while fetching results.");
            return errorResponse;
        }
    }

    @GetMapping("/related-searches")
    public ResponseEntity<?> getRelatedSearches(@RequestParam String query) {
        try {
            String combinedQuery = query + " news";
            String encodedQuery = URLEncoder.encode(combinedQuery, StandardCharsets.UTF_8.toString());

            ArrayList<String> relatedSearches = googleScraperService.scrapeGoogleResultsInterest(encodedQuery);
            if (relatedSearches.isEmpty()) {
                return ResponseEntity.ok(List.of(Map.of("text", "No related searches found.", "url", "")));
            }
            return ResponseEntity.ok(relatedSearches);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch related searches due to an IOException: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error occurred: " + e.getMessage()));
        }
    }


>>>>>>> bbeaab608e781cbbca86a9f140d3b51ddd9c5f69
}