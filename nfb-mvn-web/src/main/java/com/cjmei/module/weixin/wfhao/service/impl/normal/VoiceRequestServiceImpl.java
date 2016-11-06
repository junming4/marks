package com.cjmei.module.weixin.wfhao.service.impl.normal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cjmei.module.system.core.data.StaticData;
import com.cjmei.module.weixin.wfhao.message.request.RequestMessage;
import com.cjmei.module.weixin.wfhao.message.request.impl.VoiceRequestMessage;
import com.cjmei.module.weixin.wfhao.message.response.ResponseMessage;
import com.cjmei.module.weixin.wfhao.message.response.impl.TextResponseMessage;

/**
 * 语音消息对象服务
 * 
 * @author
 * @createTime
 * @history 1.修改时间,修改;修改内容：
 * 
 */
public class VoiceRequestServiceImpl extends AbstractRequestService {

	private static Logger logger = Logger.getLogger(VoiceRequestServiceImpl.class);

	/**
	 * 请求消息处理
	 * 
	 * @param requestMessage
	 *            请求消息对象
	 * @return 响应消息对象
	 * @throws Exception
	 */
	@Override
	public ResponseMessage handle(HttpServletRequest request, RequestMessage requestMessage) throws Exception {
		VoiceRequestMessage voiceRequestMessage = (VoiceRequestMessage) requestMessage;
		ResponseMessage responseMessage;
		if (StringUtils.isEmpty(voiceRequestMessage.getRecognition())) {
			TextResponseMessage textResponseMessage = new TextResponseMessage(requestMessage);
			textResponseMessage.setContent("");
			responseMessage = textResponseMessage;
		} else {
			responseMessage = handle(requestMessage, voiceRequestMessage.getRecognition());

			logger.info("-----------------------语音解析结果：" + voiceRequestMessage.getRecognition());

			if (responseMessage != null) {
				if (responseMessage instanceof TextResponseMessage) {
					TextResponseMessage textResponseMessage = (TextResponseMessage) responseMessage;
					String voicemsg = "您说的是：";

					textResponseMessage.setContent(voicemsg + voiceRequestMessage.getRecognition());
				}
			}
		}
		return responseMessage;
	}

}