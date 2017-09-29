package com.marks.module.user.sysrole.service;


import java.util.List;
import java.util.Map;

import com.marks.common.domain.PojoDomain;
import com.marks.module.system.sysmenu.pojo.SysMenu;
import com.marks.module.user.sysrole.pojo.SysRole;
import com.marks.module.user.sysuser.pojo.SysUser;

public interface SysRoleService{

	public SysRole findById(String roleid);
	public void save(SysRole sysRole);
	public void update(SysRole sysRole);
	public void delete(String roleid);
	public List<SysRole> findAll();
	public void deleteBatch(List<String> ids);
	public PojoDomain<SysRole> list(int page_number, int page_size,Map<String,Object> param);
	public void addSysFuncByRoleId(String role_id, List<String> funcIds);
	public List<SysMenu> funcList(SysUser admin,String roleId);
	public void saveSysFuncByRoleId(SysUser admin, String roleId, List<String> funcList);
	public SysRole findByUserTypeAndCompanyId(String roleid, String companyId);
	public boolean isDelete(String roleid);
	public List<SysRole> getUserlist(Map<String, Object> param);
}