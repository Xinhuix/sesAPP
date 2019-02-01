package com.jfinalshop.controller.usercenter.entity;

public class ProductUtil {
	private String id;
	private String num;
	private Integer   type;
	
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}

	
	@Override
	public String toString() {
		return "ProductUtil [id=" + id + ", num=" + num + ", type=" + type + "]";
	}
	public ProductUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
}
