package com.jfinalshop.controller.wap;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Pageable;
import com.jfinalshop.exception.ResourceNotFoundException;
import com.jfinalshop.interceptor.ThemeInterceptor;
import com.jfinalshop.model.ArticleCategory;
import com.jfinalshop.model.Tag;
import com.jfinalshop.service.ArticleCategoryService;
import com.jfinalshop.service.ArticleService;
import com.jfinalshop.service.SearchService;

/**
 * Controller - 文章
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/article")
@Before(ThemeInterceptor.class)
public class ArticleController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 20;

	private ArticleService articleService = enhance(ArticleService.class);
	private ArticleCategoryService articleCategoryService = enhance(ArticleCategoryService.class);
	private SearchService searchService = enhance(SearchService.class);

	/**
	 * 列表
	 */
	public void list() {
		Long id = getParaToLong(0);
		
		Integer pageNumber = getParaToInt("pageNumber");
		ArticleCategory articleCategory = articleCategoryService.find(id);
		if (id!=null&&articleCategory == null) {
		//	throw new ResourceNotFoundException();
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("articleCategory", articleCategory);
		setAttr("page", articleService.findPage(articleCategory, null, true, pageable));
		render("/wap/article/list.ftl");
	}

	/**
	 * 详情
	 */
	public void detail() {
		Long id = getParaToLong("id");
		
		if (id == null) {
			redirect("/wap/index.html");
			return;
		}
		//setAttr("articleCategoryTree", articleCategoryService.findTree());
		//setAttr("tags", tagService.findList(Tag.Type.article));
		setAttr("article", articleService.find(id));
		render("/wap/article/content.ftl");
	}
	
	
	/**
	 * 搜索
	 */
	public void search() {
		String keyword = getPara("keyword"); 
		
		System.out.println("keyword--->"+keyword);
		
		Integer pageNumber = getParaToInt("pageNumber");
		if (StringUtils.isEmpty(keyword)) {
			//redirect(ERROR_VIEW);
			//return;
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("articleKeyword", keyword);
		setAttr("page", searchService.search(keyword, pageable));
		render("/wap/article/search.ftl");
	}

	/**
	 * 点击数
	 */
	public void hits() {
		Long id = getParaToLong(0);
		renderJson(articleService.viewHits(id));
	}

}