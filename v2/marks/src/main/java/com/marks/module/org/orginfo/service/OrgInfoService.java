package com.marks.module.org.orginfo.service;


import java.util.List;
import java.util.Map;

import com.marks.common.domain.PojoDomain;
import com.marks.module.org.orginfo.pojo.OrgInfo;
import com.marks.module.user.sysuser.pojo.SysUser;

public interface OrgInfoService{

	public OrgInfo findById(String orgid);
	public void save(OrgInfo orgInfo);
	public void update(OrgInfo orgInfo);
	public void delete(String orgid);
	public List<OrgInfo> findAll();
	public void deleteBatch(List<String> ids);
	public List<OrgInfo> list(SysUser admin);
	public PojoDomain<OrgInfo> framelist(int page_number, int page_size, Map<String, Object> param);
	public List<OrgInfo> getChildList(String orgid);

	public List<OrgInfo> listGrid(String parentid, String companyId, String orgType);
	public List<OrgInfo> frameCombo(Map<String, Object> param);
	/**
	 * 获取机构ID
	 * @return
	 */
	public String getOrgId();

}