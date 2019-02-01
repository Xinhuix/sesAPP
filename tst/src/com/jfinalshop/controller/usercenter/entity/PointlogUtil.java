package com.jfinalshop.controller.usercenter.entity;

import java.util.Date;

import com.jfinalshop.model.PointLog;

public class PointlogUtil {
	private Long id;
	private Long  balance; 	//当前积分
	private Long  credit;  	//获取积分
	private Long  debit;  	//扣除积分
	private Date  createDate; //创建时间
	private String memo;   	//备注
	private String type;    //类型
	private String status; //状态
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBalance() {
		return balance;
	}
	public void setBalance(Long balance) {
		this.balance = balance;
	}
	public Long getCredit() {
		return credit;
	}
	public void setCredit(Long credit) {
		this.credit = credit;
	}
	public Long getDebit() {
		return debit;
	}
	public void setDebit(Long debit) {
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "PointlogUtil [id=" + id + ", balance=" + balance + ", credit=" + credit + ", debit=" + debit
				+ ", createDate=" + createDate + ", memo=" + memo + ", type=" + type + ", status=" + status + "]";
	}
	public PointlogUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
