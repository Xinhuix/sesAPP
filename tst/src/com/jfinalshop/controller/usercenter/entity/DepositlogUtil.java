package com.jfinalshop.controller.usercenter.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.jfinalshop.model.PointLog;

public class DepositlogUtil {
	private Long id;
	private BigDecimal  balance; 	//当前积分
	private BigDecimal  credit;  	//获取积分
	private BigDecimal  debit;  	//扣除积分
	private Date  createDate; //创建时间
	private String memo;   	//备注
	private String type;    //类型
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getCredit() {
		return credit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	public BigDecimal getDebit() {
		return debit;
	}
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "PointlogUtil [id=" + id + ", balance=" + balance + ", credit=" + credit + ", debit=" + debit
				+ ", createDate=" + createDate + ", memo=" + memo + ", type=" + type + "]";
	}
	public DepositlogUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
