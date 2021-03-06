package com.jfinalshop.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.jfinalshop.model.base.BaseSesStore;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SesStore extends BaseSesStore<SesStore> {
	public static final SesStore dao = new SesStore().dao();
	
	public enum Status{
				Join("在线申请"),Verify("待审核"),Interchange("待对接"),Success("成功入驻"),Operation("正常经营"),AuthorityPause("官方罚停"),AuthorityExit("官方清退"),pause("暂停经营"),Exit("退出");
				private String name;
			Status(String name){
				this.name=name;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			
		
	}
	/** 评论 */
	private List<Review> reviews = new ArrayList<Review>();
	
	/**
	 * 获取评论
	 * 
	 * @return 评论
	 */
	public List<Review> getReviews() {
		if (CollectionUtils.isEmpty(reviews)) {
			String sql = "SELECT * FROM review WHERE store_id = ?";
			reviews = Review.dao.find(sql, getId());
		}
		return reviews;
	}

	/**
	 * 设置评论
	 * 
	 * @param reviews
	 *            评论
	 */
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
}
