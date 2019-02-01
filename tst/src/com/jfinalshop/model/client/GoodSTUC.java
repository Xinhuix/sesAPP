package com.jfinalshop.model.client;

import java.util.ArrayList;
import java.util.List;

import com.jfinalshop.model.Goods;
import com.jfinalshop.model.Review;

public class GoodSTUC {

	private List<Goods> lg=new ArrayList<>();
	private List<Review> re=new ArrayList<>();
	
	public List<Goods> getLg() {
		return lg;
	}
	public void setLg(List<Goods> lg) {
		this.lg = lg;
	}
	public List<Review> getRe() {
		return re;
	}
	public void setRe(List<Review> re) {
		this.re = re;
	}
	
	
}
