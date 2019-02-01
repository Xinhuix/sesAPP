package com.jfinalshop.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinalshop.model.Cart;
import com.jfinalshop.util.DateUtils;

/**
 * Dao - 购物车
 * 
 * 
 */
public class CartDao extends BaseDao<Cart> {
	
	/**
	 * 构造方法
	 */
	public CartDao() {
		super(Cart.class);
	}
	
	/**
	 * 根据密钥查找购物车
	 * 
	 * @param key
	 *            密钥
	 * @return 购物车，若不存在则返回null
	 */
	public Cart findByKey(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		try {
			String sql = "SELECT * FROM cart WHERE cart_key = ?";
			return modelManager.findFirst(sql, key);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 查找购物车
	 * 
	 * @param hasExpired
	 *            是否已过期
	 * @param count
	 *            数量
	 * @return 购物车
	 */
	public List<Cart> findList(Boolean hasExpired, Integer count) {
		String sql = "SELECT * FROM cart WHERE 1 = 1";
		if (hasExpired != null) {
			if (hasExpired) {
				sql += " AND expire IS NOT NULL AND expire <= '" + DateUtils.getDateTime() + "'";
			} else {
				sql += " AND expire IS NULL OR expire > '" + DateUtils.getDateTime() + "'";
			}
		}
		return super.findList(sql, null, count, null, null);
	}

}