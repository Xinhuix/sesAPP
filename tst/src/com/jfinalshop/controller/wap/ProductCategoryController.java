package com.jfinalshop.controller.wap;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.interceptor.WapInterceptor;
import com.jfinalshop.service.ProductCategoryService;

/**
 * Controller - 商品分类
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/product_category")
@Before(WapInterceptor.class)
public class ProductCategoryController extends BaseController {
	
	private ProductCategoryService productCategoryService = new ProductCategoryService();

	/**
	 * 首页
	 */
	public void index() {
		setAttr("title" , "全部分类");
		setAttr("footer", "category");//footer 的当前
		setAttr("rootProductCategories", productCategoryService.findRoots());
		render("/wap/product_category/index.ftl");
	}
}
