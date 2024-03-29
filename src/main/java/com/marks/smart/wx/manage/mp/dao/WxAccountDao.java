package com.marks.smart.wx.manage.mp.dao;


import java.util.List;
import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.marks.smart.wx.manage.mp.entity.WxAccount;
@MapperScan
public interface WxAccountDao {

	WxAccount findById(String accountId);

	void save(WxAccount wxAccount);

	void update(WxAccount wxAccount);

	void delete(String accountId);

	List<WxAccount> findAll();

	void deleteBatch(List<String> list);

	List<WxAccount> list(PageBounds pageBounds, Map<String,Object> param);

	List<WxAccount> getWxfwhaoList(Map<String, Object> param);

	List<String> getAccountIdsByLoginUser(Map<String, Object> param);

	String getAccountId(String companyId);
}