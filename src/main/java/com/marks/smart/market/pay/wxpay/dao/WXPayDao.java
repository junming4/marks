package com.marks.smart.market.pay.wxpay.dao;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.marks.smart.market.pay.wxpay.pojo.PayNotice;
import com.marks.smart.market.pay.wxpay.pojo.WXPayRecord;
@MapperScan
public interface WXPayDao{

	/**
	 * 保存支付结果通知
	 * lhyan3
	 * 2015年3月14日下午4:03:22
	 * TODO
	 * @param notice
	 */
	void savePayNotice(@Param("notice")PayNotice notice);

	/**
	 * 保存支付记录
	 * lhyan3
	 * 2015年4月16日下午3:43:57
	 * TODO
	 * @param record
	 */
	void savePayRecord(@Param("record")WXPayRecord record);

	/**
	 * 改变订单状态和支付状态
	 * lhyan3
	 * 2015年4月30日上午9:44:54
	 * TODO
	 * @param orderId
	 * @param pay
	 * @param status
	 */
	void updateOrderPayStatus(@Param("orderId")String orderId, @Param("pay")int pay,@Param("status") Integer status,@Param("payType")int paytype);

}
