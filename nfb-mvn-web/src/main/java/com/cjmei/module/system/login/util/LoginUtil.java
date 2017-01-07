package com.cjmei.module.system.login.util;

import javax.servlet.http.HttpServletRequest;

import com.cjmei.module.runModel.RunModel;
import com.cjmei.module.system.login.pojo.SysUser;

public class LoginUtil {
	public static LoginUtil util=null;
	private LoginUtil(){}
	public static LoginUtil getInstance(){
		if(util==null){
			util=new LoginUtil();
		}
		return util;
	}
	public String getCompanyId() {
		return "Marks";
	}
	public void setCurrentUser(HttpServletRequest request,SysUser user) {
		request.getSession().setAttribute("cSysUser", user);
	}
	public SysUser getCurrentUser(HttpServletRequest request){
		Object obj = request.getSession().getAttribute("cSysUser");
		SysUser user = null;
		if (null != obj) {
			user = (SysUser) obj;
		}
		if (RunModel.getInstance().getMode().equals("N")) {
			user=new SysUser();
			user.setUserid("admin");
		}
		return user;
	}
}
