package com.jfinalshop.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.template.stat.ast.Return;
import com.jfinalshop.Pageable;
import com.jfinalshop.dao.ReturnsItemDao;
import com.jfinalshop.model.Admin;
import com.jfinalshop.model.OrderItem;
import com.jfinalshop.model.Returns;
import com.jfinalshop.model.ReturnsItem;
import com.jfinalshop.service.AdminService;
import com.jfinalshop.service.ReturnsService;
import com.jfinalshop.util.DateUtils;

/**
 * Controller - 退货单
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/returns")
public class ReturnsController extends BaseController {

	private ReturnsService returnsService = enhance(ReturnsService.class);
	private AdminService adminService = enhance(AdminService.class);

	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		setAttr("returns", returnsService.find(id));
		render("/admin/returns/view.ftl");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		
		setAttr("pageable", pageable);
		setAttr("page", returnsService.findPage(pageable));
		render("/admin/returns/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		returnsService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}

	/**
	 * 审核
	 */
	public void review() {
		Long id = getParaToLong("id");
		
		System.out.println(id);
		
		ReturnsItemDao returnsItemDao = new ReturnsItemDao();
		ReturnsItem returnsItem = returnsItemDao.find(id);
		
		
		returnsItem.setStatus(ReturnsItem.Status.completed.ordinal());
		returnsItem.update();
		
		OrderItem orderItem = OrderItem.dao.findById(returnsItem.getOrderItemId());
		orderItem.setModifyDate(DateUtils.getSysDate());
		orderItem.setVersion(orderItem.getVersion()+1);
		orderItem.setOrderItemStatus(7);//退款成功
		orderItem.update();
		Returns returns = returnsService.find(returnsItem.getReturnId());
		
		Admin admin = adminService.getCurrent();
		returns.setOperator(admin);
		returns.setModifyDate(DateUtils.getSysDate());
		returns.update();
		setAttr("returns", returns);
		render("/admin/returns/view.ftl");
	}
	
}