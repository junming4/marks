package com.cjmei.module.wx.modulemsg.util;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cjmei.module.system.core.listener.DatabaseHelper;
import com.cjmei.module.wx.modulemsg.service.ModuleMsgService;
import com.cjmei.module.wx.wxuser.dao.WxUserDao;
import com.cjmei.module.wx.wxuser.pojo.WxUser;

public class WxMsgUtil {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:config/spring/applicationContext.xml");
		DatabaseHelper.init(context);
		WxUserDao wxUserDao=(WxUserDao) DatabaseHelper.getBean(WxUserDao.class);
		List<WxUser> list=wxUserDao.findWxUserListForDairy();
		if(null != list && list.size()>0){
			ModuleMsgService moduleMsgService=(ModuleMsgService) DatabaseHelper.getBean(ModuleMsgService.class);
			moduleMsgService.pushDairyWxMsg(list.get(0));
		}
	}
}