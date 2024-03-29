package com.marks.smart.count.asset.base.service;


import java.util.List;
import java.util.Map;

import com.marks.common.domain.PojoDomain;
import com.marks.smart.count.asset.base.pojo.AssetLog;
import com.marks.smart.count.asset.base.pojo.AssetLogCount;

public interface AssetLogService{

	public AssetLog findById(String id);
	public void save(AssetLog info);
	public void update(AssetLog info);
	public void delete(String id);
	public List<AssetLog> findAll();
	public void deleteBatch(List<String> ids);
	public PojoDomain<AssetLog> list(int page_number, int page_size,Map<String,Object> param);

	/**
	 * 统计消费
	 * 
	 * @param page_number
	 * @param page_size
	 * @param param
	 * @return
	 */
	public PojoDomain<AssetLogCount> listCount(int page_number, int page_size, Map<String, Object> param);

	/**
	 * 查询支出总额
	 * 
	 * @param param
	 * @return
	 */
	public List<AssetLogCount> countAmount(Map<String, Object> param);

	/**
	 * 查询每日统计
	 * 
	 * @param page_number
	 * @param page_size
	 * @param param
	 * @return
	 */
	public PojoDomain<AssetLogCount> listDayCount(int page_number, int page_size, Map<String, Object> param);
}