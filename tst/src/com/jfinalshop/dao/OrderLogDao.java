package com.jfinalshop.dao;

import com.jfinalshop.model.OrderLog;

/**
 * Dao - 订单记录
 * 
 * 
 */
public class OrderLogDao extends BaseDao<OrderLog> {
	
	/**
	 * 构造方法
	 */
	public OrderLogDao() {
		super(OrderLog.class);
	}
	
	/**
	 * 删除
	 */
	public void deleteByOrderId(Long orderId) {
		modelManager.deleteByOrderId(orderId);
	}
}