package com.jfinalshop.model;


import com.jfinalshop.model.base.BaseDepositLog;
import com.jfinalshop.util.ObjectUtils;

/**
 * Model - 预存款记录
 * 
 * 
 */
public class DepositLog extends BaseDepositLog<DepositLog> {
	private static final long serialVersionUID = -3810632592610442339L;
	public static final DepositLog dao = new DepositLog();
	
	/**
	 * 类型
	 */
	public enum Type {

		/** 预存款充值 */
		recharge("预存款充值"),

		/** 预存款调整 */
		adjustment("预存款调整"),

		/** 订单支付 */
		payment("订单支付"),

		/** 订单退款 */
		refunds("订单退款"),
		/** 积分充值 */
		pionts("积分充值");
		private String name;
		Type(String name) {
			this.name=name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	/** 会员 */
	private Member member;

	/**
	 * 类型名称
	 */
	public DepositLog.Type getTypeName() {
		return DepositLog.Type.values()[getType()];
	}
	
	/**
	 * 类型名称
	 */
	public String getTypeNameValue() {
		String value;
		switch (getTypeName()) {
		case recharge:
			value = "预存款充值";
			break;
		case adjustment:
			value = "预存款调整";
			break;
		case payment:
			value = "订单支付";
			break;
		case refunds:
			value = "订单退款";
			break;
		default:
			value = "未知类型";
			break;
		}
		return value;
	}
	
	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public Member getMember() {
		if (ObjectUtils.isEmpty(member)) {
			member = Member.dao.findById(getMemberId());
		}
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}
	
	/**
	 * 设置操作员
	 * 
	 * @param operator
	 *            操作员
	 */
	public void setOperator(Admin operator) {
		setOperator(operator != null ? operator.getUsername() : null);
	}
}
