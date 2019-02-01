package com.jfinalshop.service;

import java.util.Date;
import java.util.List;

import com.jfinalshop.model.OrderItem;

/**
 * Service - 订单项
 * 
 * 
 */
public class OrderItemService extends BaseService<OrderItem> {

	/**
	 * 构造方法
	 */
	public OrderItemService() {
		super(OrderItem.class);
	}
	/**
	 * 发货 修改订单项状态
	 * @param orderItem
	 */
	public void updateStatus(OrderItem orderItem) {
		if(orderItem==null) {
			System.out.println("订单项空的");
			throw new IllegalArgumentException();
		}else {
			OrderItem Item= new OrderItem();
			Item.setId(orderItem.getId());
			Item.setModifyDate(new Date());
			Item.setVersion(orderItem.getVersion()+1);
			Item.setIsRead(2);
			Item.setOrderItemStatus(2);
			Item.update();
		}
		
	}
	
	/**
	 * 判断订单项都已收货
	 */
	public static boolean AllisReceipt(List<OrderItem> arr, Integer targetValue) {
        for (OrderItem s : arr) {
            if (s.getOrderItemStatus()==targetValue)
                return false;
        }
        return true;
    }
	
}