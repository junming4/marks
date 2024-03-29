package com.marks.smart.system.org.orginfo.controller;

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
import com.marks.common.enums.Enums;
import com.marks.common.enums.OrgEnums;
import com.marks.common.util.Code;
import com.marks.common.util.JsonUtil;
import com.marks.smart.system.core.controller.SupportContorller;
import com.marks.smart.system.org.orginfo.pojo.OrgInfo;
import com.marks.smart.system.org.orginfo.service.OrgInfoService;
import com.marks.smart.system.user.login.helper.LoginUtil;
import com.marks.smart.system.user.sysrole.pojo.SysRole;
import com.marks.smart.system.user.sysrole.service.SysRoleService;
import com.marks.smart.system.user.sysuser.pojo.SysUser;

import net.sf.json.JSONArray;

@Controller
public class OrgInfoController extends SupportContorller {
	private static Logger logger = Logger.getLogger(OrgInfoController.class);

	@Autowired
	private OrgInfoService orgInfoService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public Logger getLogger() {
		return logger;
	}

	/**
	 * 查询机构管理
	 */
	@RequestMapping("/inner/orgInfo/findOrgInfoById")
	public void findOrgInfoById(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		try {
			OrgInfo orgInfo = getModel(OrgInfo.class);
			OrgInfo requestOrgInfo = orgInfoService.findById(orgInfo.getOrgid());
			result.getData().put("orgInfo", requestOrgInfo);
			result.setMessage("findById orgInfo successs!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setMessage("查询失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}

	/**
	 * 保存机构管理
	 */
	@RequestMapping("/inner/orgInfo/save")
	public void saveOrgInfo(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		try {
			SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
			OrgInfo orgInfo = getModel(OrgInfo.class);
			// orgInfo.setOrgid(IDUtil.getTimeID());
			OrgInfo ori = null;
			if (orgInfo.getLogoId() != null) {
				ori = orgInfoService.findById(orgInfo.getLogoId());
			}

			if (ori == null) {
				if (OrgEnums.OrgType.company.getValue() == orgInfo.getOrgType()) {
					orgInfo.setOrgCategory(OrgEnums.OrgCategory.company.getValue());
				} else {
					if ("top".equals(orgInfo.getParentId())) {
						orgInfo.setParentId(admin.getCompanyId());
					}
				}
				orgInfo.setCompanyId(admin.getCompanyId());
				orgInfo.setCreator(admin.getOperator());
				orgInfoService.save(orgInfo);
				result.setMessage("保存成功");
				result.setCode(Code.CODE_SUCCESS);
			} else {
				result.setMessage("此记录已存在");
				result.setCode(Code.CODE_FAIL);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setMessage("保存失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}

	/**
	 * 更改机构管理
	 */
	@RequestMapping("/inner/orgInfo/update")
	public void updateOrgInfo(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		try {
			SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
			OrgInfo orgInfo = getModel(OrgInfo.class);
			OrgInfo ori = orgInfoService.findById(orgInfo.getOrgid());
			if (ori == null) {
				result.setMessage("此记录已删除!");
				result.setCode(Code.CODE_FAIL);
				JsonUtil.output(response, result);
				return;
			}
			if (OrgEnums.OrgType.company.getValue() != orgInfo.getOrgType() && ori.getChildnum() > 0
					&& !ori.getParentId().equals(orgInfo.getParentId())) {
				result.setMessage("此记录下有子节点不能更换父节点!");
				result.setCode(Code.CODE_FAIL);
				JsonUtil.output(response, result);
				return;
			}
			if ("top".equals(orgInfo.getParentId())) {
				orgInfo.setParentId(admin.getCompanyId());
			}
			orgInfoService.update(orgInfo);
			result.setMessage("更新成功!");
			result.setCode(Code.CODE_SUCCESS);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setMessage("更新失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}

	/**
	 * 删除机构管理
	 */
	@RequestMapping("/inner/orgInfo/delete")
	public void deleteOrgInfoById(HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		try {
			OrgInfo orgInfo = getModel(OrgInfo.class);
			List<OrgInfo> list = orgInfoService.getChildList(orgInfo.getOrgid());
			if (list != null && list.size() > 0) {
				result.setMessage("含子节点不能删除!");
				result.setCode("4001");
				JsonUtil.output(response, result);
				return;
			}
			List<SysRole> roleList = sysRoleService.listByOrgId(orgInfo.getOrgid());
			if (null != roleList && roleList.size() > 0) {
				result.setMessage("机构下挂有职位/角色，故不能删除!");
				result.setCode("4002");
				JsonUtil.output(response, result);
				return;
			}
			orgInfoService.delete(orgInfo.getOrgid());
			result.setMessage("删除成功!");
			result.setCode(Code.CODE_SUCCESS);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setMessage("删除失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}

	/**
	 * jqGrid多种条件查询
	 */
	@RequestMapping("/inner/orgInfo/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {

		SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
		String parentId = request.getParameter("parentId");
		String orgType = request.getParameter("orgType");
		logger.info("list parentId:" + parentId);
		String companyId = admin.getCompanyId();
		logger.info("list companyId:" + companyId);
		List<OrgInfo> list = null;

		// 根节点加载
		if (parentId == null || "".equals(parentId)) {
			parentId = admin.getCompanyId();
		}
		String useFlag = "";
		logger.info("list parentId:" + parentId + " - " + admin.getCompanyId());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		params.put("companyId", companyId);
		params.put("orgType", orgType);
		params.put("useFlag", useFlag);
		list = orgInfoService.listGrid(params);
		JsonUtil.output(response, JSONArray.fromObject(list).toString());
		return;
	}

	/**
	 * jqGrid多种条件查询
	 */
	@RequestMapping("/inner/orgInfo/treebox")
	public void treebox(HttpServletRequest request, HttpServletResponse response) {

		SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
		String parentId = request.getParameter("parentId");
		String orgType = request.getParameter("orgType");
		logger.info("list parentId:" + parentId);
		String companyId = admin.getCompanyId();
		logger.info("list companyId:" + companyId);
		List<OrgInfo> list = null;

		// 根节点加载
		if (parentId == null || "".equals(parentId)) {
			parentId = admin.getCompanyId();
		}
		String useFlag = "1";
		logger.info("list parentId:" + parentId + " - " + admin.getCompanyId() + " - " + orgType + " - ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		params.put("companyId", companyId);
		params.put("orgType", orgType);
		params.put("useFlag", useFlag);
		list = orgInfoService.listGrid(params);
		JsonUtil.output(response, JSONArray.fromObject(list).toString());
		return;
	}

	/**
	 * jqGrid多种条件查询
	 */
	@RequestMapping("/inner/orgInfo/tree")
	public void tree(HttpServletRequest request, HttpServletResponse response) {

		SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
		String parentId = request.getParameter("parentId");
		String orgType = request.getParameter("orgType");
		logger.info("list parentId:" + parentId);
		String companyId = admin.getCompanyId();
		logger.info("list companyId:" + companyId);
		List<OrgInfo> list = null;

		// 根节点加载
		if (parentId == null || "".equals(parentId)) {
			parentId = admin.getCompanyId();
		}
		logger.info("list parentId:" + parentId + " - " + admin.getCompanyId() + " - " + orgType + " - ");
		list = orgInfoService.list(admin.getCompanyId(), parentId, orgType);
		JsonUtil.output(response, JSONArray.fromObject(list).toString());
		return;
	}

	/**
	 * jqGrid多种条件查询
	 */
	@RequestMapping("/inner/orgInfo/framelist")
	public void framelist(HttpServletRequest request, HttpServletResponse response) {
		PaginationResult result = new PaginationResult();
		try {
			SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
			int page_number = Integer.parseInt(request.getParameter("page_number"));
			int page_size = Integer.parseInt(request.getParameter("page_size"));
			String keyword = request.getParameter("keyword");
			String orgType = request.getParameter("orgType");
			String orgCategory = request.getParameter("orgCategory");
			String companyId = admin.getCompanyId();
			if (OrgEnums.OrgCategory.company.toString().equals(orgCategory)) {
				companyId = "";
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("keyword", keyword);
			param.put("companyId", companyId);
			param.put("orgType", orgType);
			param.put("orgCategory", orgCategory);
			PojoDomain<OrgInfo> list = orgInfoService.framelist(page_number, page_size, param);
			result.getData().put("list", list.getPojolist());
			result.setPageNumber(list.getPage_number());
			result.setPageSize(list.getPage_size());
			result.setPageTotal(list.getPage_total());
			result.setTotalCount(list.getTotal_count());
			result.setMessage("find sysRole successs!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setMessage("find sysRole fail!");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}

	@RequestMapping("/inner/orgInfo/combo")
	public void combo(HttpServletRequest request, HttpServletResponse response) {
		SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
		String orgType = request.getParameter("orgType");
		String orgCategory = request.getParameter("orgCategory");
		Map<String, Object> param = new HashMap<String, Object>();
		String companyId = admin.getCompanyId();
		param.put("companyId", companyId);
		param.put("orgType", orgType);
		param.put("orgCategory", orgCategory);
		List<OrgInfo> list = orgInfoService.frameCombo(param);
		JsonUtil.output(response, JSONArray.fromObject(list).toString());
	}

	@RequestMapping("/inner/orgInfo/approve")
	public void approve(HttpServletRequest request, HttpServletResponse response) {

		Result result = new Result();
		try {
			SysUser admin = LoginUtil.getInstance().getCurrentUser(request);
			String idNo = request.getParameter("idNo");
			String checkStatus = Enums.CheckStatus.checkOk.getValue() + "";
			OrgInfo info = orgInfoService.findById(idNo);
			if (info == null) {
				result.setMessage("参数错误！");
				result.setCode("4001");
				JsonUtil.output(response, result);
				return;
			}

			Map<String, String> map = new HashMap<String, String>();
			map.put("idNo", idNo);
			map.put("checkStatus", checkStatus);
			map.put("checkerId", admin.getUserCode());
			map.put("checker", admin.getUsername());
			map.put("companyId", admin.getCompanyId());
			orgInfoService.updateCheckStatus(map);

			result.setMessage("删除成功!");
			result.setCode(Code.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setMessage("删除失败，请联系管理员！");
			result.setCode(Code.CODE_FAIL);
		}
		JsonUtil.output(response, result);
	}

}