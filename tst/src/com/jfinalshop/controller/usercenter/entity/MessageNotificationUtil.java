package com.jfinalshop.controller.usercenter.entity;

public class MessageNotificationUtil {
		private Long    id;
		private String  name;
		private String  imgs;
		private String  status;
		
		
		
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getImgs() {
			return imgs;
		}
		public void setImgs(String imgs) {
			this.imgs = imgs;
		}
		
		 
		
		@Override
		public String toString() {
			return "MessageNotificationUtil [id=" + id + ", name=" + name + ", imgs=" + imgs + ", status=" + status
					+ "]";
		}
		public MessageNotificationUtil() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
}
