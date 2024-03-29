package com.marks.smart.system.org.orginfo.service;


import java.util.List;
import java.util.Map;

import com.marks.common.domain.PojoDomain;
import com.marks.smart.system.org.orginfo.pojo.OrgInfo;

public interface OrgInfoService{

	public OrgInfo findById(String orgid);
	public String save(OrgInfo orgInfo);
	public void update(OrgInfo orgInfo);
	public void delete(String orgid);
	public void deleteBatch(List<String> ids);

	public List<OrgInfo> list(String companyId, String parentId, String orgType);
	public PojoDomain<OrgInfo> framelist(int page_number, int page_size, Map<String, Object> param);
	public List<OrgInfo> getChildList(String orgid);

	public List<OrgInfo> listGrid(Map<String, Object> param);
	public List<OrgInfo> frameCombo(Map<String, Object> param);
	public void updateCheckStatus(Map<String, String> map);

}