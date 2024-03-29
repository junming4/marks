package com.marks.smart.market.mall.dispatch.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.marks.smart.market.mall.dispatch.pojo.DispatchGood;

@MapperScan
public interface DispatchGoodDao {

	DispatchGood findById(@Param("orderGoodId") String orderGoodId);

	void save(@Param("info") DispatchGood dispatchGood);
	
	void saveBatch(@Param("list") List<DispatchGood> list);

	void update(@Param("info") DispatchGood dispatchGood);
	
//	void updateBatch(@Param("list") List<DispatchGood> list);

	void delete(@Param("orderGoodId") String orderGoodId);

	List<DispatchGood> findAll();

	void deleteBatch(@Param("list") List<String> list);

	List<DispatchGood> list(PageBounds pageBounds, Map<String,Object> param);

	List<DispatchGood> findByOrderId(@Param("orderId") String orderId,@Param("orgId") String orgId);

	void deleteByOrderId(@Param("orderId") String orderId);

	void updateRecevieNums(@Param("orderGoodId") String orderGoodId, @Param("receiveNums") int receiveNums);

	void updateDispatchNums(@Param("orderGoodId")String orderGoodId,@Param("dispatchNums") int dispatchNums);
}