package com.jfinalshop.controller.usercenter.entity;

import java.util.Date;

public class SrecommendUtil {
		private String avatar; //头像
		private Date   createDate; //创建时间
		private String username; //用户手机号
		private Long size; //用户推荐的人数
		public String getAvatar() {
			return avatar;
		}
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}
		public Date getCreateDate() {
			return createDate;
		}
		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public Long getSize() {
			return size;
		}
		public void setSize(Long size) {
			this.size = size;
		}
		@Override
		public String toString() {
			return "SrecommendUtil [avatar=" + avatar + ", createDate=" + createDate + ", username=" + username
					+ ", size=" + size + "]";
		}
		public SrecommendUtil() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
		
}
