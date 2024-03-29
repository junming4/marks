package com.marks.smart.system.autocode.web.util;

import java.util.ArrayList;
import java.util.List;

import com.marks.common.util.Constants;
import com.marks.smart.system.autocode.core.produced.config.AutoConfig;
import com.marks.smart.system.autocode.core.produced.pojo.AutoBean;
import com.marks.smart.system.autocode.core.produced.webpage.html.htmlpage.HtmlPageProduced;
import com.marks.smart.system.core.common.SpringContextHolder;
import com.marks.smart.system.system.sysmenu.pojo.SysMenu;
import com.marks.smart.system.system.sysmenu.pojo.SysOperate;
import com.marks.smart.system.system.sysmenu.service.SysMenuService;
import com.marks.smart.system.user.sysrole.service.SysRoleService;

public class AuthUtil {
	private SysMenuService sysMenuService = (SysMenuService) SpringContextHolder.getBean(SysMenuService.class);
	private SysRoleService sysRoleService = (SysRoleService) SpringContextHolder.getBean(SysRoleService.class);
	private static AuthUtil util = null;

	private AuthUtil() {
	}

	public static AuthUtil getInstance() {
		if (util == null) {
			util = new AuthUtil();
		}
		return util;
	}

	// 授权
	public void addFuncForRole(AutoBean autoBean) {
		HtmlPageProduced html = new HtmlPageProduced();
		String autoBeanName = autoBean.getFactBeanName();
		String menuUrl = AutoConfig.config_menu_src + autoBean.getParentPackage().replace(".", "/") + "/" + autoBeanName
				+ "." + html.DEFAULT_FILE_HTML;
		String listurl = "/inner/" + autoBeanName + "/list";
		String saveurl = "/inner/" + autoBeanName + "/save";
		String updateurl = "/inner/" + autoBeanName + "/update";
		String deleteurl = "/inner/" + autoBeanName + "/delete";
		String[] arr = autoBean.getParentPackage().split("\\.");
		String lvl1Menuid = "M_" + arr[0];
		String lvl2Menuid = "M_" + arr[0] + "_" + arr[1];
		String menuId = lvl2Menuid + "_" + autoBeanName;
		lvl1Menuid=lvl1Menuid.toUpperCase();
		lvl2Menuid=lvl2Menuid.toUpperCase();
		menuId=menuId.toUpperCase();
		SysMenu smP = sysMenuService.getSysMenuByMenuid(lvl1Menuid);

		if (smP == null) {
			smP = new SysMenu();
			smP.setMenuid(lvl1Menuid);
			smP.setMenuitem(autoBean.getModuleDesc());
			smP.setParentid(Constants.top_parent_id);
			smP.setSort(12);
			smP.setLvl(1);
			smP.setUrl("#");
			sysMenuService.save(smP);
		}
		SysMenu smP2 = sysMenuService.getSysMenuByMenuid(lvl2Menuid);
		if (smP2 == null) {
			smP2 = new SysMenu();
			smP2.setMenuid(lvl2Menuid);
			smP2.setMenuitem(autoBean.getModuleDesc());
			smP2.setParentid(lvl1Menuid);
			smP2.setLvl1Menuid(lvl1Menuid);
			smP2.setLvl(2);
			smP2.setSort(12);
			smP2.setUrl("#");
			sysMenuService.save(smP2);
		}
		SysMenu sm = sysMenuService.getSysMenuByMenuid(menuId);
		if (sm == null) {
			sm = new SysMenu();
			sm.setMenuid(menuId);
			sm.setMenuitem(autoBean.getModuleDesc());
			sm.setParentid(lvl2Menuid);
			sm.setLvl1Menuid(lvl1Menuid);
			sm.setLvl2Menuid(lvl2Menuid);
			sm.setLvl(3);
			sm.setSort(100);
			sm.setUrl(menuUrl);

			sysMenuService.save(sm);
			SysOperate query = sysMenuService.saveFunc("query", sm.getMenuid(), listurl);
			SysOperate add = sysMenuService.saveFunc("add", sm.getMenuid(), saveurl);
			SysOperate update = sysMenuService.saveFunc("edit", sm.getMenuid(), updateurl);
			SysOperate delete = sysMenuService.saveFunc("delete", sm.getMenuid(), deleteurl);
			List<String> funcIds = new ArrayList<String>();
			funcIds.add(query.getFuncid());
			funcIds.add(add.getFuncid());
			funcIds.add(update.getFuncid());
			funcIds.add(delete.getFuncid());
			sysRoleService.addSysFuncByRoleId(AutoConfig.role_id, funcIds);
		}
	}
}
