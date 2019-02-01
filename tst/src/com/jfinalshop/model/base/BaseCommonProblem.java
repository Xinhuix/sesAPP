package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCommonProblem<M extends BaseCommonProblem<M>> extends Model<M> implements IBean {

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

	public void setVersion(java.lang.String version) {
		set("version", version);
	}

	public java.lang.String getVersion() {
		return get("version");
	}

	public void setAnswerimgs(java.lang.String answerimgs) {
		set("answerimgs", answerimgs);
	}

	public java.lang.String getAnswerimgs() {
		return get("answerimgs");
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

	public void setParentId(java.lang.String parentId) {
		set("parent_id", parentId);
	}

	public java.lang.String getParentId() {
		return get("parent_id");
	}

}