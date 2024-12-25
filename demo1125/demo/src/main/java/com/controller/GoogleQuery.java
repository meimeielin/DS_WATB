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

    // 這裏的 setSearchKeyword 需要動態更新搜索關鍵詞
    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        keywordCount = searchKeyword.split("\\s+").length;
        //測試用step1
        System.out.print("Search keyword: " + searchKeyword);
        System.out.println("Number of keywords: " + keywordCount);
        try {
            String encodeKeyword = java.net.URLEncoder.encode(searchKeyword, "utf-8");
            this.url = "https://www.google.com/search?q=" + encodeKeyword + "&oe=utf8&num=20";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //測試用step2
        System.out.println(url);
    }

    private String fetchContent() throws IOException {
        //測試用step5
        System.out.println("fetchContent1");

        System.out.println(url);

        if (url == null || url.isEmpty()) {
        throw new IllegalStateException("URL is null or empty. Did you forget to call setSearchKeyword()?");
        }

        StringBuilder retVal = new StringBuilder();
        System.out.println("fetchContent2");
        URL u = new URL(url);
        System.out.println("fetchContent3");
        URLConnection conn = u.openConnection();
        System.out.println("fetchContent4");
        conn.setRequestProperty("User-agent", "Chrome/107.0.5304.107");
        InputStream in = conn.getInputStream();

        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(in, "utf-8"))) {
            String line;
            while ((line = bufReader.readLine()) != null) {
                retVal.append(line);
            }
        }

        return retVal.toString();
    }

    //得到搜尋結果的標題與連結
    public HashMap<String, String> query() throws IOException {
        // 每次查詢前都需要獲取新的内容
        //測試用step4
        System.out.println("query1");
        String content = fetchContent();

        System.out.println("query2");
        HashMap<String, String> retVal = new HashMap<>();
        System.out.println(retVal);

        System.out.println("query3");
        Document doc = Jsoup.parse(content);

        System.out.println("query4");

        // Google 的搜索结果結構可能會發生變化
        Elements searchResults = doc.select("a:has(h3)"); // 選擇包含 <h3> 的連結

        for (Element result : searchResults) {
            try {
                String title = result.select("h3").text(); // 獲取標題
                String citeUrl = result.attr("href");      // 獲取連結

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

    //取得搜尋結果Url的List
    public List<String> getAllUrls() throws IOException {
        //測試用step3
        System.out.println("1");
        System.out.println(this.url);
        System.out.println("2");
        HashMap<String, String> resultMap = this.query();
        System.out.println("end");
        
        List<String> urls = new ArrayList<>();
        int count = 0;

        for (String value : resultMap.values()) {
            if (count >= 5) break; // 確保只添加前 5 個
            urls.add(value);
            count++;
        }

        for (String url : urls) {
            System.out.println(url);
        }
        return urls;
        
    }

    public ArrayList<String> googleRelatedSearch() {
        ArrayList<String> relatedSearchResult = new ArrayList<>();
        WebDriver driver = WebDriverSetup.createDriver();

        try {
            driver.get(url);

            // 等待頁面加載完成（可以改用 WebDriverWait）
            Thread.sleep(3000);

            // 找到「其他人也搜尋了」的關鍵字元素
            List<WebElement> keywordElements = driver.findElements(By.xpath("//span[@class='dg6jd']"));

            // 提取關鍵字文字
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
