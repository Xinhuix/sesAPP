package com.jfinalshop.model.client;



public class MessageNotification {

	
	public enum Order {
	        T01("下单成功", 1), T02("已发货", 2), T03("已签收", 4), T04("退款成功", 7);
	        // 成员变量
	        private String name;
	        private Integer index;
	        
	        // 构造方法
	        private Order(String name, Integer index) {
	            this.name = name;
	            this.index = index;
	        }
	        
	        // 普通方法
	        public static String getName(Integer index,String string) {
	            for (Order c : Order.values()) {
	                if (c.getIndex().equals(index)) {
	                    return "您购买的:"+string+""+c.getName()+"。";
	                }
	            }
	            return null;
	        }


			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}



			public Integer getIndex() {
				return index;
			}



			public void setIndex(Integer index) {
				this.index = index;
			}
	        
	        // 覆盖方法
	        @Override
	        public String toString() {
	            return this.index + "_" + this.name;
	        }
	    }
	
	public enum sesCardpackage {
        T01("卡包还未付款", 1), T02("以及支付成功", 2), T03("卡包已经到期,请处理", 3);
        // 成员变量
        private String name;
        private Integer index;
        
        // 构造方法
        private sesCardpackage(String name, Integer index) {
            this.name = name;
            this.index = index;
        }
        
        // 普通方法
        public static String getNames(Integer index,String string) {
            for (sesCardpackage c : sesCardpackage.values()) {
                if (c.getIndex().equals(index)) {
                    return "您购买的:"+string+""+c.getName()+"。";
                }
            }
            return null;
        }


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}



		public Integer getIndex() {
			return index;
		}



		public void setIndex(Integer index) {
			this.index = index;
		}
        
        // 覆盖方法
        @Override
        public String toString() {
            return this.index + "_" + this.name;
        }
    }
}
