<<<<<<< HEAD
package com.controller;

import java.io.IOException;
import java.util.ArrayList;

public class WebPage {
	
	public String url;
	public String name;
	public WordCounter counter;
	public double score;

	public WebPage(String url) {
		this.url = url;
		this.counter = new WordCounter(url);
	}

	public void setScore(ArrayList<Keyword> keywords) throws IOException {
		score = 0;
		// YOUR TURN
		// 1. calculate the score of this webPage
		for (Keyword k : keywords) {
			score += counter.countKeyword(k.name) * k.weight;
		}

	}
}
=======
package com.controller;

import java.io.IOException;
import java.util.ArrayList;

public class WebPage {
	
	public String url;
	public String name;
	public WordCounter counter;
	public double score;

	public WebPage(String url) {
		this.url = url;
		this.counter = new WordCounter(url);
		this.score = 0;
	}

	public void setScore(ArrayList<Keyword> keywords) throws IOException {
		score = 0;
		// 1. calculate the score of this webPage
		for (Keyword k : keywords) {
			score += counter.countKeyword(k.name) * k.weight;
		}

	}

}

>>>>>>> bbeaab608e781cbbca86a9f140d3b51ddd9c5f69
