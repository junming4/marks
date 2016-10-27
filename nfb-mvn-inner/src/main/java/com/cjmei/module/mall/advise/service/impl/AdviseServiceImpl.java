package com.cjmei.module.mall.advise.service.impl;

import java.util.List;
import java.util.Map;

import com.cjmei.common.domain.PojoDomain;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import com.cjmei.module.mall.advise.pojo.Advise;
import com.cjmei.module.mall.advise.dao.AdviseDao;
import com.cjmei.module.mall.advise.service.AdviseService;

public class AdviseServiceImpl implements AdviseService{
   

    private AdviseDao adviseDao;

    public AdviseDao getAdviseDao(){
        return adviseDao;
    }
    public void setAdviseDao(AdviseDao adviseDao){
        this.adviseDao =adviseDao;
    }

    
    /**
    *根据ID查找客户专制
    */
    @Override
    public Advise findById(String ID){
        return adviseDao.findById(ID);
    }
    
    /**
    *保存客户专制
    */
    @Override
    public void save(Advise advise){
        adviseDao.save(advise);
    }
    
    /**
    *更新客户专制
    */
    @Override
    public void update(Advise advise){
        adviseDao.update(advise);
    }
    
    /**
    *删除客户专制
    */
    @Override
    public void delete(String ID){
        adviseDao.delete(ID);       
    }
    
    /**
    *查找所有客户专制
    */
    @Override
    public List<Advise> findAll(){
        return adviseDao.findAll();   
    }
    
    /**
    *删除多个客户专制
    */
    @Override
   public void deleteBatch(List<String> ids) {
		adviseDao.deleteBatch(ids);
	}
	
	public PojoDomain<Advise> list(int page_number, int page_size, Map<String,Object> param) {
		PojoDomain<Advise> pojoDomain = new PojoDomain<Advise>();
		PageBounds pageBounds = new PageBounds(page_number, page_size);
		List<Advise> list = adviseDao.list(pageBounds,param);
		PageList<Advise> pageList = (PageList<Advise>)list; 
		pojoDomain.setPojolist(list);
		pojoDomain.setPage_number(page_number);
		pojoDomain.setPage_size(page_size);
		pojoDomain.setTotal_count(pageList.getPaginator().getTotalCount());
		return pojoDomain;
	}
	
}