package com.jfinalshop.model.client;

public class CommonSTUC {
		private  int status;
		private  String code;
	
		public enum Status{
			 Success(1),Fail(2),Error(3),LackPara(4),ParaWrong(5),Exist(6),NoExist(7),Illegal(8),OutTime(9),CodeError(10),limitOut(11),AmountWrong(12),MKFail(13)
			 ,Received(14),InComplete(15);
			
			 private 	int index;
			 
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

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
		
		
}
