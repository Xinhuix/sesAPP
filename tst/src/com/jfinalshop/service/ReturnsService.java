package com.jfinalshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinalshop.dao.SnDao;
import com.jfinalshop.model.Returns;
import com.jfinalshop.model.Sn;
import com.jfinalshop.util.Assert;

/**
 * Service - 退货单
 * 
 * 
 */
public class ReturnsService extends BaseService<Returns> {

	/**
	 * 构造方法
	 */
	public ReturnsService() {
		super(Returns.class);
	}
	
	private SnDao snDao = Enhancer.enhance(SnDao.class);

	public Returns save(Returns returns) {
		Assert.notNull(returns);

		returns.setSn(snDao.generate(Sn.Type.returns));

		return super.save(returns);
	}
}