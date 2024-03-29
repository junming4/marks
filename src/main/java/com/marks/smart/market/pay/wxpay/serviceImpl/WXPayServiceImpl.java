package com.marks.smart.market.pay.wxpay.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marks.common.util.IDUtil;
import com.marks.smart.market.pay.wxpay.dao.WXPayDao;
import com.marks.smart.market.pay.wxpay.pojo.PayNotice;
import com.marks.smart.market.pay.wxpay.pojo.WXPayRecord;
import com.marks.smart.market.pay.wxpay.service.WXPayService;
@Service
public class WXPayServiceImpl implements WXPayService{
	 @Autowired
	private WXPayDao wxPayDao;


	public WXPayDao getWxPayDao() {
		return wxPayDao;
	}

	public void setWxPayDao(WXPayDao wxPayDao) {
		this.wxPayDao = wxPayDao;
	}

	@Override
	public void savePayNotice(PayNotice notice) {
		wxPayDao.savePayNotice(notice);
	}

	@Override
	public void savePayRecord(WXPayRecord record) {
		record.setId(IDUtil.getUUID());
		wxPayDao.savePayRecord(record);
	}

}
