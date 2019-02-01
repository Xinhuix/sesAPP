package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseProduct<M extends BaseProduct<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
	}

	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public void setModifyDate(java.util.Date modifyDate) {
		set("modify_date", modifyDate);
	}

	public java.util.Date getModifyDate() {
		return get("modify_date");
	}

	public void setVersion(java.lang.Long version) {
		set("version", version);
	}

	public java.lang.Long getVersion() {
		return get("version");
	}

	public void setAllocatedStock(java.lang.Integer allocatedStock) {
		set("allocated_stock", allocatedStock);
	}

	public java.lang.Integer getAllocatedStock() {
		return get("allocated_stock");
	}

	public void setCost(java.math.BigDecimal cost) {
		set("cost", cost);
	}

	public java.math.BigDecimal getCost() {
		return get("cost");
	}

	public void setExchangePoint(java.lang.Long exchangePoint) {
		set("exchange_point", exchangePoint);
	}

	public java.lang.Long getExchangePoint() {
		return get("exchange_point");
	}

	public void setIsDefault(java.lang.Boolean isDefault) {
		set("is_default", isDefault);
	}

	public java.lang.Boolean getIsDefault() {
		return get("is_default");
	}

	public void setMarketPrice(java.math.BigDecimal marketPrice) {
		set("market_price", marketPrice);
	}

	public java.math.BigDecimal getMarketPrice() {
		return get("market_price");
	}

	public void setPrice(java.math.BigDecimal price) {
		set("price", price);
	}

	public java.math.BigDecimal getPrice() {
		return get("price");
	}

	public void setRewardPoint(java.lang.Long rewardPoint) {
		set("reward_point", rewardPoint);
	}

	public java.lang.Long getRewardPoint() {
		return get("reward_point");
	}

	public void setSn(java.lang.String sn) {
		set("sn", sn);
	}

	public java.lang.String getSn() {
		return get("sn");
	}

	public void setSpecificationValues(java.lang.String specificationValues) {
		set("specification_values", specificationValues);
	}

	public java.lang.String getSpecificationValues() {
		return get("specification_values");
	}

	public void setStock(java.lang.Integer stock) {
		set("stock", stock);
	}

	public java.lang.Integer getStock() {
		return get("stock");
	}

	public void setGoodsId(java.lang.Long goodsId) {
		set("goods_id", goodsId);
	}

	public java.lang.Long getGoodsId() {
		return get("goods_id");
	}

}