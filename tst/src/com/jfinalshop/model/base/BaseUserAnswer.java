package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUserAnswer<M extends BaseUserAnswer<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setCareateDate(java.util.Date careateDate) {
		set("careate_date", careateDate);
	}

	public java.util.Date getCareateDate() {
		return get("careate_date");
	}

	public void setModifyDate(java.util.Date modifyDate) {
		set("modify_date", modifyDate);
	}

	public java.util.Date getModifyDate() {
		return get("modify_date");
	}

	public void setVersion(java.lang.Integer version) {
		set("version", version);
	}

	public java.lang.Integer getVersion() {
		return get("version");
	}

	public void setProblem(java.lang.String problem) {
		set("problem", problem);
	}

	public java.lang.String getProblem() {
		return get("problem");
	}

	public void setAnswer(java.lang.String answer) {
		set("answer", answer);
	}

	public java.lang.String getAnswer() {
		return get("answer");
	}

	public void setUserProblemsId(java.lang.Long userProblemsId) {
		set("user_problems_id", userProblemsId);
	}

	public java.lang.Long getUserProblemsId() {
		return get("user_problems_id");
	}

}