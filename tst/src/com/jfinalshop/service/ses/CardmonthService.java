package com.jfinalshop.service.ses;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.dao.SCardmonthDao;
import com.jfinalshop.model.SCardmonth;
import com.jfinalshop.service.BaseService;

/**
 * Service - 月卡
 * 
 * 
 */
public class CardmonthService extends BaseService<SCardmonth> {
	
	/**
	 * 构造方法
	 */
	public CardmonthService() {
		super(SCardmonth.class);
	}
	
	
	private SCardmonthDao cardDao = Enhancer.enhance(SCardmonthDao.class);;
	
	public SCardmonth save(SCardmonth ad) {
		return super.save(ad);
	}

	public SCardmonth update(SCardmonth ad) {
		return super.update(ad);
	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(SCardmonth ad) {
		super.delete(ad);
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
	public Page<SCardmonth> findPage(String isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
		return cardDao.findPage(isEnabled, isExchange, hasExpired, pageable);
	}

}