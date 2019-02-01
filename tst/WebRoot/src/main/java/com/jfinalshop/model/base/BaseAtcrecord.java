package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseAtcrecord<M extends BaseAtcrecord<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setReceivedate(java.util.Date receivedate) {
		set("receivedate", receivedate);
	}

	public java.util.Date getReceivedate() {
		return get("receivedate");
	}

	public void setReceivecount(java.lang.String receivecount) {
		set("receivecount", receivecount);
	}

	public java.lang.String getReceivecount() {
		return get("receivecount");
	}

	public void setMemberId(java.lang.String memberId) {
		set("member_id", memberId);
	}

	public java.lang.String getMemberId() {
		return get("member_id");
	}

}