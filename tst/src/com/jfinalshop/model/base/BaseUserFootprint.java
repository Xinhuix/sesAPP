package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUserFootprint<M extends BaseUserFootprint<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
	}

	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public void setGoodsId(java.lang.Long goodsId) {
		set("goods_id", goodsId);
	}

	public java.lang.Long getGoodsId() {
		return get("goods_id");
	}

	public void setImgurl(java.lang.String imgurl) {
		set("imgurl", imgurl);
	}

	public java.lang.String getImgurl() {
		return get("imgurl");
	}

	public void setMemberId(java.lang.String memberId) {
		set("member_id", memberId);
	}

	public java.lang.String getMemberId() {
		return get("member_id");
	}

	public void setUserid(java.lang.Integer userid) {
		set("userid", userid);
	}

	public java.lang.Integer getUserid() {
		return get("userid");
	}

}
