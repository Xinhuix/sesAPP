package com.jfinalshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.model.SCard;

/**
 * Dao - 会员卡
 * 
 * 
 */
public class SCardDao extends BaseDao<SCard> {

	/**
	 * 构造方法
	 */
	public SCardDao() {
		super(SCard.class);
	}

	/**
	 * 查找优惠券分页
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param isExchange
	 *            是否允许积分兑换
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 优惠券分页
	 */
	public Page<SCard> findPage(String isEnabled, Boolean isExchange,
			Boolean hasExpired, Pageable pageable) {
		String sqlExceptSelect = "FROM s_card WHERE 1 = 1 ";
		if (isEnabled != null) {
			sqlExceptSelect += " AND status = '" + isEnabled+"'";
		}
		
		return super.findPage(sqlExceptSelect, pageable);
	}

}