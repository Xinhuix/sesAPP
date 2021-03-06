package com.jfinalshop.model;

import com.jfinalshop.model.base.BaseClientAd;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class ClientAd extends BaseClientAd<ClientAd> {
	
	
	public enum Status{
		/**
		 * oridinal 0 禁止 1 使用
		 */
		  Forbidden,Use
	}
	public enum Upper{
		   normal(0,"不置顶"),upper(1,"置顶");
		   
		   private int index;
		private String  label;
		Upper(int dex,String lb){
			index=dex;
			label=lb;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		
	}
	public enum Type{

			mainbanner(1,"主页banner"),event(2,"活动"),storeBanner(3,"店铺首页banner"),ArticleBanner(5,"资讯页banner"),
			chosenStoreBanner(6,"精选商家banner"),chosenGroomBanner(7,"精选推荐banner"),chosenGoodBanner(8,"精选现货/定制banner"),
			allGoodBanner(9,"所有产品banner"),agriculturalBanner(10,"商家入驻banner"),marketBanner(11,"线下门店banner"),customBanner(12,"私人订制banner");
			
		private int index;
		private String value;
		 Type(int index,String value) {
			 this.index=index;
			 this.value=value;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	
		
	}
	
	public static final ClientAd dao = new ClientAd().dao();
	
	public Type[] gettype() {
		return Type.values();
	}
	public  String getvalue(int index) {
		String value="";
		for(Type t:Type.values()) {
			if(t.index==index) {
			  value=t.value;
			  break;
			}
		}
		return value;
	}
}
