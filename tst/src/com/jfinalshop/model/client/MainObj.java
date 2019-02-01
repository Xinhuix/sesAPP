package com.jfinalshop.model.client;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Article;
import com.jfinalshop.model.ClientAd;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.SesStore;

public class MainObj {
			private List<ClientAd> ads;//首页banner
			private List<Article> la;//资讯集合
			private List<Goods>  dz;//定制商品
			private List<Goods> xh;//现货
			private List<Goods> tj;//推荐
			private List<Record> stores;//商铺
			
			
			public List<Goods> getDz() {
				return dz;
			}

			public void setDz(List<Goods> dz) {
				this.dz = dz;
			}

			public List<Goods> getXh() {
				return xh;
			}

			public void setXh(List<Goods> xh) {
				this.xh = xh;
			}

			public List<Goods> getTj() {
				return tj;
			}

			public void setTj(List<Goods> tj) {
				this.tj = tj;
			}

			public List<Record> getStores() {
				return stores;
			}

			public void setStores(List<Record> stores) {
				this.stores = stores;
			}

			public void setAds(List<ClientAd> lc) {
				ads=lc;
			}
			
			public List<ClientAd> getAds(){
				return ads;
			}
			public void setLa(List<Article> la) {
				this.la=la;
			}
			public List<Article> getLa() {
				return la;
			}
}
