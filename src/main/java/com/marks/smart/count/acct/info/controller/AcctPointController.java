package com.marks.smart.count.acct.info.controller;

import java.util.HashMap;
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
import com.marks.common.util.string.IStringUtil;
import com.marks.common.util.IDUtil;
import com.marks.common.util.Code;
import com.marks.smart.system.core.controller.SupportContorller;
import com.marks.smart.system.user.login.helper.LoginUtil;
import com.marks.smart.system.user.sysuser.pojo.SysUser;

import com.marks.smart.count.acct.info.pojo.AcctPoint;
import com.marks.smart.count.acct.info.service.AcctPointService;

 /**
	 * 用户积分: 
	 */
@Controller
public class AcctPointController extends SupportContorller{
    private static Logger logger = Logger.getLogger( AcctPointController.class);
    
    @Autowired
    private AcctPointService  acctPointService;
   

    @Override
	public Logger getLogger() {
		return logger;
	}

    /**
	 * 查询用户积分
	 */
    @RequestMapping("/inner/acctPoint/findById")
    public void findAcctPointById(HttpServletRequest request,
    HttpServletResponse response){
        Result result = new Result();
		try {
		    AcctPoint info = getModel(AcctPoint.class,request.getParameterMap());
		    
		    logger.info("findAcctPointById > param>"+info.getUserid());
		    
			AcctPoint vo = acctPointService.findById(info.getUserid());
			result.getData().put("info",vo);
			result.setMessage("findById successs!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("查询失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
    }
    
    /**
	 * 保存用户积分
	 */
    @RequestMapping("/inner/acctPoint/save")
    public void saveAcctPoint(HttpServletRequest request,
    HttpServletResponse response){
		Result result = new Result();
		try {
			SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
	    	AcctPoint info = getModel(AcctPoint.class,request.getParameterMap());
	        info.setUserid(IDUtil.getUUID());
	 		
	 		logger.info("saveAcctPoint > param>"+info.toLog());
	 
			 AcctPoint ori=null;
	 		if(info.getUserid() != null){
	 			ori=acctPointService.findById(info.getUserid());
	 		}
	 		
	 		if(ori==null){
	 			acctPointService.save(info);
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
	 * 更改用户积分
	 */
    @RequestMapping("/inner/acctPoint/update")
    public void updateAcctPoint(HttpServletRequest request,
    HttpServletResponse response){
		Result result = new Result();
		try {
			SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
		    AcctPoint info = getModel(AcctPoint.class,request.getParameterMap());
		    
		    logger.info(" updateAcctPoint> param>"+info.toLog());
		    
		    AcctPoint ori=acctPointService.findById(info.getUserid());
		    if(ori == null){
		    	result.setMessage("此记录已删除!");
				result.setCode(Code.CODE_FAIL);
		    }else{
		    	acctPointService.update(info);
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
	 * 删除用户积分
	 */
    @RequestMapping("/inner/acctPoint/delete")
    public void deleteAcctPointById(HttpServletRequest request,
    HttpServletResponse response){
		Result result = new Result();
		try {
		   	AcctPoint info = getModel(AcctPoint.class,request.getParameterMap());
		   	
		   	logger.info("deleteAcctPointById > param>"+info.getUserid());
		   	
			acctPointService.delete(info.getUserid());
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
	 * 查询全部用户积分
	 */

    /*
    @RequestMapping("/inner/acctPoint/findAllAcctPoint")
    public void findAllAcctPoint(HttpServletRequest request,
    HttpServletResponse response){
		Result result = new Result();
		try {
			List<AcctPoint> allList = acctPointService.findAll();
			result.getData().put("allList",allList);
			result.setMessage("findAll acctPoint successs!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("findAll acctPoint fail!");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	} 
	*/
	
	/**
	 * 删除多个用户积分
	 */
	/*
	@RequestMapping("/inner/acctPoint/deleteIds")
	public void deleteAcctPoint(HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		try {
			String id = request.getParameter("userid");
			logger.info("delete batch> param>"+id);
			String[] ids = id.split(",");
			List<String> idList = new ArrayList<String>();
			for(int i=0;i<ids.length;i++){
				idList.add(ids[i]);
			}
			if(idList.size()>0){
				acctPointService.deleteBatch(idList);
				result.setMessage("删除成功!");
				result.setCode(Code.CODE_SUCCESS);
			}else{
				result.setMessage("删除失败，请联系管理员!");
				result.setCode(Code.CODE_FAIL);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("delete acctPoint fail!");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}
	*/
	
	/**
	 * 列表查询
	 */
	@RequestMapping("/inner/acctPoint/list")
    public void list(HttpServletRequest request,HttpServletResponse response){
       PaginationResult result = new PaginationResult();
		try {
			SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
			int page_number = Integer.parseInt(request.getParameter("page_number"));
			int page_size = Integer.parseInt(request.getParameter("page_size"));
			if (page_size > 200) {
				page_size = 200;
			}
			String keyword=IStringUtil.getUTF8(request.getParameter("keyword"));
			logger.info("list> param>"+page_number+"-"+page_size+"-"+keyword);
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("keyword", keyword);
			PojoDomain<AcctPoint> list = acctPointService.list(page_number, page_size, param);
			result.getData().put("list", list.getPojolist());
			result.setPageNumber(list.getPage_number());
			result.setPageSize(list.getPage_size());
			result.setPageTotal(list.getPage_total());
			result.setTotalCount(list.getTotal_count());
			result.setMessage("find acctPoint successs!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			result.setMessage("find acctPoint fail!");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
    }
	
}