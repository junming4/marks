package com.marks.module.work.base.dao;


import java.util.List;
import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;

import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.marks.module.work.base.pojo.WorkType;

@MapperScan
public interface WorkTypeDao {

	WorkType findById(@Param("typeId") String typeId);

	void save(@Param("info") WorkType workType);
	
	void saveBatch(@Param("list") List<WorkType> list);

	void update(@Param("info") WorkType workType);
	
	void updateBatch(@Param("list") List<WorkType> list);

	void delete(@Param("typeId") String typeId);

	List<WorkType> findAll();

	void deleteBatch(@Param("list") List<String> list);

	List<WorkType> list(PageBounds pageBounds, Map<String,Object> param);
}