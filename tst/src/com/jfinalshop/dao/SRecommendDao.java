package com.jfinalshop.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.SCard;
import com.jfinalshop.model.SRecommend;

/**
 * Dao - 会员推荐记录
 * 
 * 
 */
public class SRecommendDao extends BaseDao<SRecommend> {

	/**
	 * 构造方法
	 */
	public SRecommendDao() {
		super(SRecommend.class);
	}
	

}