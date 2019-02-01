package com.jfinalshop.model.client;

public class PointLogSTUC {
	

	    public enum Trantype {
	    	T00("公分赠送", 0),T01("公分兑换", 1), T02("公分兑换撤销", 2), T03("公分调整", 3), T04("提现申请", 4), T05("公分兑换", 5), T06("推荐公分", 6);
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
