package com.controller;

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

	public String getName(){
		return name;
	}

	public Double getWeight(){
		return weight;
	}

	@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			Keyword keyword = (Keyword) obj;
			return name.equals(keyword.name);
	}

	@Override
		public int hashCode() {
			return name.hashCode();
	}

}