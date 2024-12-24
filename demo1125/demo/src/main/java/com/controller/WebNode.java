package com.controller;

import java.io.IOException;
import java.util.ArrayList;

import java.io.IOException;
import java.util.ArrayList;
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

    // 递归爬取子网页
    public void crawlChildren(int depth) {
        if (depth <= 0) return;

        try {
            Document doc = Jsoup.connect(webPage.url).get();
            Elements links = doc.select("a[href]");

            for (var link : links) {
                String childUrl = link.attr("abs:href");
                if (childUrl.isEmpty() || !childUrl.startsWith("http")) continue;

                String title = link.text();
                WebNode childNode = new WebNode(new WebPage(childUrl));
                this.addChild(childNode);

                // 递归爬取
                childNode.crawlChildren(depth - 1);
            }
        } catch (IOException e) {
            System.out.println("Failed to crawl URL: " + webPage.url);
        }
    }

    // 计算当前节点的总分
    public void calculateNodeScore(ArrayList<Keyword> keywords) throws IOException {
        webPage.setScore(keywords);
        this.nodeScore = webPage.score;

        for (WebNode child : children) {
            child.calculateNodeScore(keywords);
            this.nodeScore += child.nodeScore;
        }
    }
}

