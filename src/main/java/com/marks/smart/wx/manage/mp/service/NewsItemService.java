package com.marks.smart.wx.manage.mp.service;


import java.util.List;
import java.util.Map;

import com.marks.common.domain.PojoDomain;
import com.marks.smart.wx.manage.mp.entity.NewsItem;

public interface NewsItemService{

	public NewsItem findById(String id);
	public void save(NewsItem newsItem);
	public void update(NewsItem newsItem);
	public void delete(String id);
	public List<NewsItem> findAll();
	public void deleteBatch(List<String> ids);
	public PojoDomain<NewsItem> list(int page_number, int page_size,Map<String,Object> param);
	public List<NewsItem> getnewItems(Map<String, Object> param);
	public int countNews(String id);
}