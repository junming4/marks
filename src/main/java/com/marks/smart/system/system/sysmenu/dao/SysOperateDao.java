package com.marks.smart.system.system.sysmenu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.marks.smart.system.system.sysmenu.pojo.SysOperate;
@MapperScan
public interface SysOperateDao {

	List<SysOperate> list(PageBounds pageBounds,@Param("keyword")String keyword);

	void save(@Param("info")SysOperate info);

	SysOperate getObjectById(@Param("operid")String operid);

	void update(@Param("info")SysOperate info);

	int countfunc(@Param("operid")String id);

	void delete(@Param("operid")String id);

}
