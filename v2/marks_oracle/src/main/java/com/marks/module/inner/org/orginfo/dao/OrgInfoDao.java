package com.marks.module.inner.org.orginfo.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.marks.module.inner.org.orginfo.pojo.OrgInfo;

@MapperScan
public interface OrgInfoDao {

	OrgInfo findById(String orgid);

	void save(OrgInfo orgInfo);

	void update(OrgInfo orgInfo);

	void delete(String orgid);

	List<OrgInfo> findAll(@Param("companyId")String companyId);

	void deleteBatch(List<String> list);

	List<OrgInfo> list(PageBounds pageBounds, Map<String,Object> param);

	List<OrgInfo> getTreeGridByParentId(@Param("plist")List<String>  plist,@Param("companyId")String companyId);

	List<OrgInfo> getChildList(@Param("orgid")String orgid);

	List<OrgInfo> frameCombo(Map<String, Object> param);

	void updateOrgChildNum(@Param("orgid") String orgid);

	String getOrgId();

}