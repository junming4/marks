package com.marks.smart.wx.api.pay.wxInterface.cashpay.wxservice;

import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;

import com.marks.common.domain.JsonResult;
import com.marks.common.util.center.SysCode;
import com.marks.smart.wx.api.pay.wxInterface.cashpay.pojo.QueryRedPackReq;
import com.marks.smart.wx.api.pay.wxInterface.cashpay.pojo.QueryRedPackRes;
import com.marks.smart.wx.api.pay.wxInterface.cashpay.pojo.RedPackReq;
import com.marks.smart.wx.api.pay.wxInterface.cashpay.pojo.RedPackReqGroup;
import com.marks.smart.wx.api.pay.wxInterface.cashpay.pojo.RedPackRes;
import com.marks.smart.wx.api.pay.wxInterface.util.HttpService;
import com.marks.smart.wx.api.pay.wxInterface.util.WXPayUtil;
import com.marks.smart.wx.api.pay.wxInterface.util.WxPayPropUtil;

public class RedPackService {
	private static Logger logger = Logger.getLogger(RedPackService.class);
	private static RedPackService util=null;
	private RedPackService(){};
	
	public static RedPackService getInstance(){
		if(util == null){
			util = new RedPackService();
		}
		return util;
	}

	public JsonResult sendCommonRad(String accountid,RedPackReq vo) {
		JsonResult result=new JsonResult();  
		//签名
		WXPayUtil wxPayUtil = new WXPayUtil();
		try {
			vo.setTotal_num(1);
			String sign = wxPayUtil.createSign(vo.getSortedMap(),WxPayPropUtil.getValueByAccountId(accountid, "key"));
			String reqUrl=WxPayPropUtil.getProperty("payurl");
			SortedMap<String, String> reqMap=vo.getSortedMap();
			reqMap.put("sign", sign);
			String rsp = HttpService.doSendMoney(reqUrl, wxPayUtil.parseMapToXML(reqMap),accountid).replaceAll("\r|\n", "");
			Map<String, String> resMap=wxPayUtil.parseXMLToMap(rsp);
			RedPackRes res=(RedPackRes) wxPayUtil.converMap(RedPackRes.class, resMap);
			if(!"SUCCESS".equals(res.getReturn_code())){
				result.setErrorCode(res.getReturn_code());
				result.setErrorMsg(res.getReturn_msg());
				return result;	
			}
			if(!"SUCCESS".equals(res.getResult_code())){
				result.setErrorCode(res.getErr_code());
				result.setErrorMsg(res.getErr_code_des());
				return result;	
			}
			result.setResult(res);
		} catch (Exception e) {
			result.setErrorCode(SysCode.ERROR);
			result.setErrorMsg("系统错误");
		}  
		
		return result;
	}

	public JsonResult sendgroupredpack(String accountid, RedPackReqGroup vo) {
		JsonResult result=new JsonResult();  
		//签名
		WXPayUtil wxPayUtil = new WXPayUtil();
		try {
			String sign = wxPayUtil.createSign(vo.getSortedMap(),WxPayPropUtil.getValueByAccountId(accountid, "key"));
			String reqUrl=WxPayPropUtil.getProperty("payGroupUrl");
			SortedMap<String, String> reqMap=vo.getSortedMap();
			reqMap.put("sign", sign);
			String rsp = HttpService.doSendMoney(reqUrl, wxPayUtil.parseMapToXML(reqMap),accountid).replaceAll("\r|\n", "");
			Map<String, String> resMap=wxPayUtil.parseXMLToMap(rsp);
			RedPackRes res=(RedPackRes) wxPayUtil.converMap(RedPackRes.class, resMap);
			if(!"SUCCESS".equals(res.getReturn_code())){
				result.setErrorCode(res.getReturn_code());
				result.setErrorMsg(res.getReturn_msg());
				return result;	
			}
			if(!"SUCCESS".equals(res.getResult_code())){
				result.setErrorCode(res.getErr_code());
				result.setErrorMsg(res.getErr_code_des());
				return result;	
			}
			result.setResult(res);
		} catch (Exception e) {
			result.setErrorCode(SysCode.ERROR);
			result.setErrorMsg("系统错误");
		}  
		
		return result;
	}

	public JsonResult gethbinfo(String accountid, QueryRedPackReq vo) {
		JsonResult result=new JsonResult();  
		//签名
		WXPayUtil wxPayUtil = new WXPayUtil();
		try {
			String sign = wxPayUtil.createSign(vo.getSortedMap(),WxPayPropUtil.getValueByAccountId(accountid, "key"));
			String reqUrl=WxPayPropUtil.getProperty("gethbinfo");
			SortedMap<String, String> reqMap=vo.getSortedMap();
			reqMap.put("sign", sign);
			String rsp = HttpService.doSendMoney(reqUrl, wxPayUtil.parseMapToXML(reqMap),accountid).replaceAll("\r|\n", "");
			Map<String, String> resMap=wxPayUtil.parseXMLToMap(rsp);
			QueryRedPackRes res=(QueryRedPackRes) wxPayUtil.converMap(QueryRedPackRes.class, resMap);
			if(!"SUCCESS".equals(res.getReturn_code())){
				result.setErrorCode(res.getReturn_code());
				result.setErrorMsg(res.getReturn_msg());
				return result;	
			}
			if(!"SUCCESS".equals(res.getResult_code())){
				result.setErrorCode(res.getErr_code());
				result.setErrorMsg(res.getErr_code_des());
				return result;	
			}
			result.setResult(res);
		} catch (Exception e) {
			result.setErrorCode(SysCode.ERROR);
			result.setErrorMsg("系统错误");
		}  
		
		return result;
	}
}
