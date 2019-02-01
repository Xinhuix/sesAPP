package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseArticle<M extends BaseArticle<M>> extends Model<M> implements IBean {

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

	public void setAuthor(java.lang.String author) {
		set("author", author);
	}

	public java.lang.String getAuthor() {
		return get("author");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return get("content");
	}

	public void setGenerateMethod(java.lang.Integer generateMethod) {
		set("generate_method", generateMethod);
	}

	public java.lang.Integer getGenerateMethod() {
		return get("generate_method");
	}

	public void setHits(java.lang.Long hits) {
		set("hits", hits);
	}

	public java.lang.Long getHits() {
		return get("hits");
	}

	public void setIsPublication(java.lang.Boolean isPublication) {
		set("is_publication", isPublication);
	}

	public java.lang.Boolean getIsPublication() {
		return get("is_publication");
	}

	public void setIsTop(java.lang.Boolean isTop) {
		set("is_top", isTop);
	}

	public java.lang.Boolean getIsTop() {
		return get("is_top");
	}

	public void setSeoDescription(java.lang.String seoDescription) {
		set("seo_description", seoDescription);
	}

	public java.lang.String getSeoDescription() {
		return get("seo_description");
	}

	public void setSeoKeywords(java.lang.String seoKeywords) {
		set("seo_keywords", seoKeywords);
	}

	public java.lang.String getSeoKeywords() {
		return get("seo_keywords");
	}

	public void setSeoTitle(java.lang.String seoTitle) {
		set("seo_title", seoTitle);
	}

	public java.lang.String getSeoTitle() {
		return get("seo_title");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}

	public java.lang.String getTitle() {
		return get("title");
	}

	public void setArticleCategoryId(java.lang.Long articleCategoryId) {
		set("article_category_id", articleCategoryId);
	}

	public java.lang.Long getArticleCategoryId() {
		return get("article_category_id");
	}

	public void setImg(java.lang.String img) {
		set("img", img);
	}

	public java.lang.String getImg() {
		return get("img");
	}

	public void setGoodcount(java.lang.Long goodcount) {
		set("goodcount", goodcount);
	}

	public java.lang.Long getGoodcount() {
		return get("goodcount");
	}

}