package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.driver.WebDriverSetup;

@Component
public class GoogleQuery {
    private String searchKeyword;
    private String url;
    private int keywordCount;

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        keywordCount = searchKeyword.split("\\s+").length;

        // 測試用步驟
        System.out.println("Search keyword: " + searchKeyword);
        System.out.println("Number of keywords: " + keywordCount);

        try {
            // 只對原始關鍵字進行 URL 編碼，避免雙重編碼（這行好像會導致雙重編碼）
            //String encodeKeyword = java.net.URLEncoder.encode(searchKeyword, "UTF-8");
            //限制英文搜尋結果
            this.url = "https://www.google.com/search?q=" + searchKeyword + "&oe=utf8&num=20&lr=lang_en";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Generated URL: " + url);
    }

    private String fetchContent() throws IOException {
        System.out.println("fetchContent1");
        System.out.println(url);

        if (url == null || url.isEmpty()) {
            throw new IllegalStateException("URL is null or empty. Did you forget to call setSearchKeyword()?");
        }

        StringBuilder retVal = new StringBuilder();
        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        conn.setRequestProperty("User-agent", "Chrome/107.0.5304.107");

        // 獲取 Content-Type 頭並提取 charset
        String contentType = conn.getHeaderField("Content-Type");
        String charset = "UTF-8";  // 默認設置為 UTF-8

        if (contentType != null && contentType.contains("charset=")) {
            charset = contentType.split("charset=")[1];
            System.out.println("Charset found in Content-Type: " + charset);
        }

        // 使用從 Content-Type 中提取的 charset 進行解碼
        InputStream in = conn.getInputStream();
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(in, charset))) {
            String line;
            while ((line = bufReader.readLine()) != null) {
                retVal.append(line);
            }
        }

        return retVal.toString();
    }

    // 獲取搜尋結果的標題與連結
    public HashMap<String, String> query() throws IOException {
        String content = fetchContent();
        HashMap<String, String> retVal = new HashMap<>();
        Document doc = Jsoup.parse(content);

        // Google 搜索結果結構可能會發生變化
        Elements searchResults = doc.select("a:has(h3)");

        for (Element result : searchResults) {
            try {
                String title = result.select("h3").text();
                String citeUrl = result.attr("href");
                if (!title.isEmpty() && citeUrl.startsWith("/url?q=")) {
                    citeUrl = citeUrl.substring(7).split("&")[0]; // 去除多餘參數
                    retVal.put(title, citeUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return retVal;
    }

    public List<String> getAllUrls() throws IOException {
        HashMap<String, String> resultMap = this.query();
        return new ArrayList<>(resultMap.values());
    }

    public ArrayList<String> googleRelatedSearch() {
        ArrayList<String> relatedSearchResult = new ArrayList<>();
        WebDriver driver = WebDriverSetup.createDriver();

        try {
            driver.get(url);
            Thread.sleep(3000);

            // 找到「其他人也搜尋了」的關鍵字元素
            List<WebElement> keywordElements = driver.findElements(By.xpath("//span[@class='dg6jd']"));

            for (WebElement element : keywordElements) {
                String text = element.getText();
                relatedSearchResult.add(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return relatedSearchResult;
    }

    public int getKeywordCount() {
        return keywordCount;
    }
}
