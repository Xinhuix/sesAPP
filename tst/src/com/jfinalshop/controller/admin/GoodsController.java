package com.jfinalshop.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;
import com.jfinalshop.Message;
import com.jfinalshop.Pageable;
import com.jfinalshop.entity.ParameterValue;
import com.jfinalshop.entity.ProductImage;
import com.jfinalshop.entity.SpecificationItem;
import com.jfinalshop.entity.SpecificationValue;
import com.jfinalshop.model.Admin;
import com.jfinalshop.model.Attribute;
import com.jfinalshop.model.Brand;
import com.jfinalshop.model.DzType;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.Parameter;
import com.jfinalshop.model.Product;
import com.jfinalshop.model.ProductCategory;
import com.jfinalshop.model.Promotion;
import com.jfinalshop.model.Specification;
import com.jfinalshop.model.StockLog;
import com.jfinalshop.model.Tag;
import com.jfinalshop.service.AdminService;
import com.jfinalshop.service.AttributeService;
import com.jfinalshop.service.BrandService;
import com.jfinalshop.service.GoodsService;
import com.jfinalshop.service.ParameterValueService;
import com.jfinalshop.service.ProductCategoryService;
import com.jfinalshop.service.ProductImageService;
import com.jfinalshop.service.ProductService;
import com.jfinalshop.service.PromotionService;
import com.jfinalshop.service.SpecificationItemService;
import com.jfinalshop.service.SpecificationService;
import com.jfinalshop.service.StockLogService;
import com.jfinalshop.service.TagService;

/**
 * Controller - 货品
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/goods")
public class GoodsController extends BaseController {

	private GoodsService goodsService = enhance(GoodsService.class);
	private ProductService productService = enhance(ProductService.class);
	private ProductCategoryService productCategoryService = enhance(ProductCategoryService.class);
	private BrandService brandService = enhance(BrandService.class);
	private PromotionService promotionService = enhance(PromotionService.class);
	private TagService tagService = enhance(TagService.class);
	private ProductImageService productImageService = enhance(ProductImageService.class);
	private ParameterValueService parameterValueService = enhance(ParameterValueService.class);
	private SpecificationItemService specificationItemService = enhance(SpecificationItemService.class);
	private AttributeService attributeService = enhance(AttributeService.class);
	private SpecificationService specificationService = enhance(SpecificationService.class);
	private AdminService adminService = enhance(AdminService.class);
	
	private StockLogService stockLogService = enhance(StockLogService.class);

	/**
	 * 检查编号是否存在
	 */
	public void checkSn() {
		String sn = getPara("goods.sn");
		if (StringUtils.isEmpty(sn)) {
			renderJson(false);
			return;
		}
		renderJson(!goodsService.snExists(sn));
	}

	/**
	 * 获取参数
	 */
	public void parameters() {
		Long productCategoryId = getParaToLong("productCategoryId");
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		if (productCategory == null
				|| CollectionUtils.isEmpty(productCategory.getParameters())) {
			renderJson(data);
			return;
		}
		for (Parameter parameter : productCategory.getParameters()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("group", parameter.getParameterGroup());
			item.put("names", parameter.getNamesConverter());
			data.add(item);
		}
		renderJson(data);
	}

	/**
	 * 获取属性
	 */
	public void attributes() {
		Long productCategoryId = getParaToLong("productCategoryId");
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		if (productCategory == null
				|| CollectionUtils.isEmpty(productCategory.getAttributes())) {
			renderJson(data);
			return;
		}
		for (Attribute attribute : productCategory.getAttributes()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", attribute.getId());
			item.put("name", attribute.getName());
			item.put("options", attribute.getOptionsConverter());
			data.add(item);
		}
		renderJson(data);
	}

	/**
	 * 获取规格
	 */
	public void specifications() {
		Long productCategoryId = getParaToLong("productCategoryId");
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		if (productCategory == null
				|| CollectionUtils.isEmpty(productCategory.getSpecifications())) {
			renderJson(data);
			return;
		}
		for (Specification specification : productCategory.getSpecifications()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("name", specification.getName());
			item.put("options", specification.getOptionsConverter());
			data.add(item);
		}
		renderJson(data);
	}

	/**
	 * 添加
	 */
	public void add() {
		setAttr("types", Goods.Type.values());
		setAttr("productCategoryTree", productCategoryService.findTree());
		setAttr("brands", brandService.findAll());
		setAttr("promotions", promotionService.findAll());
		setAttr("tags", tagService.findList(Tag.Type.goods));
		setAttr("specifications", specificationService.findAll());
		setAttr("dz_type",DzType.dao.find("select id,name from dz_type"));
		render("/admin/goods/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		List<UploadFile> uploadFiles = getFiles();
		Goods goods = getModel(Goods.class);
		String typeName = getPara("type");
		goods.setType(StrKit.notBlank(typeName) ? Goods.Type.valueOf(typeName)
				.ordinal() : null);
		goods.setIsMarketable(getParaToBoolean("isMarketable", false));
		goods.setIsList(getParaToBoolean("isList", false));
		goods.setIsTop(getParaToBoolean("isTop", false));
		goods.setIsDelivery(getParaToBoolean("isDelivery", false));
		
		Product product = getModel(Product.class);
		String cycle=this.getPara("cycle");
		if(TextUtils.isEmpty(cycle)) {
			cycle="0";
		}
		if(!cycle.matches("[0-9]*")) {
			cycle="0";
		}
		String dz=getPara("dz_type_id");
		int dt=TextUtils.isEmpty(dz)?0:Integer.parseInt(dz);
		goods.setDzTypeId(dt);
		goods.setCycle(Integer.parseInt(cycle));
		Long productCategoryId = getParaToLong("productCategoryId");
		Long brandId = getParaToLong("brandId");
		Long[] promotionIds = getParaValuesToLong("promotionIds");
		Long[] tagIds = getParaValuesToLong("tagIds");

		// 图片
		Integer productImageIndex = getBeans(ProductImage.class,
				"productImages").size();
		if (CollectionUtils.isNotEmpty(uploadFiles)) {
			List<ProductImage> productImages = new ArrayList<ProductImage>();
			for (int i = 0; i < productImageIndex; i++) {
				ProductImage productImage = getBean(ProductImage.class,
						"productImages[" + i + "]");
				productImage.setFile(getFile("productImages[" + i + "].file"));
				productImages.add(productImage);
			}
			goods.setProductImagesConverter(productImages);
			productImageService.filter(goods.getProductImagesConverter());
		}

		// 参数
		Integer parameterIndex = getBeans(ParameterValue.class,
				"parameterValues").size();
		if (0 < parameterIndex) {
			List<ParameterValue> parameterValues = new ArrayList<ParameterValue>();
			for (int i = 0; i < parameterIndex; i++) {
				ParameterValue parameterValue = getBean(ParameterValue.class,
						"parameterValues[" + i + "]");
				List<ParameterValue.Entry> entries = getBeans(
						ParameterValue.Entry.class, "parameterValueEntrys[" + i
								+ "].entries");
				parameterValue.setEntries(entries);
				parameterValues.add(parameterValue);
			}
			goods.setParameterValues(JSONArray.toJSONString(parameterValues));
			goods.setParameterValuesConverter(parameterValues);
			parameterValueService.filter(goods.getParameterValuesConverter());
		}

		// 产品组
		Integer productsIndex = getBeans(Product.class, "productList").size();
		List<Product> products = new ArrayList<Product>();

		if (0 < productsIndex) {
			for (int i = 0; i < productsIndex; i++) {
				Product sProduct = getModel(Product.class, "productList[" + i
						+ "]");
				List<SpecificationValue> specificationValues = getBeans(
						SpecificationValue.class, "productLists[" + i
								+ "].specificationValues");
				sortList(specificationValues, "id", "ASC");
				sProduct.setSpecificationValues(JSONArray
						.toJSONString(specificationValues));
				products.add(sProduct);
			}
			productService.filter(products);
		}

		// 规格
		Integer specificationItemsIndex = getBeans(SpecificationItem.class,
				"specificationItems").size();
		List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();
		if (0 < specificationItemsIndex) {
			for (int i = 0; i < specificationItemsIndex; i++) {
				SpecificationItem specificationItem = getBean(
						SpecificationItem.class, "specificationItems[" + i
								+ "]");
				List<SpecificationItem.Entry> entries = getBeans(
						SpecificationItem.Entry.class,
						"specificationItemEntrys[" + i + "].entries");
				specificationItem.setEntries(entries);
				specificationItems.add(specificationItem);
			}
			goods.setSpecificationItems(JSONArray
					.toJSONString(specificationItems));
			goods.setSpecificationItemConverter(specificationItems);
			specificationItemService.filter(goods
					.getSpecificationItemsConverter());
		}

		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		if (productCategory != null) {
			goods.setProductCategoryId(productCategory.getId());
		}
		Brand brand = brandService.find(brandId);
		if (brand != null) {
			goods.setBrandId(brand.getId());
		}
		goods.setPromotions(new ArrayList<Promotion>(promotionService
				.findList(promotionIds)));
		goods.setTags(new ArrayList<Tag>(tagService.findList(tagIds)));

		// goods.removeAttributeValue();
		for (Attribute attribute : goods.getProductCategory().getAttributes()) {
			String value = getPara("attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(
					attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		if (StringUtils.isNotEmpty(goods.getSn())
				&& goodsService.snExists(goods.getSn())) {
			setAttr("errorMessage", "商品编号不能为空或已存在！");
			redirect(ERROR_VIEW);
			return;
		}

		Admin admin = adminService.getCurrent();
		if (goods.hasSpecification()) {

			if (CollectionUtils.isEmpty(products)) {

				setAttr("errorMessage", "商品规格不能为空！");
				// redirect("/admin/common/error.jhtml");
				render("/admin/common/error.ftl");
				// redirect(ERROR_VIEW,true);
				return;
			}
			goodsService.save(goods, products, admin);
		} else {
			if (product == null) {

				setAttr("errorMessage", "产品不能为空！");
				redirect(ERROR_VIEW);
				return;
			}
			goodsService.save(goods, product, admin);
		}

		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("types", Goods.Type.values());
		setAttr("productCategoryTree", productCategoryService.findTree());
		setAttr("brands", brandService.findAll());
		setAttr("promotions", promotionService.findAll());
		setAttr("tags", tagService.findList(Tag.Type.goods));
		setAttr("specifications", specificationService.findAll());
		setAttr("goods", goodsService.find(id));
		render("/admin/goods/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		List<UploadFile> uploadFiles = getFiles();

		Product product = getModel(Product.class);
		Long productCategoryId = getParaToLong("productCategoryId");
		Long brandId = getParaToLong("brandId");
		Long[] promotionIds = getParaValuesToLong("promotionIds");
		Long[] tagIds = getParaValuesToLong("tagIds");

		Goods goods = getModel(Goods.class);
		goods.setIsMarketable(getParaToBoolean("isMarketable", false));
		goods.setIsList(getParaToBoolean("isList", false));
		goods.setIsTop(getParaToBoolean("isTop", false));
		goods.setIsDelivery(getParaToBoolean("isDelivery", false));

		Goods pGoods = goodsService.find(goods.getId());
		goods.setType(pGoods.getType());

		// 图片
		Integer productImageIndex = getBeans(ProductImage.class,
				"productImages").size();
		if (productImageIndex > 0 && CollectionUtils.isNotEmpty(uploadFiles)) {
			List<ProductImage> productImages = new ArrayList<ProductImage>();
			for (int i = 0; i < productImageIndex; i++) {
				ProductImage productImage = getBean(ProductImage.class,
						"productImages[" + i + "]");
				productImage.setFile(getFile("productImages[" + i + "].file"));
				productImages.add(productImage);
			}
			goods.setProductImagesConverter(productImages);
			productImageService.filter(goods.getProductImagesConverter());
		} else if (productImageIndex > 0
				&& CollectionUtils.isEmpty(uploadFiles)) {
			goods.setProductImages(pGoods.getProductImages());
		}

		// 参数
		Integer parameterIndex = getBeans(ParameterValue.class,
				"parameterValues").size();
		if (0 < parameterIndex) {
			List<ParameterValue> parameterValues = new ArrayList<ParameterValue>();
			for (int i = 0; i < parameterIndex; i++) {
				ParameterValue parameterValue = getBean(ParameterValue.class,
						"parameterValues[" + i + "]");
				List<ParameterValue.Entry> entries = getBeans(
						ParameterValue.Entry.class, "parameterValueEntrys[" + i
								+ "].entries");
				parameterValue.setEntries(entries);
				parameterValues.add(parameterValue);
			}
			goods.setParameterValues(JSONArray.toJSONString(parameterValues));
			goods.setParameterValuesConverter(parameterValues);
			parameterValueService.filter(goods.getParameterValuesConverter());
		}

		// 产品组
		Integer productsIndex = getBeans(Product.class, "productList").size();
		List<Product> products = new ArrayList<Product>();
		if (0 < productsIndex) {
			for (int i = 0; i < productsIndex; i++) {
				Product sProduct = getModel(Product.class, "productList[" + i
						+ "]");
				List<SpecificationValue> specificationValues = getBeans(
						SpecificationValue.class, "productLists[" + i
								+ "].specificationValues");
				sortList(specificationValues, "id", "ASC");
				sProduct.setSpecificationValues(JSONArray
						.toJSONString(specificationValues));
				products.add(sProduct);
			}
			productService.filter(products);
		}

		// 规格
		Integer specificationItemsIndex = getBeans(SpecificationItem.class,
				"specificationItems").size();
		List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();
		if (0 < specificationItemsIndex) {
			for (int i = 0; i < specificationItemsIndex; i++) {
				SpecificationItem specificationItem = getBean(
						SpecificationItem.class, "specificationItems[" + i
								+ "]");
				List<SpecificationItem.Entry> entries = getBeans(
						SpecificationItem.Entry.class,
						"specificationItemEntrys[" + i + "].entries");
				specificationItem.setEntries(entries);
				specificationItems.add(specificationItem);
			}
			goods.setSpecificationItems(JSONArray
					.toJSONString(specificationItems));
			goods.setSpecificationItemConverter(specificationItems);
			specificationItemService.filter(goods
					.getSpecificationItemsConverter());
		}

		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		if (productCategory != null) {
			goods.setProductCategoryId(productCategory.getId());
		}
		Brand brand = brandService.find(brandId);
		if (brand != null) {
			goods.setBrandId(brand.getId());
		}
		goods.setPromotions(new ArrayList<Promotion>(promotionService
				.findList(promotionIds)));
		goods.setTags(new ArrayList<Tag>(tagService.findList(tagIds)));

		for (Attribute attribute : goods.getProductCategory().getAttributes()) {
			String value = getRequest().getParameter(
					"attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(
					attribute, value);
			goods.setAttributeValue(attribute, attributeValue);
		}

		Admin admin = adminService.getCurrent();
		if (goods.hasSpecification()) {
			if (CollectionUtils.isEmpty(products)) {
				setAttr("errorMessage", "商品规格不能为空！");
				render("/admin/common/error.ftl");
				return;
			}
			goodsService.update(goods, products, admin);
		} else {
			if (product == null) {
				//redirect(ERROR_VIEW);
				setAttr("errorMessage", "商品规格不能为空！");
				render("/admin/common/error.ftl");
				return;
			}
			goodsService.update(goods, product, admin);
		}

		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		String typeName = getPara("type");
		Goods.Type type = StrKit.notBlank(typeName) ? Goods.Type
				.valueOf(typeName) : null;
		Long productCategoryId = getParaToLong("productCategoryId");
		Long brandId = getParaToLong("brandId");
		Long promotionId = getParaToLong("promotionId");
		Long tagId = getParaToLong("tagId");
		Boolean isMarketable = getParaToBoolean("isMarketable");
		Boolean isList = getParaToBoolean("isList");
		Boolean isTop = getParaToBoolean("isTop");
		Boolean isOutOfStock = getParaToBoolean("isOutOfStock");
		Boolean isStockAlert = getParaToBoolean("isStockAlert");
		Pageable pageable = getBean(Pageable.class);

		ProductCategory productCategory = productCategoryService
				.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		Tag tag = tagService.find(tagId);
		setAttr("types", Goods.Type.values());
		setAttr("productCategoryTree", productCategoryService.findTree());
		setAttr("brands", brandService.findAll());
		setAttr("promotions", promotionService.findAll());
		setAttr("tags", tagService.findList(Tag.Type.goods));
		setAttr("type", type);
		setAttr("productCategoryId", productCategoryId);
		setAttr("brandId", brandId);
		setAttr("promotionId", promotionId);
		setAttr("tagId", tagId);
		setAttr("isMarketable", isMarketable);
		setAttr("isList", isList);
		setAttr("isTop", isTop);
		setAttr("isOutOfStock", isOutOfStock);
		setAttr("isStockAlert", isStockAlert);
		setAttr("pageable", pageable);
		setAttr("page", goodsService.findPage(type, productCategory, brand,
				promotion, tag, null, null, null, isMarketable, isList, isTop,
				isOutOfStock, isStockAlert, null, null, pageable));
		render("/admin/goods/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		if (ids != null) {
			for (Long id : ids) {
				Product product = productService.findByGoodsId(id);
				if (product != null && 0 < product.getStock()) {
					renderJson(Message.error(
							"admin.goods.deleteExistNotAllowed",
							product.getName()));
					return;
				}
				//根据产品ID，删除库存为商品的库存
				if (product != null &&  product.getStock()==0) {
					//System.out.println("ssssssssssss---id->"+id);
					//System.out.println("ssssssssssss---product->"+product.getId());
					List<StockLog> stockLogs = new ArrayList<StockLog>();
					stockLogs=product.getStockLogs();
					for(StockLog temp_stockLog:stockLogs){
					stockLogService.delete(temp_stockLog);
					}
				}
				
			}
			goodsService.delete(ids);
		}
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 根据类型获取验证组
	 * 
	 * @param type
	 *            类型
	 * @return 验证组
	 */
	// private Class<?> getValidationGroup(Goods.Type type) {
	// Assert.notNull(type);
	//
	// switch (type) {
	// case general:
	// return Product.General.class;
	// case exchange:
	// return Product.Exchange.class;
	// case gift:
	// return Product.Gift.class;
	// }
	// return null;
	// }

	/**
	 * FormBean - 商品
	 * 
	 * 
	 */
	// public static class ProductForm {
	//
	// /** 商品 */
	// private Product product;
	//
	// /**
	// * 获取商品
	// *
	// * @return 商品
	// */
	// public Product getProduct() {
	// return product;
	// }
	//
	// /**
	// * 设置商品
	// *
	// * @param product
	// * 商品
	// */
	// public void setProduct(Product product) {
	// this.product = product;
	// }
	//
	// }

	/**
	 * FormBean - 商品
	 * 
	 * 
	 */
	// public static class ProductListForm {
	//
	// /** 商品 */
	// private List<Product> productList;
	//
	// /**
	// * 获取商品
	// *
	// * @return 商品
	// */
	// public List<Product> getProductList() {
	// return productList;
	// }
	//
	// /**
	// * 设置商品
	// *
	// * @param productList
	// * 商品
	// */
	// public void setProductList(List<Product> productList) {
	// this.productList = productList;
	// }
	//
	// }

}