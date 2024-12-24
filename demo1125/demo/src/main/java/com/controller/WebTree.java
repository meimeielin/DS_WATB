package com.controller;

import java.io.IOException;
import java.util.ArrayList;

public class WebTree {
    public WebNode root;

    public WebTree(WebPage rootPage) {
        this.root = new WebNode(rootPage);
    }

    // 递归爬取子网页
    public void crawl(int depth) {
        root.crawlChildren(depth);
    }

    // 计算整棵树的分数
    public void setPostOrderScore(ArrayList<Keyword> keywords) {
        try {
			root.calculateNodeScore(keywords);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // 打印网页树
    public void eularPrintTree() {
        eularPrintTree(root, 0);
    }

    private void eularPrintTree(WebNode node, int depth) {
        for (int i = 0; i < depth; i++) System.out.print("  ");
        System.out.println(" (Score: " + node.nodeScore + ")");

        for (WebNode child : node.children) {
            eularPrintTree(child, depth + 1);
        }
    }
}
