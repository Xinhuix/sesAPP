package com.jfinalshop.model.client;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;



public class DP_STUC {
		private List<DP> ld;
		public DP_STUC() {
			ld=new ArrayList<>();
		}
		

		public List<DP> getLd() {
			return ld;
		}

		public void setLd(List<DP> ld) {
			this.ld = ld;
		}
			public DP_STUC  makeDPType(String title,String type,List<Record> lr) {
						DP dp=new DP();
						dp.setTitle(title);
						dp.setDp(lr);
						dp.setType(type);
						ld.add(dp);
						return this;
			}
		public	class  DP{
				    private  String title;
				    private String type;
				    List<Record>  dp;
				    
				    
					public String getType() {
						return type;
					}
					public void setType(String type) {
						this.type = type;
					}
					public String getTitle() {
						return title;
					}
					public void setTitle(String title) {
						this.title = title;
					}
					public List<Record> getDp() {
						return dp;
					}
					public void setDp(List<Record> dp) {
						this.dp = dp;
					}
				
			
			}

			
}
