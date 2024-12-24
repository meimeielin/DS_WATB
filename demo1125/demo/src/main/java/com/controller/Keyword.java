package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

public class Keyword{
	public String name;
	public double weight;

	public Keyword(String name, double weight){
		
<<<<<<< Updated upstream
=======
		if (name.equals("地震") || name.equals("颱風") || name.equals("水災") || name.equals("豪雨") || name.equals("台灣") ||
			 name.equals("日本") || name.equals("風災") ||name.equals("暴風雪") ||name.equals("earthquake") || name.equals("typhoon") || name.equals("flood") 
			 || name.equals("heavy rain") || name.equals("Taiwan") || name.equals("Japan") || name.equals("windstorm") 
			 || name.equals("natural disaster")||name.equals("disaster")||name.equals("disasters")) {
			
			this.weight = 1.0;

		} else if (name.equals("氣候") || name.equals("預測") || name.equals("地區") || name.equals("影響") || 
			name.equals("climate") || name.equals("forecast") || name.equals("region") || name.equals("impact")) {
			
			this.weight = 0.5;

		} else if (name.equals("政府") || name.equals("措施") || name.equals("government") || name.equals("measures")||name.equals("政策")
			||name.equals("policy")|| name.equals("natural")) {
			
			
			this.weight = 0.3;
			
		} else{
			this.weight = 0.1;
		}
		
>>>>>>> Stashed changes
		this.name = name;
		this.weight = weight;
	}

	@Override
	public String toString(){
		return "[" + name + "," + weight + "]";
	}
}