package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePromotionCoupon<M extends BasePromotionCoupon<M>> extends Model<M> implements IBean {

	public void setPromotions(java.lang.Long promotions) {
		set("promotions", promotions);
	}

	public java.lang.Long getPromotions() {
		return get("promotions");
	}

	public void setCoupons(java.lang.Long coupons) {
		set("coupons", coupons);
	}

	public java.lang.Long getCoupons() {
		return get("coupons");
	}

}
