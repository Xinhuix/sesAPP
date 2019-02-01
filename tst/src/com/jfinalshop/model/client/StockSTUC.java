package com.jfinalshop.model.client;

import java.util.ArrayList;
import java.util.List;

import com.jfinalshop.model.ClientAd;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.ProductCategory;

public class StockSTUC {
	
	private List<ClientAd> ad=new ArrayList<>();	
	private List<PriceGape> lp=new ArrayList<>();
	private List<Goods> lg=new ArrayList<>();
	private List<ProductCategory> dt=new ArrayList<>();
	
	private int status;
		
	public enum Status{
		 Success(1),Fail(2),Error(3),LackPara(4),ParaWrong(5),Exist(6),NoExist(7),Illegal(8),OutTime(9),CodeError(10),limitOut(11),AmountWrong(12),MKFail(13);
		
		 private 	int index;
		Status(int index){
			this.index=index;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
	}

	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<ClientAd> getAd() {
		return ad;
	}

	public void setAd(List<ClientAd> ad) {
		this.ad = ad;
	}

	
	public List<ProductCategory> getDt() {
		return dt;
	}

	public void setDt(List<ProductCategory> dt) {
		this.dt = dt;
	}

	public List<PriceGape> getLp() {
		return lp;
	}

	public void setLp(List<PriceGape> lp) {
		this.lp = lp;
	}

	public List<Goods> getLg() {
		return lg;
	}

	public void setLg(List<Goods> lg) {
		this.lg = lg;
	}

public 	static class  PriceGape{
		    private String label;
		    private String[] condition;
		    
		    public PriceGape(String label,String ...gaps) {
		    	this.label=label;
		    	condition=gaps;
		    }
			public String getLabel() {
				return label;
			}
			public void setLabel(String label) {
				this.label = label;
			}
			public String[] getCondition() {
				return condition;
			}
			public void setCondition(String[] condition) {
				this.condition = condition;
			}
	  }
}
