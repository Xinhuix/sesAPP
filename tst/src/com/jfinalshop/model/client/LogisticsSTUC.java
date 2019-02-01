package com.jfinalshop.model.client;

public class LogisticsSTUC {
	

	    public enum Trantype {
	    	T01("订单待发货", 1), T02("订单已发货", 2), T03("订单已签收", 4), T04("已申请退款", 6), T05("退款成功", 7);
	        // 成员变量
	        private String name;
	        private Integer index;

	        // 构造方法
	        private Trantype(String name, Integer index) {
	            this.name = name;
	            this.index = index;
	        }

	        // 普通方法
	        public static String getName(Integer index) {
	            for (Trantype c : Trantype.values()) {
	                if (c.getIndex().equals(index)) {
	                    return c.name;
	                }
	            }
	            return null;
	        }

	        // get set 方法
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
