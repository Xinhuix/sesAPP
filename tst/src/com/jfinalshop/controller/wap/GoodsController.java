package com.jfinalshop.controller.wap;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.RequestContextHolder;
import com.jfinalshop.dao.TagDao;
import com.jfinalshop.exception.ResourceNotFoundException;
import com.jfinalshop.model.Consultation;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.ProductCategory;
import com.jfinalshop.model.Review;
import com.jfinalshop.model.Tag;
import com.jfinalshop.service.ConsultationService;
import com.jfinalshop.service.GoodsService;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.ProductCategoryService;
import com.jfinalshop.service.ReviewService;
import com.jfinalshop.service.SearchService;
import com.jfinalshop.util.SystemUtils;


/**
 * Controller - 货品
 * 
 *
 */
@ControllerBind(controllerKey = "/wap/goods")
public class GoodsController extends BaseController {
	
	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
	private ConsultationService consultationService = enhance(ConsultationService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private MemberService memberService = enhance(MemberService.class);
	private SearchService searchService = enhance(SearchService.class);
	private ReviewService reviewService = enhance(ReviewService.class);
	private TagDao tagDao = Enhancer.enhance(TagDao.class);
	/**
	 * 列表
	 */
	public void list() {
		Long productCategoryId = getParaToLong("productCategoryId");
		if(productCategoryId==null){
		  productCategoryId = getParaToLong(0);
		}
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null) {
		//throw new ResourceNotFoundException();
		}
		
		String orderTypeName = getPara("orderType");
		Goods.OrderType orderType = StrKit.notBlank(orderTypeName) ? Goods.OrderType.valueOf(orderTypeName) : null;
		
		String startPriceStr = getPara("startPrice", null);
		BigDecimal startPrice = null;
		if (StrKit.notBlank(startPriceStr)) {
			startPrice = new BigDecimal(startPriceStr);
		}
		
		String endPriceStr = getPara("endPrice", null);
		BigDecimal endPrice = null;
		if (StrKit.notBlank(endPriceStr)) {
			endPrice = new BigDecimal(endPriceStr);
		}
		Long tagId = getParaToLong("tagId");
		Tag tag=null;
		if(tagId!=null){
		tag = tagDao.find(tagId);
		setAttr("title" , tag.getName());
		}else{
			if(productCategory!=null){
			setAttr("title" , productCategory.getName());
			}else{
				setAttr("title" , "所有商品");
			}
		}
		Integer pageNumber = getParaToInt("pageNumber", 1);
		Integer pageSize = getParaToInt("pageSize", 100);
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		
		setAttr("productCategory", productCategory);
		setAttr("orderType", orderType == null ? "all" : orderType);
		setAttr("pages", goodsService.findPage(null, productCategory, null, null, tag, null, startPrice, endPrice, true, true, null, null, null, null, orderType, pageable));
		render("/wap/goods/list.ftl");
	}

	/**
	 * 详情
	 */
	public void detail() {
		Long id = getParaToLong("id");
		Goods goods = goodsService.find(id);
		Pageable pageable = new Pageable(1, 20);
		Boolean favorite = false;
		
		if (goods == null) {
			redirect("/wap/index.html");
			return;
		}
		
		RequestContextHolder.setRequestAttributes(getRequest());
		if (goods.getFavoriteMembers().contains(memberService.getCurrent())) {
			favorite = true;
		}
		Page<Consultation> consultationPages = consultationService.findPage(null, goods, true, pageable);
		Page<Review> reviewPages = reviewService.findPage(null, goods, null, null, pageable);
		setAttr("goods", goods);
		setAttr("favorite", favorite);
		setAttr("consultationPages", consultationPages);
		setAttr("reviewPages", reviewPages);
		setAttr("title" , goods.getName());
		
		
		
		render("/wap/goods/detail.ftl");
	}
	
	/**
	 * 搜索
	 */
	public void search() {
		String keyword = getPara("keyword");
		List<Goods> goods = searchService.query(keyword);
		setAttr("goodsList", goods == null ? null : goods);
		setAttr("title" , "搜索到[" + goods.size() + "]结果");
		setAttr("keyword", keyword);
		List list_str=null;
		
		if(SystemUtils.getSetting().getKeyword()!=null){
	      String[] arr = SystemUtils.getSetting().getKeyword().split(",");//分割字符串得到数组
	      list_str = java.util.Arrays.asList(arr);//字符数组转list
		}
		setAttr("list_str", list_str); //后台设置:搜索关键词
		
		render("/wap/goods/search.ftl");
	}
	
	
}
