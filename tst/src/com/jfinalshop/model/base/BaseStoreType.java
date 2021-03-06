package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStoreType<M extends BaseStoreType<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setTname(java.lang.String tname) {
		set("tname", tname);
	}

	public java.lang.String getTname() {
		return get("tname");
	}

	public void setDetail(java.lang.String detail) {
		set("detail", detail);
	}

	public java.lang.String getDetail() {
		return get("detail");
	}

	public void setCdate(java.util.Date cdate) {
		set("cdate", cdate);
	}

	public java.util.Date getCdate() {
		return get("cdate");
	}

	public void setIconAdr(java.lang.String iconAdr) {
		set("icon_adr", iconAdr);
	}

	public java.lang.String getIconAdr() {
		return get("icon_adr");
	}

}
