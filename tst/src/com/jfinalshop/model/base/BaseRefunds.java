package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRefunds<M extends BaseRefunds<M>> extends Model<M> implements IBean {

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

	public void setAccount(java.lang.String account) {
		set("account", account);
	}

	public java.lang.String getAccount() {
		return get("account");
	}

	public void setAmount(java.math.BigDecimal amount) {
		set("amount", amount);
	}

	public java.math.BigDecimal getAmount() {
		return get("amount");
	}

	public void setBank(java.lang.String bank) {
		set("bank", bank);
	}

	public java.lang.String getBank() {
		return get("bank");
	}

	public void setMemo(java.lang.String memo) {
		set("memo", memo);
	}

	public java.lang.String getMemo() {
		return get("memo");
	}

	public void setMethod(java.lang.Integer method) {
		set("method", method);
	}

	public java.lang.Integer getMethod() {
		return get("method");
	}

	public void setOperator(java.lang.String operator) {
		set("operator", operator);
	}

	public java.lang.String getOperator() {
		return get("operator");
	}

	public void setPayee(java.lang.String payee) {
		set("payee", payee);
	}

	public java.lang.String getPayee() {
		return get("payee");
	}

	public void setPaymentMethod(java.lang.String paymentMethod) {
		set("payment_method", paymentMethod);
	}

	public java.lang.String getPaymentMethod() {
		return get("payment_method");
	}

	public void setSn(java.lang.String sn) {
		set("sn", sn);
	}

	public java.lang.String getSn() {
		return get("sn");
	}

	public void setOrderId(java.lang.Long orderId) {
		set("order_id", orderId);
	}

	public java.lang.Long getOrderId() {
		return get("order_id");
	}

}
