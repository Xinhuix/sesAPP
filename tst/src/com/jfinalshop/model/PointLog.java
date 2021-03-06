package com.jfinalshop.model;

import com.jfinalshop.model.base.BasePointLog;
import com.jfinalshop.util.ObjectUtils;

/**
 * Model - 积分记录
 * 
 * 
 */
public class PointLog extends BasePointLog<PointLog> {
	private static final long serialVersionUID = -3444473497099804707L;
	public static final PointLog dao = new PointLog();
	
	/**
	 * 类型
	 */
	public enum Type {

		/** 积分赠送 */
		reward("积分赠送"),
		/** 积分兑换 */exchange("积分兑换"),
		/** 积分兑换撤销 */undoExchange("积分兑换撤销"),
		/** 积分调整 */adjustment("积分调整"),
		/** 积分提现 */cash("积分提现"),
		/** 积分转成余额 */amount("积分转余额"),
		/** 积分推荐积分 */recommend("推荐积分");
		private String name;
		Type(String name){
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
	
	/** 类型 */
	private PointLog.Type typeName;
	
	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public PointLog.Type getTypeName() {
		if (typeName == null) {
			typeName = PointLog.Type.values()[getType()];
		}
		return typeName;
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
