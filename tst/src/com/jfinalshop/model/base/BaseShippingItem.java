package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseShippingItem<M extends BaseShippingItem<M>> extends Model<M> implements IBean {

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

	public void setIsDelivery(java.lang.Boolean isDelivery) {
		set("is_delivery", isDelivery);
	}

	public java.lang.Boolean getIsDelivery() {
		return get("is_delivery");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setQuantity(java.lang.Integer quantity) {
		set("quantity", quantity);
	}

	public java.lang.Integer getQuantity() {
		return get("quantity");
	}

	public void setSn(java.lang.String sn) {
		set("sn", sn);
	}

	public java.lang.String getSn() {
		return get("sn");
	}

	public void setSpecifications(java.lang.String specifications) {
		set("specifications", specifications);
	}

	public java.lang.String getSpecifications() {
		return get("specifications");
	}

	public void setProductId(java.lang.Long productId) {
		set("product_id", productId);
	}

	public java.lang.Long getProductId() {
		return get("product_id");
	}

	public void setShippingId(java.lang.Long shippingId) {
		set("shipping_id", shippingId);
	}

	public java.lang.Long getShippingId() {
		return get("shipping_id");
	}

}
