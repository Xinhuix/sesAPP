package com.jfinalshop.service.ses;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.dao.SCardDao;
import com.jfinalshop.model.SCard;
import com.jfinalshop.service.BaseService;

/**
 * Service - 卡
 * 
 * 
 */
public class CardService extends BaseService<SCard> {
	
	/**
	 * 构造方法
	 */
	public CardService() {
		super(SCard.class);
	}
	
	
	private SCardDao cardDao = Enhancer.enhance(SCardDao.class);;
	
	public SCard save(SCard ad) {
		return super.save(ad);
	}

	public SCard update(SCard ad) {
		return super.update(ad);
	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(SCard ad) {
		super.delete(ad);
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
	public Page<SCard> findPage(String isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
		return cardDao.findPage(isEnabled, isExchange, hasExpired, pageable);
	}

}