<<<<<<< HEAD
package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebNode {
    public WebPage webPage;
    public ArrayList<WebNode> children;
    public double nodeScore;

    public WebNode(WebPage webPage) {
        this.webPage = webPage;
        this.children = new ArrayList<>();
    }

    public void addChild(WebNode child) {
        this.children.add(child);
    }

    // 遞歸爬取子網頁
    public void crawlChildren(int depth, HashSet<String> visitedUrls) {
        if (depth <= 0) return;

        try {
            Document doc = Jsoup.connect(webPage.url).get();
            Elements links = doc.select("a[href]");

            for (var link : links) {
                String childUrl = link.attr("abs:href");
                if (childUrl.isEmpty() || !childUrl.startsWith("http") || visitedUrls.contains(childUrl)) continue;

                visitedUrls.add(childUrl);
                String title = link.text();
                WebNode childNode = new WebNode(new WebPage(childUrl));
                this.addChild(childNode);

                // 遞歸爬取
                childNode.crawlChildren(depth - 1, visitedUrls);
            }
        } catch (IOException e) {
            
            System.out.println("Failed to crawl URL: " + webPage.url);
            this.nodeScore = 0; // 如果無法設置分數，則設置為0
        }
    }

    //計算當前節點的總分
    public void calculateNodeScore(ArrayList<Keyword> keywords) throws IOException {
        webPage.setScore(keywords);
        this.nodeScore = webPage.score;

        System.out.println("Node (Score: " + this.nodeScore + ")");

        if (children != null) {
            for (WebNode child : children) {
                child.calculateNodeScore(keywords);
                this.nodeScore += child.nodeScore;
            }
        }

    }
}

=======
package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebNode {
    public WebPage webPage;
    public ArrayList<WebNode> children;
    public double nodeScore;

    public WebNode(WebPage webPage) {
        this.webPage = webPage;
        this.children = new ArrayList<>();
    }

    public void addChild(WebNode child) {
        this.children.add(child);
    }

    // 遞歸爬取子網頁
    public void crawlChildren(int depth, HashSet<String> visitedUrls) {
        if (depth <= 0) return;

        try {
            Document doc = Jsoup.connect(webPage.url).get();
            Elements links = doc.select("a[href]");

            for (var link : links) {
                String childUrl = link.attr("abs:href");
                if (childUrl.isEmpty() || !childUrl.startsWith("http") || visitedUrls.contains(childUrl)) continue;

                visitedUrls.add(childUrl);
                String title = link.text();
                WebNode childNode = new WebNode(new WebPage(childUrl));
                this.addChild(childNode);

                // 遞歸爬取
                childNode.crawlChildren(depth - 1, visitedUrls);
            }
        } catch (IOException e) {
            
            System.out.println("Failed to crawl URL: " + webPage.url);
            this.nodeScore = 0; // 如果無法設置分數，則設置為0
        }
    }

    //計算當前節點的總分
    public void calculateNodeScore(ArrayList<Keyword> keywords) throws IOException {
        webPage.setScore(keywords);
        this.nodeScore = webPage.score;

        if (children != null) {
            for (WebNode child : children) {
                child.calculateNodeScore(keywords);
                this.nodeScore += child.nodeScore;
            }
        }

    }
}

>>>>>>> bbeaab608e781cbbca86a9f140d3b51ddd9c5f69
