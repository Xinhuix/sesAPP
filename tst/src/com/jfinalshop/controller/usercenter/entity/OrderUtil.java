package com.jfinalshop.controller.usercenter.entity;

import java.util.List;

import com.jfinalshop.model.Product;

public class OrderUtil {
	
		private List<ProductUtil> productId;
		private String 		  memo;
		private String 	  memberId;
		private String 	  receiverId;
		private Integer   paymentamount;
		

		public List<ProductUtil> getProductId() {
			return productId;
		}


		public void setProductId(List<ProductUtil> productId) {
			this.productId = productId;
		}


		public String getMemo() {
			return memo;
		}


		public void setMemo(String memo) {
			this.memo = memo;
		}


		public String getMemberId() {
			return memberId;
		}


		public void setMemberId(String memberId) {
			this.memberId = memberId;
		}


		public String getReceiverId() {
			return receiverId;
		}


		public void setReceiverId(String receiverId) {
			this.receiverId = receiverId;
		}


		public Integer getPaymentamount() {
			return paymentamount;
		}


		public void setPaymentamount(Integer paymentamount) {
			this.paymentamount = paymentamount;
		}

        
			
		

		@Override
		public String toString() {
			return "OrderUtil [productId=" + productId + ", memo=" + memo + ", memberId=" + memberId + ", receiverId="
					+ receiverId + ", paymentamount=" + paymentamount + "]";
		}


		public OrderUtil() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
		
		
}


