package com.cjmei.module.wx.wxuser.service;


import com.cjmei.module.wx.wxuser.pojo.WxUser;

import java.util.List;
import java.util.Map;

import com.cjmei.common.domain.PojoDomain;

public interface WxUserService{

	public WxUser findById(String openid);
	public void save(WxUser wxUser);
	public void update(WxUser wxUser);
	public void delete(String openid);
	public List<WxUser> findAll();
	public void deleteBatch(List<String> ids);
	public PojoDomain<WxUser> list(int page_number, int page_size,Map<String,Object> param);
}