package com.marks.common.enums;

public class Enums {

	public enum Status {
		Enable(1, "启用"), // 绑定
		Unable(0, "禁用");// 未绑定

		private int status;
		private String name;

		private Status(int status, String name) {
			this.status = status;
			this.name = name;
		}

		public String toString() {
			return String.valueOf(status);
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return status;
		}

		public static String getByKey(int status) {
			for (Status c : Status.values()) {
				if (c.getValue() == status) {
					return c.getName();
				}
			}
			return "";
		}
	}

	public enum CheckStatus {
		noCheck(0, "未审核"), //
		checking(3, "审核中"), //
		checkFail(2, "审核不通过"), //
		checkOk(1, "审核通过"), //
		;

		private int status;
		private String name;

		private CheckStatus(int status, String name) {
			this.status = status;
			this.name = name;
		}

		public String toString() {
			return String.valueOf(status);
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return status;
		}

		public static String getByKey(int status) {
			for (CheckStatus c : CheckStatus.values()) {
				if (c.getValue() == status) {
					return c.getName();
				}
			}
			return "";
		}
	}

}
