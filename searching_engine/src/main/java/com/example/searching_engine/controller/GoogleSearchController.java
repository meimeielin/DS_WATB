package com.example.searching_engine.controller;

import com.example.searching_engine.service.GoogleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class GoogleSearchController {

    @Autowired
    private GoogleQuery googleQuery;

    @GetMapping("/searching")
    public String getSearchPage() {
        return "index"; // 返回 index.html
    }

    @GetMapping("/searching/results")
    public String getSearchResults(@RequestParam("word") String word, Model model) {
        try {
            HashMap<String, String> results = googleQuery.query(word);
            model.addAttribute("results", results);
            model.addAttribute("keyword", word);
        } catch (IOException e) {
            model.addAttribute("error", "無法取得搜尋結果，請稍後再試。");
        }
        return "index"; // 返回 index.html 並附帶結果
    }
}
