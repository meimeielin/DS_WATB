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
		
		this.name = name;
		this.weight = weight;
	}

	@Override
	public String toString(){
		return "[" + name + "," + weight + "]";
	}
}