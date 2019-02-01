package com.jfinalshop.controller.usercenter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Message;
import com.jfinalshop.model.OrderItem;
import com.jfinalshop.model.client.LogisticsSTUC;
import com.jfinalshop.model.client.PushMessageSTUC2;
import com.jfinalshop.util.GetIpUtil;
@ControllerBind(controllerKey="/News")
public class News extends Controller{
		
	/**
	 * 用户通知
	 * 需要用户:id
	 */
	public void UserMessage() {
		String userid=getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			//消息
			List<Message> message =Message.dao.find("select me.id as meid,me.title as title,me.content as content,me.create_date as createDate,me.receiver_read as receiverRead,\r\n" + 
					"me.messageimgurl as messageimgurl,me.messagefrom as messagefrom,me.goods_id as goodsId,me.member_id as memberId,\r\n" + 
					"mr.id as mrid,\r\n" + 
					"gs.id as gsid,gs.image as image,gs.name as name,gs.goods_url as goodsUrl FROM\r\n" + 
					"message me LEFT JOIN\r\n" + 
					"member mr ON me.receiver_id=mr.id\r\n" + 
					"LEFT JOIN goods gs ON me.goods_id=gs.id WHERE mr.id=? ORDER BY me.create_date desc",userid);
			//广告
		List<Message>	ads = Message.dao.find("select  create_date as createDate,content,title,messageimgurl from message where messagefrom=1");
			map.put("message", message);
			map.put("ads", ads);
			map.put("status", 1);
			this.renderJson(map);
			
		} catch (Exception e) {
			map.put("status", 0);
			this.renderJson(map);
		}
		
	}
	
	/**
	 * 发送通知  
	 * 需要用户的usname
	 */
	public void addNews() {
		
		String userName = getPara("name");
		Integer from = getParaToInt("from");//推送形式
		Long  goodsId = getParaToLong("goodsid");//商品id
		String imgurl = getPara("imgurl");//图片路径
		String title = getPara("title");//标题
		String content = getPara("content");//内容
		if(TextUtils.isEmpty(userName)||from==null||TextUtils.isEmpty(title)||TextUtils.isEmpty(content)) {
			this.renderJson("error");
			return;
		}
		Member member = Member.dao.findFirst("select * from member where username=?",userName);
		Map<String,Object> map = new HashMap<>();
		try {
		Date date = new Date();	
		Message message = new Message();
		message.setCreateDate(date);//创建时间
		message.setModifyDate(date);//修改时间
		message.setVersion(0L);     //版本号
		message.setContent(content);//内容
		message.setIp(new GetIpUtil().getIpAddress(getRequest()));//ip
		message.setIsDraft(false);		  //是否存草稿
		message.setReceiverDelete(false); //收件人是否删除
		message.setReceiverRead(false);   //收件人是否已读	
		message.setSenderDelete(false);   //发件人是否删除
		message.setSenderRead(true);	  //发件人是否已读
		message.setTitle(title);  	 	  //标题
		message.setReceiverId(member.getId()); //收件人id
		if(imgurl==null) {
			message.setMessageimgurl(null);
		}else {
			message.setMessageimgurl(imgurl);//图片路径
		}
		message.setMessagefrom(PushMessageSTUC2.Trantype.getName(from));  //推送形式
		if(goodsId==null) {
			message.setGoodsId(null);
		}else {
			message.setGoodsId(goodsId);
		}
		message.save();
		
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
			System.out.println(e);
		map.put("status", 0);
		this.renderJson(map);
		}
		
	}
	
	

	
	
	/**
	 * 用户物流
	 * 需要用户唯一标识符：id
	 */
	public void UserLogistics() {
		String userid = getPara("id");
		
		String cp =getPara("cp");
		int page =0;
		 if(!TextUtils.isEmpty(cp)) {
			 page=Integer.parseInt(cp);
			 }
		 //计算消息总数
		 Map<String, Object> map=new HashMap<String,Object>();
		 try {
		
				List<OrderItem> logistics =OrderItem.dao.find("select orm.id as ormid,orm.create_date as createDate,orm.order_item_status,orm.Remarks,\r\n" + 
						"gs.`name` as name,gs.caption,gs.image as image,gs.goods_url as  goodeUrl,spg.tracking_no as trackingNo, \r\n" + 
						"oer.id as oerid\r\n" + 
						"FROM\r\n" + 
						"order_item orm LEFT JOIN\r\n" + 
						"`order` oer ON orm.order_id=oer.id\r\n" + 
						"LEFT JOIN member mr ON oer.member_id=mr.id\r\n" + 
						"LEFT JOIN product pt ON orm.product_id=pt.id\r\n" +  
						"LEFT JOIN	goods gs ON pt.goods_id=gs.id\r\n" + 
						"LEFT JOIN shipping spg ON spg.order_id=oer.id\r\n" + 
						"WHERE mr.id="+userid+" ORDER BY orm.create_date desc limit "+(page*5)+",5");
				/*if(totalpage==0) {
					this.renderJson("您还没有物流消息");
					return;
				}*/
				System.out.println(logistics);
				for(int i =0;i<logistics.size();i++) {
					logistics.get(i).setRemarks(LogisticsSTUC.Trantype.getName(logistics.get(i).getOrderItemStatus()));
				}
				map.put("logistics", logistics);
				map.put("status", 1);
				this.renderJson(map);
		} catch (Exception e) {
				map.put("status", 0);
				this.renderJson(map);
		}
		 
		
	}
}
