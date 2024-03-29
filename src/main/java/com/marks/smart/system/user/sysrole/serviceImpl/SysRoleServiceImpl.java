package com.marks.smart.system.user.sysrole.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.marks.common.domain.PojoDomain;
import com.marks.common.util.Constants;
import com.marks.smart.system.org.orginfo.dao.OrgInfoDao;
import com.marks.smart.system.org.orginfo.pojo.OrgInfo;
import com.marks.smart.system.system.sysmenu.pojo.SysFunc;
import com.marks.smart.system.system.sysmenu.pojo.SysMenu;
import com.marks.smart.system.user.login.dao.LoginDao;
import com.marks.smart.system.user.sysrole.dao.SysRoleDao;
import com.marks.smart.system.user.sysrole.pojo.SysRole;
import com.marks.smart.system.user.sysrole.pojo.SysRoleFunc;
import com.marks.smart.system.user.sysrole.service.SysRoleService;
import com.marks.smart.system.user.sysuser.pojo.SysUser;
@Service
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private LoginDao loginDao;

	@Autowired
	private OrgInfoDao orgInfoDao;

	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}
	@Autowired
	private SysRoleDao sysRoleDao;

	public SysRoleDao getSysRoleDao() {
		return sysRoleDao;
	}

	public void setSysRoleDao(SysRoleDao sysRoleDao) {
		this.sysRoleDao = sysRoleDao;
	}

	/**
	 * 根据ID查找角色管理
	 */
	@Override
	public SysRole findById(String roleid) {
		return sysRoleDao.findById(roleid);
	}

	/**
	 * 保存角色管理
	 */
	@Override
	public void save(SysRole sysRole) {
		OrgInfo info = orgInfoDao.findById(sysRole.getOrgId());
		if (info != null) {
			sysRole.setOrgName(info.getOrgname());
		}
		sysRoleDao.save(sysRole);
		if (1 == sysRole.getRoleYwType()) {
			String roleId = sysRole.getCompanyId() + "_" + sysRole.getRoleType();
			List<String> funcList = sysRoleDao.getFuncListByRoleId(roleId);
			saveSysFuncByRoleId(sysRole.getRoleid(), funcList);
		}
	}

	/**
	 * 更新角色管理
	 */
	@Override
	public void update(SysRole sysRole) {
		sysRoleDao.update(sysRole);
		sysRoleDao.updateSysUserByRole(sysRole);
		sysRoleDao.updateSysUserRoleByRole(sysRole);
	}

	/**
	 * 删除角色管理
	 */
	@Override
	public void delete(String roleid) {
		sysRoleDao.deleteFuncByRoleid(roleid);
		sysRoleDao.delete(roleid);
	}

	/**
	 * 查找所有角色管理
	 */
	@Override
	public List<SysRole> findAll() {
		return sysRoleDao.findAll();
	}

	/**
	 * 删除多个角色管理
	 */
	@Override
	public void deleteBatch(List<String> ids) {
		sysRoleDao.deleteBatch(ids);
	}

	public PojoDomain<SysRole> list(int page_number, int page_size, Map<String, Object> param) {
		PojoDomain<SysRole> pojoDomain = new PojoDomain<SysRole>();
		PageBounds pageBounds = new PageBounds(page_number, page_size);
		List<SysRole> list = sysRoleDao.list(pageBounds, param);
		PageList<SysRole> pageList = (PageList<SysRole>) list;
		pojoDomain.setPojolist(list);
		pojoDomain.setPage_number(page_number);
		pojoDomain.setPage_size(page_size);
		pojoDomain.setTotal_count(pageList.getPaginator().getTotalCount());
		return pojoDomain;
	}
	
	

	@Override
	public List<SysRole> getUserlist(Map<String, Object> param) {
		List<SysRole> list = sysRoleDao.getUserlist(param);
		return list;
	}

	@Override
	public void addSysFuncByRoleId(String role_id, List<String> funcIds) {
		SysRoleFunc srf = null;
		if (null != funcIds && funcIds.size() > 0) {
			List<SysRoleFunc> list = new ArrayList<SysRoleFunc>();
			for (String funcid : funcIds) {
				srf = new SysRoleFunc();
				srf.setCreatetime(new Date());
				srf.setFuncid(funcid);
				srf.setRoleid(role_id);
				srf.setUpdatetime(new Date());
				list.add(srf);
				// sysRoleDao.saveRoleFunc(srf);
			}
			sysRoleDao.saveBatchRoleFunc(list);
		}

	}
	
	

	@Override
	public void saveSysFuncByRoleId(String roleId, List<String> funcList) {
		sysRoleDao.deleteFuncByRoleid(roleId);
		addSysFuncByRoleId(roleId,funcList);
	}

	@Override
	public List<SysMenu> funcList(SysUser admin,String roleId) {
		String loginUserRoleId = admin.getRoleId();
		boolean getflag = false;// 请求数据标识
		List<SysMenu> returnMenu = new ArrayList<SysMenu>();

		// 未指定用户角色不可查看任何菜单
		if (null != loginUserRoleId && !loginUserRoleId.equals("")) {
			getflag = true;
		}
		// 开发者角色可以配置所有的功能
		if (Constants.default_roleId.equals(loginUserRoleId)) {
			getflag = true;
			loginUserRoleId=null;
		}
		
		if (getflag) {
			List<SysFunc> funcList=sysRoleDao.getFuncList(loginUserRoleId,roleId);
			List<SysMenu> child = loginDao.getChildMenu(loginUserRoleId);
			
		
			if (null != child && child.size() > 0) {
				//子菜单添加功能
				for (SysMenu cm : child) {
					for(SysFunc sf:funcList){
						if(cm.getMenuid().equals(sf.getMenuid())){
							cm.addOper(sf);
						}
					}
				}
				//封装成树
				List<SysMenu> parentMenu = loginDao.getParentSysMenu();
				for (SysMenu pm : parentMenu) {
					for (SysMenu cm : child) {
						if (pm.getMenuid().equals(cm.getParentid())) {
							pm.addChildren(cm);
						}
					}
					if (pm.getChildren().size() > 0) {
						returnMenu.add(pm);
					}
				}
			}
		}
		return returnMenu;
	}
	@Override
	public boolean isDelete(String roleid) {
		int num=sysRoleDao.countUserByRoleid(roleid);
		if(num>0) return false;
		return true;
	}

	@Override
	public List<SysRole> listByOrgId(String orgid) {
		return sysRoleDao.listByOrgId(orgid);
	}
	
	
	
	

}