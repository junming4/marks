package com.marks.module.system.sys.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marks.common.domain.Result;
import com.marks.common.enums.Enums;
import com.marks.common.util.Constants;
import com.marks.common.util.JsonUtil;
import com.marks.common.util.encrypt.EncryptUtil;
import com.marks.module.system.core.data.StaticData;
import com.marks.module.system.core.helper.SysUserHelper;
import com.marks.module.system.orginfo.pojo.OrgInfo;
import com.marks.module.system.sys.pojo.SysMenu;
import com.marks.module.system.sys.service.LoginService;
import com.marks.module.system.sysrole.pojo.SysRole;
import com.marks.module.system.sysrole.service.SysRoleService;
import com.marks.module.system.sysuser.pojo.SysUser;
import com.marks.module.wx.wxaccount.service.WxAccountService;

/**
 * 用户登录 控制层 File Name: com.grgbanking.inner.controller.LoginController.java
 * 
 * @author:marks0221@163.com
 * @Date:2016年7月27日下午1:59:34
 * @see (optional)
 * @Copyright (c) 2016, marks All Rights Reserved.
 */
@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 登录 queryDepartmentList:描述 <br/>
	 *
	 * @param request
	 * @param response
	 * @author marks
	 * @throws Exception
	 * @修改记录:(日期,修改人,描述) (可选) <br/>
	 */
	@RequestMapping("/login")
	public void queryDepartmentList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Result result = new Result();
		String userid = request.getParameter("userid");
		String pwd = request.getParameter("pwd");
		/**
		 * 如果登陆用为system，则拥有所有权限除了业务权限
		 */
		SysUser user = loginService.getSysUserByUserid(userid);
		if (user == null) {
			result.setCode(4001);
			result.setMessage("用户不存在");
		} else {
			if (Enums.SysUserUse.NOUSE.getValue() == user.getActiveFlag()) {
				result.setCode(4002);
				result.setMessage("用户被禁用");
			} else {
				String password = EncryptUtil.encrypt(pwd);
				if (password.equals(user.getPassword())) {
					SysRole role = sysRoleService.findById(user.getRoleid());
					if (role != null) {
						result.setCode(0);
						result.setMessage("success");
						user.setLoginTime(new Date());
						user.setPassword("");
						user.setRole(role);
						List<OrgInfo> orgInfo = loginService.getOrgInfoListByUserid(user.getUserid());
						user.setOrgInfoList(orgInfo);
						// 组织架构
						boolean topflag = true;
						for (OrgInfo sr : orgInfo) {
							if (1 == sr.getIsMain()){
								topflag = false;
								break;
							}
						}
						if (topflag) {
							List<String> orgids = loginService.getOrgidBySysUser(orgInfo);
							user.setOrgids(orgids);
						} else {
							user.setOrgids(null);
							user.setCompanyId(null);
						}
						user.setAccountids(null);
						if (null != user.getOrgids() && null != user.getCompanyId()) {
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("conpanyId", user.getCompanyId());
//							param.put("orgids", user.getOrgids());
							param.put("orgids",null);
							List<String> accountids = wxAccountService.getAccountIdsByLoginUser(param);
							user.setAccountids(accountids);
						}
						SysUserHelper.setCurrentUserInfo(request, user);
					} else {
						result.setCode(4005);
						result.setMessage("您没有权限登录");
					}

				} else {
					result.setCode(4003);
					result.setMessage("密码错误");
				}
			}
		}
		JsonUtil.output(response, result);
	}

	@RequestMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Result result = new Result();
		SysUser user = SysUserHelper.getCurrentUserInfo(request);
		if (user != null) {
			SysUserHelper.setCurrentUserInfo(request, null);
		}
		result.setCode(0);
		result.setMessage("success");
		JsonUtil.output(response, result);
	}

	@RequestMapping("/menu")
	public void sysMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Result result = new Result();
		SysUser user = SysUserHelper.getCurrentUserInfo(request);
		List<SysMenu> list = loginService.getSysMenuOfSysUser(user);
		result.setCode(0);
		result.setMessage("success");
		result.getData().put("menuList", list);
		result.getData().put("loginUser", user);
		JsonUtil.output(response, result);
	}
}