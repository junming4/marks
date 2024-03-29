package com.marks.smart.wx.manage.mp.service;


import java.util.List;
import java.util.Map;

import com.marks.common.domain.PojoDomain;
import com.marks.smart.wx.manage.mp.entity.WxMenuUrl;

public interface WxMenuUrlService{

	public WxMenuUrl findById(String id);
	public void save(WxMenuUrl wxMenuUrl);
	public void update(WxMenuUrl wxMenuUrl);
	public void delete(String id);
	public List<WxMenuUrl> findAll();
	public void deleteBatch(List<String> ids);
	public PojoDomain<WxMenuUrl> list(int page_number, int page_size,Map<String,Object> param);
}