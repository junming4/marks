package com.marks.smart.wx.manage.mp.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.marks.smart.wx.manage.mp.entity.NewsItem;

@MapperScan
public interface NewsItemDao {

	NewsItem findById(String id);

	void save(NewsItem newsItem);

	void update(NewsItem newsItem);

	void delete(String id);

	List<NewsItem> findAll();

	void deleteBatch(List<String> list);

	List<NewsItem> list(PageBounds pageBounds, Map<String,Object> param);

	List<NewsItem> getnewItems(Map<String, Object> param);

	int countNews(@Param("newsId")String newsId);

	List<NewsItem> getNewsItemByIds(@Param("newsItemIds")String[] newsItemIds);
}