package com.jfinalshop.controller.wap;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Order;
import com.jfinalshop.model.AdPosition;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.Payment;
import com.jfinalshop.model.PaymentLog;
import com.jfinalshop.service.AdPositionService;
import com.jfinalshop.service.GoodsService;
import com.jfinalshop.service.PaymentLogService;
import com.jfinalshop.util.SystemUtils;

@ControllerBind(controllerKey = "/wap")
public class IndexController extends BaseController {

	private AdPositionService adPositionService = enhance(AdPositionService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	PaymentLogService plService=enhance(PaymentLogService.class);

	/**
	 * 首页
	 */
	public void index() {
		try {
			List<Order> orders = new ArrayList<Order>();
			AdPosition adPosition = adPositionService.find(1L);
			//现货：
			List<Goods> goodsList = goodsService.findList(Goods.Type.general, null,
					null, null, null, null, null, null, true, true, null, null,
					null, null, null, 6, null, orders, false,"0");
			//定制：
			List<Goods> goodsList_dz = goodsService.findList(Goods.Type.general, null,
					null, null, null, null, null, null, true, true, null, null,
					null, null, null,6, null, orders, false,"1");
			//推荐：
			Long tagId = new Long((long)3);
			List<Goods> goodsList_tag = goodsService.findList(Goods.Type.general, null,
					null, null, tagId, null, null, null, true, true, null, null,
					null, null, null, 6, null, orders, false,null);
			
			if(SystemUtils.getSetting().getHotline()!=null){ //在线客服
				setAttr("hotline", SystemUtils.getSetting().getHotline());
			}else{
				setAttr("hotline", "");
			}
			
			setAttr("adPosition", adPosition);
			setAttr("goodsList", goodsList); //现货
			setAttr("goodsList_dz", goodsList_dz);//定制
			setAttr("goodsList_tag", goodsList_tag);//热门推荐
			setAttr("footer", "wap");//footer 的当前
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		render("/wap/index.ftl");
	}

}
