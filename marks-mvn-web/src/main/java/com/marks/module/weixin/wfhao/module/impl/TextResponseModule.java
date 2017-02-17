package com.marks.module.weixin.wfhao.module.impl;

import com.marks.module.weixin.wfhao.message.request.WechatRequest;
import com.marks.module.weixin.wfhao.message.response.WechatResponse;
import com.marks.module.weixin.wfhao.module.Module;

/**
 * 回复文本消息
 * @author lhyan3
 * 2015年7月15日下午2:47:11
 */
public class TextResponseModule extends Module{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1000L;

	@Override
	public WechatResponse syncRequest(WechatRequest requestMessage) {
		WechatResponse textResponseMessage = new WechatResponse(requestMessage);
		textResponseMessage.setContent(requestMessage.getContent());
		return textResponseMessage;
	}

	@Override
	public String getOperate() {
		return "回复文本消息";
	}

}
