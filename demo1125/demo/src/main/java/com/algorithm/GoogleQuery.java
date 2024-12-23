package com.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class GoogleQuery {
    private String searchKeyword;
    private String url;

    // 这里的 setSearchKeyword 需要动态更新搜索关键词
    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        try {
            String encodeKeyword = java.net.URLEncoder.encode(searchKeyword, "utf-8");
            this.url = "https://www.google.com/search?q=" + encodeKeyword + "&oe=utf8&num=20";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String fetchContent() throws IOException {
        StringBuilder retVal = new StringBuilder();

        URL u = new URL(url);
        URLConnection conn = u.openConnection();
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

    public HashMap<String, String> query() throws IOException {
        // 每次查询前都需要获取新的内容
        String content = fetchContent();

        HashMap<String, String> retVal = new HashMap<>();
        Document doc = Jsoup.parse(content);

        // Google 的搜索结果结构可能会发生变化
        Elements searchResults = doc.select("a:has(h3)"); // 选择包含 <h3> 的链接

        for (Element result : searchResults) {
            try {
                String title = result.select("h3").text(); // 获取标题
                String citeUrl = result.attr("href");      // 获取链接

                if (!title.isEmpty() && citeUrl.startsWith("/url?q=")) {
                    citeUrl = citeUrl.substring(7).split("&")[0]; // 去除多余的参数
                    retVal.put(title, citeUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return retVal;
    }

    public List<Map<String, String>> queryInterest() throws IOException {
        // 每次查询前都需要获取新的内容
        String content = fetchContent();

        List<Map<String, String>> relatedSearches = new ArrayList<>();
        Document doc = Jsoup.parse(content);

        // Google 的搜索结果结构可能会发生变化
        Elements relatedSearchElements = doc.select(".b2Rnsc .dg6jd"); 

        for (Element result : relatedSearchElements) {
            try {
                String text = result.text();
                Element parent = result.closest("a"); // 找到包裹的超連結
                String href = (parent != null) ? parent.attr("href") : "";
                
                if (!text.isEmpty() && href.startsWith("/search?")) {
                    Map<String, String> searchItem = new HashMap<>();
                    searchItem.put("text", text);
                    searchItem.put("url", href);
                    relatedSearches.add(searchItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return relatedSearches;
    }
}
