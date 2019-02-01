package com.jfinalshop.model.client;

import java.util.ArrayList;
import java.util.List;

import com.jfinalshop.model.ClientAd;
import com.jfinalshop.model.DzType;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.ProductCategory;
import com.jfinalshop.model.Review;

public class CustSTUC {
	
		private List<ClientAd> ad=new ArrayList<>();
		//private List<DzType> dt=new ArrayList<>();
		private List<PriceGap> lp=new ArrayList<>();
		private List<Goods> lg=new ArrayList<>();
		private List<Review> rg;
		private List<ProductCategory> dt=new ArrayList<>();
		private int status;
		private int collect;
		
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
		
		/**
		 * 商品收藏
		 */
		public enum Collect {

			/** 未收藏 */
			Cancel(0),

			/** 已收藏 */
			Success(1);
			private 	int index;
			Collect(int index){
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

		//public List<DzType> getDt() {
		//	return dt;
		//}

		//public void setDt(List<DzType> dt) {
		//	this.dt = dt;
		//}
		

		public List<PriceGap> getLp() {
			return lp;
		}

		public List<ProductCategory> getDt() {
			return dt;
		}

		public void setDt(List<ProductCategory> dt) {
			this.dt = dt;
		}

		public void setLp(List<PriceGap> lp) {
			this.lp = lp;
		}

		public List<Goods> getLg() {
			return lg;
		}

		public void setLg(List<Goods> lg) {
			this.lg = lg;
		}
		
	    public List<Review> getRg() {
			return rg;
		}

		public void setRg(List<Review> rg) {
			this.rg = rg;
		}

	    public int getCollect() {
			return collect;
		}

		public void setCollect(int collect) {
			this.collect = collect;
		}


	public 	static class  PriceGap{
			    private String label;
			    private String[] condition;
			    
			    public PriceGap(String label,String ...gaps) {
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
