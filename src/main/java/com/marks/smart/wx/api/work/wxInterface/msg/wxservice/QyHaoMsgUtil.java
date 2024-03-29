package com.marks.smart.wx.api.work.wxInterface.msg.wxservice;

import org.json.JSONException;
import org.json.JSONObject;

import com.marks.common.domain.JsonResult;
import com.marks.common.util.center.SysCode;
import com.marks.smart.wx.api.work.wxInterface.base.QyAccessTokenUtil;
import com.marks.smart.wx.api.work.wxInterface.base.utils.WxQyHttpUtils;
import com.marks.smart.wx.api.work.wxInterface.base.utils.WxqyConfig;
import com.marks.smart.wx.api.work.wxInterface.msg.entity.QyHaoMsg;

public class QyHaoMsgUtil {
	// 通知企业号用户
	public static JsonResult sendQyMsg(QyHaoMsg qyHaoMsg, String qyAccount) throws Exception {
		JSONObject objx = new JSONObject();
		JsonResult result = new JsonResult();
		try {
			objx.accumulate("agentid", qyHaoMsg.getAgentid());
			objx.accumulate("touser", qyHaoMsg.getTouser());
			objx.accumulate("msgtype", qyHaoMsg.getMsgtype());
			objx.accumulate("toparty", qyHaoMsg.getToparty());
			objx.accumulate("totag", qyHaoMsg.getTotag());
			objx.accumulate("safe", qyHaoMsg.getSafe());
			objx.accumulate("text", new JSONObject().accumulate("content", qyHaoMsg.getContent()));
		} catch (JSONException e) {

		}

		String url = WxqyConfig.qyhao_sendmsg;
		url = url + QyAccessTokenUtil.getInstance().getQyAccessToken(qyAccount);
		result = WxQyHttpUtils.doPostJson(qyAccount, url, objx, null);
		if (SysCode.C9980.equals(result.getErrorCode())) {
			sendQyMsg(qyHaoMsg, qyAccount);
		}
		return result;
	}
}
