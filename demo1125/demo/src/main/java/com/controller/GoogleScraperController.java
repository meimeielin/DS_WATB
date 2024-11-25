package com.controller;

import com.service.GoogleScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/google")
@CrossOrigin(origins = "http://localhost:8080")
public class GoogleScraperController {

    @Autowired
    private GoogleScraperService googleScraperService;

    /**
     * 搜索 Google 并返回标题和链接的 Map
     *
     * @param query 查询关键词
     * @return 标题到链接的映射
     */
    @GetMapping("/search")
    public Map<String, String> searchGoogle(@RequestParam String query) {
        try {
            // 对查询关键词进行 URL 编码
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());

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