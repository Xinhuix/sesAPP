package com.jfinalshop.dao;

import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.model.DepositLog;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.SCardmonth;
import com.jfinalshop.model.SCardorder;

/**
 * Dao - 月卡记录
 * 
 * 
 */
public class SCardorderDao extends BaseDao<SCardorder> {

	/**
	 * 构造方法
	 */
	public SCardorderDao() {
		super(SCardorder.class);
	}

	/**
	 * 查找月卡订单分页
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param isExchange
	 *            是否显示退款申请记录
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 优惠券分页
	 */
	public Page<SCardorder> findPage(String isEnabled, Boolean isExchange,
			Boolean hasExpired, Pageable pageable) {
		String sqlExceptSelect = "FROM s_cardorder WHERE 1 = 1 ";
		if (isEnabled != null) {
			if(isEnabled.equals("6")){
				sqlExceptSelect += " AND status='" + isEnabled+"'";
			}else{
				sqlExceptSelect += " AND status in('" + isEnabled+"','5')";
			}
		}
		
		if (isExchange ) {//是否显示退款申请记录
			sqlExceptSelect += " AND status in('0','1','2','3')";
		}
		
		return super.findPage(sqlExceptSelect, pageable);
	}

	/**
	 * 查找月卡订单记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 月卡订单记录分页
	 */
	public Page<SCardorder> findPageList(Member member, Pageable pageable) {
		if (member == null) {
			return null;
		}
		String sqlExceptSelect = "FROM s_cardorder WHERE   status<>'0'    and   member_id = " + member.getId();
		return super.findPage(sqlExceptSelect, pageable);
	}
	

/**
 * 计算 status：状态的总数
 * @param member
 * @param status
 * @return
 */
	public Long count(Member member, String status) {
		String sqlExceptSelect = "FROM s_cardorder o WHERE 1 = 1 ";
		if (member != null) {
			sqlExceptSelect += " AND o.member_id = " + member.getId();
		}
		if (status != null) {
			sqlExceptSelect += " AND o.`status` ='" + status+"'";
		}
		return super.count(sqlExceptSelect);
	}
	
	
	
}