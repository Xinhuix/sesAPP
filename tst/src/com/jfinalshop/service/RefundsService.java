package com.jfinalshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinalshop.dao.SnDao;
import com.jfinalshop.model.Refunds;
import com.jfinalshop.model.Sn;
import com.jfinalshop.util.Assert;

/**
 * Service - 退款单
 * 
 * 
 */
public class RefundsService extends BaseService<Refunds> {

	/**
	 * 构造方法
	 */
	public RefundsService() {
		super(Refunds.class);
	}
	
	private SnDao snDao = Enhancer.enhance(SnDao.class);
	
	public Refunds save(Refunds refunds) {
		Assert.notNull(refunds);

		refunds.setSn(snDao.generate(Sn.Type.refunds));

		return super.save(refunds);
	}
}