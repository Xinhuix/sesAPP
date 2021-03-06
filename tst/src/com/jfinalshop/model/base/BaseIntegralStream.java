package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseIntegralStream<M extends BaseIntegralStream<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setCreateData(java.util.Date createData) {
		set("create_data", createData);
	}

	public java.util.Date getCreateData() {
		return get("create_data");
	}

	public void setEvent(java.lang.String event) {
		set("event", event);
	}

	public java.lang.String getEvent() {
		return get("event");
	}

	public void setBalance(java.lang.Integer balance) {
		set("balance", balance);
	}

	public java.lang.Integer getBalance() {
		return get("balance");
	}

	public void setMemberId(java.lang.Integer memberId) {
		set("member_id", memberId);
	}

	public java.lang.Integer getMemberId() {
		return get("member_id");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

}
