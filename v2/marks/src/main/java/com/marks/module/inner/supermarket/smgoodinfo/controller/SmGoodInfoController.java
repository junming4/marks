package com.marks.module.inner.supermarket.smgoodinfo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marks.common.domain.PaginationResult;
import com.marks.common.domain.PojoDomain;
import com.marks.common.domain.Result;
import com.marks.common.util.JsonUtil;
import com.marks.common.util.IDUtil;
import com.marks.common.util.Code;
import com.marks.module.inner.system.sys.controller.SupportContorller;
import com.marks.module.inner.system.sysuser.pojo.SysUser;
import com.marks.module.sys.system.core.helper.SysUserHelper;

import com.marks.module.inner.supermarket.smgoodinfo.pojo.SmGoodInfo;
import com.marks.module.inner.supermarket.smgoodinfo.service.SmGoodInfoService;

 /**
	 * 超市商品: 超市商品
	 */
@Controller
public class SmGoodInfoController extends SupportContorller{
    private static Logger logger = Logger.getLogger( SmGoodInfoController.class);
    
    @Autowired
    private SmGoodInfoService  smGoodInfoService;
   

    @Override
	public Logger getLogger() {
		return logger;
	}

    /**
	 * 查询超市商品
	 */
    @RequestMapping("/inner/smGoodInfo/findSmGoodInfoById")
    public void findSmGoodInfoById(HttpServletRequest request,
    HttpServletResponse response){
        Result result = new Result();
		try {
		    SmGoodInfo smGoodInfo = getModel(SmGoodInfo.class);
		    
		    logger.info("findSmGoodInfoById > param>"+smGoodInfo.getGoodId());
		    
			SmGoodInfo requestSmGoodInfo = smGoodInfoService.findById(smGoodInfo.getGoodId());
			result.getData().put("smGoodInfo",requestSmGoodInfo);
			result.setMessage("findById smGoodInfo successs!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("查询失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
    }
    
    /**
	 * 保存超市商品
	 */
    @RequestMapping("/inner/smGoodInfo/save")
    public void saveSmGoodInfo(HttpServletRequest request,
    HttpServletResponse response){
		Result result = new Result();
		try {
			SysUser admin = SysUserHelper.getCurrentUserInfo(request);
	    	SmGoodInfo smGoodInfo = getModel(SmGoodInfo.class);
	 //     smGoodInfo.setGoodId(IDUtil.getUUID());
	 		
	 		logger.info("saveSmGoodInfo > param>"+smGoodInfo.toLog());
	 
			 SmGoodInfo ori=null;
	 		if(smGoodInfo.getGoodId() != null){
	 			ori=smGoodInfoService.findById(smGoodInfo.getGoodId());
	 		}
	 		
	 		if(ori==null){
	 			smGoodInfoService.save(smGoodInfo);
	 			result.setMessage("保存成功");
				result.setCode(Code.CODE_SUCCESS);
	 		}else{
	    		result.setMessage("此记录已存在");
				result.setCode(Code.CODE_FAIL);
	    	}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("保存失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}
	
	/**
	 * 更改超市商品
	 */
    @RequestMapping("/inner/smGoodInfo/update")
    public void updateSmGoodInfo(HttpServletRequest request,
    HttpServletResponse response){
		Result result = new Result();
		try {
			SysUser admin = SysUserHelper.getCurrentUserInfo(request);
		    SmGoodInfo smGoodInfo = getModel(SmGoodInfo.class);
		    
		    logger.info(" updateSmGoodInfo> param>"+smGoodInfo.toLog());
		    
		    SmGoodInfo ori=smGoodInfoService.findById(smGoodInfo.getGoodId());
		    if(ori == null){
		    	result.setMessage("此记录已删除!");
				result.setCode(Code.CODE_FAIL);
		    }else{
		    	smGoodInfoService.update(smGoodInfo);
				result.setMessage("更新成功!");
				result.setCode(Code.CODE_SUCCESS);
		    }
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("更新失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}
	
	/**
	 * 删除超市商品
	 */
    @RequestMapping("/inner/smGoodInfo/delete")
    public void deleteSmGoodInfoById(HttpServletRequest request,
    HttpServletResponse response){
		Result result = new Result();
		try {
		   	SmGoodInfo smGoodInfo = getModel(SmGoodInfo.class);
		   	
		   	logger.info("deleteSmGoodInfoById > param>"+smGoodInfo.getGoodId());
		   	
			smGoodInfoService.delete(smGoodInfo.getGoodId());
			result.setMessage("删除成功!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("删除失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}
	
	/**
	 * 查询全部超市商品
	 */
    @RequestMapping("/inner/smGoodInfo/findAllSmGoodInfo")
    public void findAllSmGoodInfo(HttpServletRequest request,
    HttpServletResponse response){
		Result result = new Result();
		try {
			List<SmGoodInfo> smGoodInfoList = smGoodInfoService.findAll();
			result.getData().put("smGoodInfoList",smGoodInfoList);
			result.setMessage("findAll smGoodInfo successs!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("findAll smGoodInfo fail!");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}
	
	/**
	 * 删除多个超市商品
	 */
	@RequestMapping("/inner/smGoodInfo/deleteIds")
	public void deleteSmGoodInfo(HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		try {
			String id = request.getParameter("goodId");
			logger.info("delete batch> param>"+id);
			String[] ids = id.split(",");
			List<String> idList = new ArrayList<String>();
			for(int i=0;i<ids.length;i++){
				idList.add(ids[i]);
			}
			if(idList.size()>0){
				smGoodInfoService.deleteBatch(idList);
				result.setMessage("删除成功!");
				result.setCode(Code.CODE_SUCCESS);
			}else{
				result.setMessage("删除失败，请联系管理员!");
				result.setCode(Code.CODE_FAIL);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("delete smGoodInfo fail!");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}
	
	/**
	 * jqGrid多种条件查询
	 */
	@RequestMapping("/inner/smGoodInfo/list")
    public void list(HttpServletRequest request,HttpServletResponse response){
       PaginationResult result = new PaginationResult();
		try {
			SysUser admin = SysUserHelper.getCurrentUserInfo(request);
			int page_number = Integer.parseInt(request.getParameter("page_number"));
			int page_size = Integer.parseInt(request.getParameter("page_size"));
			String keyword=request.getParameter("keyword");
			if(keyword==null){
				keyword="";
			}
			logger.info("list> param>"+page_number+"-"+page_size+"-"+keyword);
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("keyword", keyword);
			PojoDomain<SmGoodInfo> list = smGoodInfoService.list(page_number, page_size, param);
			result.getData().put("list", list.getPojolist());
			result.setPageNumber(list.getPage_number());
			result.setPageSize(list.getPage_size());
			result.setPageTotal(list.getPage_total());
			result.setTotalCount(list.getTotal_count());
			result.setMessage("find smGoodInfo successs!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("find smGoodInfo fail!");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
    }
	
}