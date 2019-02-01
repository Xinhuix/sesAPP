package com.jfinalshop.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinalshop.entity.ProductImage;
import com.jfinalshop.model.base.BaseAgriculturalProduction;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class AgriculturalProduction extends BaseAgriculturalProduction<AgriculturalProduction> {
	public static final AgriculturalProduction dao = new AgriculturalProduction().dao();
	
	/** 商品图片 */
	private List<ProductImage> productImages = new ArrayList<ProductImage>();
	
	/**
	 * 获取商品图片
	 * 
	 * @return 商品图片
	 */
	public List<ProductImage> getProductImagesConverter() {
		if (CollectionUtils.isEmpty(productImages)) {
			JSONArray productImageArrays = JSONArray.parseArray(getProductImages());
			if (CollectionUtils.isNotEmpty(productImageArrays)) {
				for (int i = 0; i < productImageArrays.size(); i++) {
					productImages.add(JSONObject.parseObject(productImageArrays.getString(i), ProductImage.class));
				}
			}
		}
		return productImages;
	}
	
	

	/**
	 * 设置商品图片
	 * 
	 * @param productImages
	 *            商品图片
	 */
	public void setProductImagesConverter(List<ProductImage> productImages) {
		this.productImages = productImages;
	}
}