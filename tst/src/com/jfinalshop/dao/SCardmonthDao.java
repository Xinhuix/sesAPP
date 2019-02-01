package com.jfinalshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.model.SCardmonth;

/**
 * Dao - 月卡
 * 
 * 
 */
public class SCardmonthDao extends BaseDao<SCardmonth> {

	/**
	 * 构造方法
	 */
	public SCardmonthDao() {
		super(SCardmonth.class);
	}

	/**
	 * 查找月卡分页
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
	public Page<SCardmonth> findPage(String isEnabled, Boolean isExchange,
			Boolean hasExpired, Pageable pageable) {
		String sqlExceptSelect = "FROM s_cardmonth WHERE 1 = 1 ";
		if (isEnabled != null) {
			sqlExceptSelect += " AND status = '" + isEnabled+"'";
		}
		
		return super.findPage(sqlExceptSelect, pageable);
	}

}