package com.marks.common.enums;

public class FeeEnums {

	public enum FeeCode {
		consume("101", "消费"), //
		recharge("102", "充值"), //
		purchase("103", "采购"), //
		;// 会员

		private String status;
		private String name;

		private FeeCode(String status, String name) {
			this.status = status;
			this.name = name;
		}

		public static String getByKey(String status) {
			for (FeeCode c : FeeCode.values()) {
				if (c.getValue().equals(status)) {
					return c.name;
				}
			}
			return "";
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return status;
		}
	}

	public enum ItemCode {
		consume("1011", "购买订单"), //
		recharge("1021", "会员充值"), //
		purchase("1031", "采购订单"), //
		;// 会员

		private String status;
		private String name;

		private ItemCode(String status, String name) {
			this.status = status;
			this.name = name;
		}

		public static String getByKey(String status) {
			for (ItemCode c : ItemCode.values()) {
				if (c.getValue().equals(status)) {
					return c.name;
				}
			}
			return "";
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return status;
		}
	}

}
