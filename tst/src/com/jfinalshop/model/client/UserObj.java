package com.jfinalshop.model.client;

import com.jfinalshop.model.Member;

public class UserObj {
		private Member m;
		private int status;
		
		public enum  Status{
			Success(1),Fail(2),Error(3),NoExist(4);
			private int index;
			Status(int index){
				this.index=index;
			}
			public int getIndex() {
				return index;
			}
			public void setIndex(int index) {
				this.index = index;
			}
			
		}
		
		

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public Member getM() {
			return m;  
		}

		public void setM(Member m) {
			this.m = m;
		}
		
		
		
		
}
