package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseArticleEva<M extends BaseArticleEva<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setArticleId(java.lang.Long articleId) {
		set("article_id", articleId);
	}

	public java.lang.Long getArticleId() {
		return get("article_id");
	}

	public void setEvalueateId(java.lang.Integer evalueateId) {
		set("evalueate_id", evalueateId);
	}

	public java.lang.Integer getEvalueateId() {
		return get("evalueate_id");
	}

}
