package com.jfinalshop.model.client;

import java.util.List;

import com.jfinalshop.model.ChosenType;
import com.jfinalshop.model.ClientAd;
import com.jfinalshop.model.Evalueate;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.ProductCategory;
import com.jfinalshop.model.Review;

import com.jfinalshop.model.ScreenType;
import com.jfinalshop.model.SesPlatform;
import com.jfinalshop.model.SesStore;
import com.jfinalshop.model.StoreType;

public class StoreSTUC {
			private List<ClientAd> storead;
			private List<StoreType> sts;
			private List<SesPlatform> sps;
			private List<ScreenType> st;
			private List<SesStore> sest;
			private List<Goods> gs;
			private List<ProductCategory> pc;
			private List<Evalueate> eva;			
			private List<Review> rg;
			private List<Distance> distance;
			private List<ChosenType> ct;
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
			 * 店铺收藏
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
			public List<ClientAd> getStoread() {
				return storead;
			}
			public void setStoread(List<ClientAd> storead) {
				this.storead = storead;
			}
			public List<StoreType> getSts() {
				return sts;
			}
			public void setSts(List<StoreType> sts) {
				this.sts = sts;
			}
			public List<SesPlatform> getSps() {
				return sps;
			}
			public void setSps(List<SesPlatform> sps) {
				this.sps = sps;
			}
			public List<ScreenType> getSt() {
				return st;
			}
			public void setSt(List<ScreenType> st) {
				this.st = st;
			}						
			
			public int getStatus() {
				return status;
			}
			public void setStatus(int status) {
				this.status = status;
			}
			public List<SesStore> getSest() {
				return sest;
			}
			public void setSest(List<SesStore> sest) {
				this.sest = sest;
			}

			public List<Goods> getGs() {
				return gs;
			}
			public void setGs(List<Goods> gs) {
				this.gs = gs;
			}
			public List<ProductCategory> getPc() {
				return pc;
			}
			public void setPc(List<ProductCategory> pc) {
				this.pc = pc;
			}

			public List<Evalueate> getEva() {
				return eva;
			}
			public void setEva(List<Evalueate> eva) {
				this.eva = eva;
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

			public List<Distance> getDistance() {
				return distance;
			}
			public void setDistance(List<Distance> distance) {
				this.distance = distance;
			}

			public List<ChosenType> getCt() {
				return ct;
			}
			public void setCt(List<ChosenType> ct) {
				this.ct = ct;
			}




			public static class Distance {
				private String label;
				private String range; 
				
				public Distance(String label,String interval){
					this.label=label;
					range=interval;
				}

				public String getLabel() {
					return label;
				}

				public void setLabel(String label) {
					this.label = label;
				}

				public String getRange() {
					return range;
				}

				public void setRange(String range) {
					this.range = range;
				}
				
			}
			
}
