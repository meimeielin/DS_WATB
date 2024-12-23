package com.service;

import com.controller.GoogleQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
public class GoogleScraperService {
    private final GoogleQuery googleQuery;

    public GoogleScraperService(GoogleQuery googleQuery) {
        this.googleQuery = googleQuery;
    }

    public HashMap<String, String> scrapeGoogleResults(String query) throws IOException {
        googleQuery.setSearchKeyword(query);  // 每次调用时都设置新的关键词
        return googleQuery.query();  // 获取搜索结果
    }
}

