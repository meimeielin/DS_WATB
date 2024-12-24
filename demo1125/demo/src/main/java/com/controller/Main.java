package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements CommandLineRunner {

    private final GoogleQuery googleQuery;

    public Main(GoogleQuery googleQuery) {
        this.googleQuery = googleQuery;
    }

    @Override
    public void run(String... args) {
        try {
            if (args.length > 0) {
                googleQuery.setSearchKeyword(args[0]);  // 使用傳入的搜索關鍵字（從命令行參數獲取）
            } else {
                googleQuery.setSearchKeyword("Disaster");  // 默認值，如果没有傳入参数
            }

            //
            for(String searchUrl : googleQuery.getAllUrls()){
                //root node
                WebPage rootPage = new WebPage(searchUrl);
                WebTree tree = new WebTree(rootPage);
                //build childnode
                Elements links = doc.select("a[href]");
                links.forEach(link -> System.out.println(link.attr("abs:href")));

                tree.root.addChild(new WebNode(new WebPage("http://soslab.nccu.edu.tw/Publications.html")));
            }
            //
            System.out.println(googleQuery.query());  // 輸出查詢結果
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    	public void main(String[] args) throws IOException{

		//root node
		WebPage rootPage = new WebPage("http://soslab.nccu.edu.tw/Welcome.html", "Soslab");		
		WebTree tree = new WebTree(rootPage);
		//build childnode
		tree.root.addChild(new WebNode(new WebPage("http://soslab.nccu.edu.tw/Publications.html","Publication")));
		tree.root.addChild(new WebNode(new WebPage("http://soslab.nccu.edu.tw/Projects.html","Projects")));
		tree.root.children.get(1).addChild(new WebNode(new WebPage("https://vlab.cs.ucsb.edu/stranger/", "Stranger")));
		tree.root.addChild(new WebNode(new WebPage("http://soslab.nccu.edu.tw/Members.html", "MEMBER")));
		tree.root.addChild(new WebNode(new WebPage("http://soslab.nccu.edu.tw/Courses.html","Course")));
		// This website has something wrong, ignore it.
		
		Scanner scanner = new Scanner(System.in);

		while (scanner.hasNextLine()){
			int numOfKeywords = scanner.nextInt();
			ArrayList<Keyword> keywords = new ArrayList<Keyword>();

			for (int i = 0; i < numOfKeywords; i++){
				String name = scanner.next();
				double weight = scanner.nextDouble();
				Keyword k = new Keyword(name, weight);
				keywords.add(k);
			}

			tree.setPostOrderScore(keywords);
			tree.eularPrintTree();
		}
		scanner.close();
	}
}


