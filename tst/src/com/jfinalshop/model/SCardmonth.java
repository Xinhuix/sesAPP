package com.jfinalshop.model;

import com.jfinalshop.model.base.BaseSCardmonth;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SCardmonth extends BaseSCardmonth<SCardmonth> {
	public static final SCardmonth dao = new SCardmonth();
	
	
	/** 会员卡 */
	private SCard card;
	
	
	/**
	 * 获取会员卡
	 * 
	 * @return 会员卡
	 */
	public SCard getCard() {
		if (card == null) {
			card = SCard.dao.findById(getCardId());
		}
		return card;
	}
	
	
	/**
	 * 设置会员卡类
	 * 
	 * @param card
	 *            会员卡
	 */
	public void setCard(SCard card) {
		this.card = card;
	}

	
}
