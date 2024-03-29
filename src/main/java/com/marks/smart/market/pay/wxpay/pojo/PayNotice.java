package com.marks.smart.market.pay.wxpay.pojo;

import java.util.Date;

/**
 * 支付通知
 * @author lhyan3
 * 2015年4月15日上午8:28:56
 */
public class PayNotice {
	
	private String appid;//微信分配的公众账号ID
	
	private String mch_id;//微信支付分配的商户号
	
	private String device_info;//微信支付分配的终端设备号
	
	private String result_code;//业务结果SUCCESS/FAIL
	
	private String err_code;//错误代码
	
	private String openid;//用户标识
	
	private String is_subscribe;//是否关注公众账号
	
	private String trade_type;//交易类型
	
	private String bank_type;//付款银行
	
	private String total_fee;//总金额
	
	private String fee_type;//货币种类
	
	private Integer cash_fee;//现金支付金额
	
	private String cash_fee_type;//现金支付货币类型
	
	private Integer coupon_fee;//代金券或立减优惠金额
	
	private Integer coupon_count;//代金券或立减优惠使用数量
	
	private String coupon_batch_id_index;//代金券或立减优惠批次ID
	
	private String coupon_id_index;//代金券或立减优惠ID
	
	private String coupon_fee_index;//单个代金券或立减优惠支付金额
	
	private String transaction_id;//微信支付订单号
	
	private String out_trade_no;//商户订单号
	
	private String attach;//商家数据包
	
	private String time_end;//支付完成时间
	
	private String payLocation;//支付路径方式 1正常支付 2扫码支付
	
	private Date recodeTime;//记录时间

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public Integer getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(Integer cash_fee) {
		this.cash_fee = cash_fee;
	}

	public String getCash_fee_type() {
		return cash_fee_type;
	}

	public void setCash_fee_type(String cash_fee_type) {
		this.cash_fee_type = cash_fee_type;
	}

	public Integer getCoupon_fee() {
		return coupon_fee;
	}

	public void setCoupon_fee(Integer coupon_fee) {
		this.coupon_fee = coupon_fee;
	}

	public Integer getCoupon_count() {
		return coupon_count;
	}

	public void setCoupon_count(Integer coupon_count) {
		this.coupon_count = coupon_count;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTime_end() {
		return time_end;
	}
	
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getCoupon_batch_id_index() {
		return coupon_batch_id_index;
	}

	public void setCoupon_batch_id_index(String coupon_batch_id_index) {
		this.coupon_batch_id_index = coupon_batch_id_index;
	}

	public String getCoupon_id_index() {
		return coupon_id_index;
	}

	public void setCoupon_id_index(String coupon_id_index) {
		this.coupon_id_index = coupon_id_index;
	}

	public String getCoupon_fee_index() {
		return coupon_fee_index;
	}

	public void setCoupon_fee_index(String coupon_fee_index) {
		this.coupon_fee_index = coupon_fee_index;
	}
	public Date getRecodeTime() {
		return recodeTime;
	}
	public void setRecodeTime(Date recodeTime) {
		this.recodeTime = recodeTime;
	}
	public String getPayLocation() {
		return payLocation;
	}
	public void setPayLocation(String payLocation) {
		this.payLocation = payLocation;
	}
	
	

}
