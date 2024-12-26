package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class WebTree {
    public WebNode root;

    public WebTree(WebPage rootPage) {
        this.root = new WebNode(rootPage);
    }

    // 遞歸爬取子網頁
    public void crawl(int depth) {
    HashSet<String> visitedUrls = new HashSet<>();
    root.crawlChildren(depth, visitedUrls);
    }

    
    // 計算整棵樹的分數
    public void setPostOrderScore(ArrayList<Keyword> keywords) {
    try {
        root.calculateNodeScore(keywords);
    } catch (IOException e) {
        System.err.println("Error calculating node score: " + e.getMessage());
        // 可以考慮繼續處理，或者重新拋出異常
    }
    }

    // 根據分數重新排序子節點
    public void sortTreeByScore() {
        sortTreeByScore(root);
    }

    private void sortTreeByScore(WebNode node) {
        //
        System.out.println("sortTreeByScore");
        if (node.children != null && !node.children.isEmpty()) {
            node.children.sort((n1, n2) -> Double.compare(n2.nodeScore, n1.nodeScore));
            for (WebNode child : node.children) {
                sortTreeByScore(child);
            }
        }
    }

    // 打印网页树
    public void eularPrintTree() {
        System.out.println("Web Tree:");  // 打印樹的根節點
        eularPrintTree(root, 0);
    }

    private void eularPrintTree(WebNode node, int depth) {
        for (int i = 0; i < depth; i++) System.out.print("  ");
        System.out.println("Node (Score: " + node.nodeScore + ")");

        for (WebNode child : node.children) {
            eularPrintTree(child, depth + 1);
        }
    }

    public WebNode getRoot() {
        return root;
    }
        //取得父網頁總值
        public double getRootNodeScore() {
            if (root != null) {
                return root.nodeScore;
            }
            return 0.0; // 如果根节点为 null，返回 0.0 或其他默认值
        }
}

