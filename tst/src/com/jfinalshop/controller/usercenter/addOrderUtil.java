package com.jfinalshop.controller.usercenter;

import java.awt.Image;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.http.util.TextUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinalshop.Setting;
import com.jfinalshop.dao.OrderLogDao;
import com.jfinalshop.dao.SnDao;
import com.jfinalshop.model.DeliveryCorp;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.OrderItem;
import com.jfinalshop.model.OrderLog;
import com.jfinalshop.model.Payment;
import com.jfinalshop.model.Product;
import com.jfinalshop.model.Receiver;
import com.jfinalshop.model.Returns;
import com.jfinalshop.model.ReturnsItem;
import com.jfinalshop.model.ShippingMethod;
import com.jfinalshop.model.Sn;
import com.jfinalshop.model.Sn.Type;
import com.jfinalshop.service.MailService;
import com.jfinalshop.service.OrderService;
import com.jfinalshop.service.SmsService;

public class addOrderUtil {

	/**
	 * 添加订单
	 * @param date 时间
	 * @param receiver 地址
	 * @param memberId 会员id
	 * @param memo 备注
	 * @param amount 付款金额
	 * @param totalQuantity 总数量
	 * @param totalWeight 总重量
	 * @param Paymentamount 总价钱
	 * @return
	 */
	public Order addOrder(Date date,Receiver receiver,String memberId,String memo,int amount,int totalQuantity,int totalWeight,Integer Paymentamount) {
				
			  System.out.println("来了吗？？");
				Order order= new Order();
				//创建时间
				order.setCreateDate(date);
				//修改时间
				order.setModifyDate(date);
				//版本号
				order.setVersion(0L);
				//详细地址
				order.setAddress(receiver.getAddress());
				//地区
				order.setAreaName(receiver.getAreaName());
				//收件人
				order.setConsignee(receiver.getConsignee());
				//电话
				order.setPhone(receiver.getPhone());
				//订单需要付款金额？？？
				//int amount= 3+(5*goods.getPrice().intValue());
				order.setAmount( new BigDecimal(amount)); //商品的数量乘以单价,然后在累加

				//已付款金额？？？
				order.setAmountPaid(BigDecimal.ZERO);
				//完成日期？？？
				order.setCompleteDate(null);
				//优惠价折扣？
				order.setCouponDiscount(BigDecimal.ZERO);
				//兑换积分？
				order.setExchangePoint(0L);
				//手续费？
				order.setFee(BigDecimal.ZERO);
				//运费？
				order.setFreight(BigDecimal.ZERO);
				
				//是否已分配库存?
				order.setIsAllocatedStock(true);
				//是否已兑换积分?
				order.setIsExchangePoint(false);
				//是否使用优惠吗？
				order.setIsUseCouponCode(false);
				//锁定过期日期?
				order.setLockExpire(null);
				//锁定KEY?
				order.setLockKey(null);
				//备注
				if(TextUtils.isEmpty(memo)) {
					order.setMemo("");
				}else {
					order.setMemo(memo);
				}
				//调整金额??
				order.setOffsetAmount(BigDecimal.ZERO);
				//支付方式名称??
				order.setPaymentMethodName(null);
				//支付方式类型??
				order.setPaymentMethodType(null);
				//商品价格
				order.setPrice( new BigDecimal(amount)); //也就是订单项的全部的钱
				//数量
				order.setQuantity(totalQuantity); //也就是订单项的全部的数量
				//商品折扣？
				order.setPromotionDiscount(BigDecimal.ZERO);
				//促销名称
				order.setPromotionNames("[]");
				//退款金额？
				order.setRefundAmount(BigDecimal.ZERO);
				//退款数量?
				order.setReturnedQuantity(0);
				//赠送积分
				order.setRewardPoint(0L);
				
				//已发货数量
				order.setShippedQuantity(0);
				//快递
				order.setShippingMethodName("普通快递");
				//订单编号
				order.setSn(new SnDao().generate(Sn.Type.order));
				//订单状态
				order.setStatus(2);
				//税金
				order.setTax(BigDecimal.ZERO);
				//类型
				order.setType(Order.Type.general.ordinal());
				//重量
				order.setWeight(totalWeight);//也就是订单项的数量乘以重量 然后在加在一起
				//邮编
				order.setZipCode("");
				//地区id
				order.setAreaId(receiver.getAreaId());
				//对应会员的id
				order.setMemberId(Long.valueOf(memberId));
				//发货方式
				order.setShippingMethodId(1L);
				
				order.save();
				
				//Long  orderId= order.getId();
				
				
				
				
				return order;
	}
	/**
	 * 创建订单项
	 * @param date 时间
	 * @param name 商品名字
	 * @param price 单价
	 * @param image 图片
	 * @param orderId 订单id
	 * @param productId 商品项id
	 * @param Quantity 数量
	 * @param Weight 重量
	 */
	public void addOrderItem(Date date,String name,BigDecimal price,String image,String productId,String Quantity,Integer Weight,String dz,String sn,Order order) {
		
		//创建订单项对象
		 OrderItem orderItem = new OrderItem(); 
		 //订单项创建时间
		 orderItem.setCreateDate(date);
		 //订单修改时间
		 orderItem.setModifyDate(date);
		 //版本
		 orderItem.setVersion(0L);
		 //是否需要物流
		 orderItem.setIsDelivery(true);
		 //商品名字
		 orderItem.setName(name);//"商品名字"
		 //商品价钱
		 orderItem.setPrice(price); //商品单价
		 //商品数量
		 orderItem.setQuantity(Integer.valueOf(Quantity));//"商品数量"
		 //退货数量 生产默认是0 修改
		 orderItem.setReturnedQuantity(0);
		 //已发货数量  生产默认是0 修改
		 orderItem.setShippedQuantity(0);
		 //编号
		 orderItem.setSn(sn);
		 //规格
		 orderItem.setSpecifications("[]");
		 //商品缩略图
		 if(image==null) {
			 orderItem.setThumbnail(null);
		 }else {
			 orderItem.setThumbnail(image);//"商品的图片"
		}
		 
		 //类型
		 if(dz==null) {
			 orderItem.setType(null);
		 }else {
			 orderItem.setType(Integer.valueOf(dz));
		}
		 //重量
		 if(Weight==null) {
			 orderItem.setWeight(null);
		 }else {
			 orderItem.setWeight(Weight);//"商品重量"
		}
		
		 //是否评论
		 orderItem.setIsReview(false);
		 //对应订单的id
		 orderItem.setOrderId(order.getId());
		 //对应商品的id
		 orderItem.setProductId(Long.valueOf(productId));//"商品项id"
		 //订单状态
		 orderItem.setOrderItemStatus(1);
		 //是否需要运费
		 orderItem.setOrderItemFreight(0);
		 //是否已读
		 orderItem.setIsRead(0);
		 //是否删除
		 orderItem.setIsDelete(0);
		 
		 orderItem.save();
		 
		
	}
	
	/**
	 * 添加退货
	 * @param date 退货时间
	 * @param oerid 订单id
	 * @param mo 备注
	 * @return
	 */
	public Returns addreturns(Date date,String oerid,String mo) {

		
		Order order = Order.dao.findById(oerid);
		Returns returns = new Returns();
		
		
		//创建时间
		returns.setCreateDate(date);
		//修改时间
		returns.setModifyDate(date);
		//版本号
		returns.setVersion(0L);
		//地址
		returns.setAddress(order.getAddress());
		//详细地址
		returns.setArea(order.getArea());
		//物流
		DeliveryCorp deliveryCorp= new DeliveryCorp();
		deliveryCorp.setId(order.getShippingMethodId());
		returns.setDeliveryCorp(deliveryCorp.getName());
		
		//快递费
		returns.setFreight(order.getFreight());
		//备注
		if(mo==null) {
			returns.setMemo("");
		}else {
			returns.setMemo(mo);
		}
		
		//操作员
		returns.setOperator("");
		//手机号
		returns.setPhone(order.getPhone());
		//发货人
		returns.setShipper(order.getConsignee());
		
		//配送方式？
		ShippingMethod shippingMethod= new ShippingMethod();
		shippingMethod.setId(order.getShippingMethodId());
		returns.setShippingMethod(shippingMethod.getDescription());
		
		//退单
		returns.setSn(new SnDao().generate(Type.returns));
		//运单号？？          
		returns.setTrackingNo(null);
		//邮编
		if(order.getZipCode()==null) {
			returns.setZipCode(null);
		}else {
			returns.setZipCode(order.getZipCode());
		}
		
		
		//对应订单id
		returns.setOrderId(order.getId());
		returns.save();
		
		return returns;
	}
	
	
	/**
	 * 退货项
	 * @param date 退货时间
	 * @param images 退货图片
	 * @param orderiemid 
	 * @param MemberId 对应会员id
	 * @param productId 商品项id
	 * @param returnsId 地址id
	 * @param desc 原因
	 * @param type 类型
	 * @param ReturnsMoney 退款金额
	 */
	public void addreturnsitem(Date date,String images,String orderiemid,String MemberId,String productId,Returns returnsId,String desc,int type,String ReturnsMoney) {
		
		ReturnsItem returnsItem = new ReturnsItem();
		
        OrderItem orderItem = OrderItem.dao.findById(orderiemid);	
		
		returnsItem.setCreateDate(date);
		returnsItem.setModifyDate(date);
		//版本号
		returnsItem.setVersion(0L);
		//图片
		if(images!=null) {
			returnsItem.setImages(images);
			
		}else {
			returnsItem.setImages("");
		}
		
		//用户描述
		if(desc!=null) {
			returnsItem.setDesc(desc);
		}else {
			returnsItem.setDesc("");
		}
		
		//申请原因
		returnsItem.setCause("原因？？");
		//数量
		returnsItem.setQuantity(orderItem.getQuantity());
		//退款金额
		if(ReturnsMoney==null) {
			returnsItem.setAmount(new BigDecimal(orderItem.getQuantity()*orderItem.getPrice().intValue()));
		}else {
			if(new BigDecimal(ReturnsMoney).compareTo(new BigDecimal(orderItem.getQuantity()*orderItem.getPrice().intValue())) ==1) {
				System.out.println("退款金额大于订单额");
				returnsItem.setAmount(new BigDecimal(ReturnsMoney));
			}else {
				returnsItem.setAmount(new BigDecimal(ReturnsMoney));
			}
			
		}
		
		
		//状态？
		returnsItem.setStatus(0);
		//退款类型(1：退货并退款，2：仅退款)
		returnsItem.setType(type);
		//商品名称
		returnsItem.setName(orderItem.getName());
		//对应订单项的编号
		returnsItem.setSn(orderItem.getSn());
		//规格？
		returnsItem.setSpecifications("[]");
		//用户id
		returnsItem.setMemberId(Long.valueOf(MemberId));
		//对应商品的id
		returnsItem.setProductId(Long.valueOf(productId));
		//退货单id
		returnsItem.setReturnId(returnsId.getId());
		//订单项id
		returnsItem.setOrderItemId(orderItem.getId());
		returnsItem.save();
	}
	
	/**
	 * 修改订单状态
	 * @param status 状态
	 * @param orderiemid 订单项id
	 * @param version 版本号
	 * @throws Exception
	 */
	public void updateOrderStatus(Long status,Long orderiemid,Long version) throws Exception {
		if(status==null) {
			throw new Exception();
		}if(status==1) {
			Db.update("update order_item set modify_date=SYSDATE(),version="+version+",order_item_status=5  where id=?",orderiemid);
			return;
		}if(status==2||status==3) {
			Db.update("update order_item set modify_date=SYSDATE(),version="+version+",order_item_status=6   where id=?",orderiemid);
		}else {
			System.out.println(status);
			throw new Exception();
		}
	}
	
	//判断图片是否合法
    /**
     * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片，这是一种非常简单的方式。
     * 
     * @param imageFile
     * @return
     */
    public static boolean isImage(File imageFile) {
        if (!imageFile.exists()) {
            return false;
        }
        Image img = null;
        try {
            img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }
    
    public void updateOrderandOrderItem(Integer status,OrderItem orderItem) throws Exception {
    	if(orderItem==null) {
    		throw new Exception("有空的订单或者订单项");
    	}
    	try {
    		//修改订单的退款数量
			Date date = new Date();
			Order order = Order.dao.findById(orderItem.getOrderId());
			order.setCompleteDate(date);
			order.setVersion(order.getVersion()+1);
			order.setReturnedQuantity(order.getReturnedQuantity()-orderItem.getReturnedQuantity());
			order.update();
			//修改订单项状态
			orderItem.setCreateDate(date);
			orderItem.setVersion(orderItem.getVersion()+1);
			if(status==5) {
				orderItem.setOrderItemStatus(1);
			}else if (status==6) {
				orderItem.setOrderItemStatus(2);
			}else {
				throw new Exception("状态不是5也不是6啊");
			}
			orderItem.setReturnedQuantity(0);
			orderItem.update();
		} catch (Exception e){
			throw new Exception("取消退款出错了");
		}
  
    }

}
