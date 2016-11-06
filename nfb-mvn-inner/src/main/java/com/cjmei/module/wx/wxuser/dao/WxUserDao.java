package com.cjmei.module.wx.wxuser.dao;


import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import com.cjmei.module.wx.wxuser.pojo.WxUser;

public interface WxUserDao {

	WxUser findById(String openid);

	void save(WxUser wxUser);

	void update(WxUser wxUser);

	void delete(String openid);

	List<WxUser> findAll();

	void deleteBatch(List<String> list);

	List<WxUser> list(PageBounds pageBounds, Map<String,Object> param);
}