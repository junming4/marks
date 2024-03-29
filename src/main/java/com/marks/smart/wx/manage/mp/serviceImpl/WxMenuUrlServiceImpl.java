package com.marks.smart.wx.manage.mp.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.marks.common.domain.PojoDomain;
import com.marks.smart.wx.manage.mp.dao.WxMenuUrlDao;
import com.marks.smart.wx.manage.mp.entity.WxMenuUrl;
import com.marks.smart.wx.manage.mp.service.WxMenuUrlService;
@Service
public class WxMenuUrlServiceImpl implements WxMenuUrlService{
   
	@Autowired
    private WxMenuUrlDao wxMenuUrlDao;

    public WxMenuUrlDao getWxMenuUrlDao(){
        return wxMenuUrlDao;
    }
    public void setWxMenuUrlDao(WxMenuUrlDao wxMenuUrlDao){
        this.wxMenuUrlDao =wxMenuUrlDao;
    }

    
    /**
    *根据ID查找微信菜单URL
    */
    @Override
    public WxMenuUrl findById(String id){
        return wxMenuUrlDao.findById(id);
    }
    
    /**
    *保存微信菜单URL
    */
    @Override
    public void save(WxMenuUrl wxMenuUrl){
        wxMenuUrlDao.save(wxMenuUrl);
    }
    
    /**
    *更新微信菜单URL
    */
    @Override
    public void update(WxMenuUrl wxMenuUrl){
        wxMenuUrlDao.update(wxMenuUrl);
    }
    
    /**
    *删除微信菜单URL
    */
    @Override
    public void delete(String id){
        wxMenuUrlDao.delete(id);       
    }
    
    /**
    *查找所有微信菜单URL
    */
    @Override
    public List<WxMenuUrl> findAll(){
        return wxMenuUrlDao.findAll();   
    }
    
    /**
    *删除多个微信菜单URL
    */
    @Override
   public void deleteBatch(List<String> ids) {
		wxMenuUrlDao.deleteBatch(ids);
	}
	
	public PojoDomain<WxMenuUrl> list(int page_number, int page_size, Map<String,Object> param) {
		PojoDomain<WxMenuUrl> pojoDomain = new PojoDomain<WxMenuUrl>();
		PageBounds pageBounds = new PageBounds(page_number, page_size);
		List<WxMenuUrl> list = wxMenuUrlDao.list(pageBounds,param);
		PageList<WxMenuUrl> pageList = (PageList<WxMenuUrl>)list; 
		pojoDomain.setPojolist(list);
		pojoDomain.setPage_number(page_number);
		pojoDomain.setPage_size(page_size);
		pojoDomain.setTotal_count(pageList.getPaginator().getTotalCount());
		return pojoDomain;
	}
	
}