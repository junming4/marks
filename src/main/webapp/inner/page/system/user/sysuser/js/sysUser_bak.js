var appInfo = {
	listUrl : top.window.urlBase + '/inner/sysUser/list.do',// 获取用户管理列表接口
	// SysUser
	saveUrl : top.window.urlBase + '/inner/sysUser/save.do',// 保存新增用户管理接口
	updateUrl : top.window.urlBase + '/inner/sysUser/update.do',// 编辑用户管理信息接口
	deleteUrl : top.window.urlBase + '/inner/sysUser/delete.do',// 删除用户管理接口
	resetPwdUrl : top.window.urlBase + '/inner/sysUser/resetPwd.do',// 删除用户管理接口
	roleUrl : top.window.urlBase + '/inner/sysRole/findSysRoleById.do',// 删除用户管理接口
	selectedId : -1,
	selectedData : {},
	requestParam : {
		page_number : 1,
		page_size : 10,
		keyword : "",
		ssorgid : "",
		s_role : null
	},
	formStatus : "new",
	checkOrg : {
		orgids : [],
		orgnames : []
	}
};

// 新增
function add() {
	$("#editWin").window({
		title : "新增"
	}).window("open");
	$('#ff').form('clear');
	appInfo.formStatus = "new";
	appInfo.checkOrg.orgids = [];
	appInfo.checkOrg.orgnames = [];
	$("#roleType").combobox("reload");
	$("#inputOrgDiv").html("");
	$("#bind_mobile").numberbox({
		disabled : false
	});
}

// 编辑
function edit() {
	if (isSelectedOne(appInfo.selectedId)) {
		$("#editWin").window({
			title : "编辑"
		}).window("open");
		appInfo.formStatus = "edit";
		$("#roleType").combobox("reload");
		$('#ff').form('load', appInfo.selectedData);
		appInfo.checkOrg.orgids = [];
		appInfo.checkOrg.orgnames = [];
		$("#inputOrgDiv").html("");
		initUser(appInfo.selectedData);
		notEdit();
	}
}

// 删除
function del() {
	if (isSelectedOne(appInfo.selectedId)) {
		$.messager.confirm('确认', '确认要删除该记录吗?', function(r) {
			if (r) {
				var parms = "userid=" + appInfo.selectedId;
				$.post(appInfo.deleteUrl, parms, function(data) {
					if (data.retcode == "0") {
						app.myreload("#tbList");
						appInfo.selectedData = {};
						appInfo.selectedId = -1;
						showMsg("删除成功");
					} else {
						showMsg(data.retmsg);
					}
				});
			}
		});
	}
}

// 重置密码
function resetPwdBtn() {
	if (isSelectedOne(appInfo.selectedId)) {
		$.messager.confirm('确认', '确认重置密码吗?', function(r) {
			if (r) {
				var parms = "userid=" + appInfo.selectedId;
				$.post(appInfo.resetPwdUrl, parms, function(data) {
					if (data.retcode == "0") {
						showMsg("重置成功");
					} else {
						showMsg(data.retmsg);
					}
				});
			}
		});
	}
}

$(function() {
	// 加载列表
	loadList();

	// 搜索
	$("#doSearch").on("click", function(e) {
		app.myreload("#tbList");
		appInfo.selectedData = {};
		appInfo.selectedId = -1;
	});

	// 保存菜单
	$("#btnOK").on("click", function() {
		formSubmit();
	});
	$("#btnCancel").on("click", function() {
		$("#editWin").window("close");
	});

	$("#chooseOrg").on("click", function() {
		// 加载组织信息
		loadOrgList("");
		$("#orgWin").window({
			title : "组织"
		}).window("open");
	});
	$("#orgBtn").on("click", function() {
		if (appInfo.checkOrg.orgids.length == 0) {
			showMsg("所选组织为空");
			return;
		}
		$("#orgWin").window("close");
	});
});
function checkPhone(val) {
	if (!(/^1[34578]\d{9}$/.test(val))) {
		return false;
	}
	return true;
}
/**
 * 保存菜单
 */
function formSubmit() {
	if (!$('#ff').form('validate')) {
		showMsg("表单校验不通过");
		return;
	}
	if (appInfo.checkOrg.orgids.length == 0) {
		showMsg("所属组织为空");
		return;
	}
	var phoneVar = checkPhone($("#bind_mobile").val());
	if (!phoneVar) {
		showMsg("手机号码格式有误，请重填");
		return;
	}

	var reqUrl = appInfo.formStatus == "new" ? appInfo.saveUrl
			: appInfo.updateUrl;
	$("#bind_mobile").numberbox({
		disabled : false
	});
	var parms = $("#ff").serialize();
	parms += "&formStatus=" + appInfo.formStatus;
	parms += "&orgId=" + appInfo.checkOrg.orgids[0]+"";
	$.post(reqUrl, parms, function(data) {
		if (typeof data === 'string') {
			try {
				data = $.parseJSON(data);
			} catch (e0) {
				showMsg("json格式错误");
				return;
			}
		}
		if (data.retcode == "0") {
			$("#editWin").window("close");
			app.myreload("#tbList");
			appInfo.selectedData = {};
			appInfo.selectedId = -1;
			showMsg("保存成功");
		} else {
			showMsg(data.retmsg);
		}
	});
}
function notEdit() {
	if (appInfo.formStatus != "new") {
		$("#bind_mobile").numberbox({
			disabled : true
		});
	}
}
function loadList() {
	$('#tbList').datagrid({
		url : appInfo.listUrl,
		toolbar : "#tb",
		striped : true,
		nowrap : true,
		rownumbers : true,
		animate : true,
		collapsible : true,
		fitColumns : true,
		height : 580,
		pagination : true,
		idField : 'userid',
		pagination : true,
		pageNumber : appInfo.requestParam.page_number,
		pageSize : appInfo.requestParam.page_size,
		singleSelect : true,
		columns : [ [ {
			title : '用户编号',
			field : 'userCode',
			width : 120
		}, {
			title : '绑定手机',
			field : 'bind_mobile',
			width : 100,
			align : "center"
		}, {
			title : '用户名称',
			field : 'username',
			width : 100,
			align : "center"
		}, {
			title : '绑定标识',
			field : 'bindFlag',
			width : 100,
			align : "center",
			formatter : function(value, row, index) {
				if (value == 1) {
					return "绑定";
				} else {
					return "未绑定";
				}
			}
		}, {
			title : '用户类型',
			field : 'roleName',
			width : 100,
			align : "center"
		}, {
			title : '所属组织',
			field : 'orgName',
			width : 100,
			align : "center"
		}, {
			title : '激活标识',
			field : 'activeFlag',
			width : 100,
			align : "center",
			formatter : function(value, row, index) {
				if (value == 1) {
					return "启用";
				} else {
					return "禁用";
				}
			}

		}, {
			title : '创建时间',
			field : 'createtime',
			width : 100,
			align : "center"
		}, {
			title : '更新时间',
			field : 'updatetime',
			width : 100,
			align : "center"

		} ] ],
		loader : function(params, success, loadError) {
			var that = $(this);
			loader(that, params, success, loadError);
		},
		onClickRow : function(rowIndex, rowData) {
			appInfo.selectedId = rowData.userid;
			appInfo.selectedData = rowData;
		},
		onLoadSuccess : function(data) {
			$("#tbList").datagrid('unselectAll');
			appInfo.selectedData = {};
		}
	});
	// 请求加载数据
	function loader(that, params, success, loadError) {
		var opts = that.datagrid("options");
		appInfo.requestParam.page_number = params.page;
		appInfo.requestParam.page_size = params.rows;
		appInfo.requestParam.keyword = $("#keyword").val();
		appInfo.requestParam.ssorgid = $("#ssorgid").combotree("getValue");
		appInfo.requestParam.s_role = $("#s_role").combotree("getValue");
		$.ajax({
			url : opts.url,
			type : "get",
			data : appInfo.requestParam,
			dataType : "json",
			success : function(data, status, xhr) {
				checkLogin(data);
				if (data.retcode == "0") {
					var list = data.list;
					that.data().datagrid["cache"] = data;
					success({
						"total" : data.total_count,
						"rows" : list
					});
					return true;
				} else {
					showMsg(data.retmsg);
					success({
						"total" : 0,
						"rows" : []
					});
				}
			},
			error : function(err) {
				loadError.apply(this, arguments);
			}
		});
	}
}

function loadOrgList(id) {
	var orgListUrl = top.window.urlBase + '/inner/orgInfo/list.do?companyId='+id;
	$('#orgList').treegrid(
			{
				url : orgListUrl,
				striped : true,
				nowrap : true,
				rownumbers : true,
				animate : true,
				collapsible : true,
				fitColumns : true,
				idField : 'orgid',
				treeField : 'orgname',
				singleSelect : true,
				queryParams : {
					userflag : 1
				},
				columns : [ [ {
					title : '组织名称',
					field : 'orgname',
					width : 150,
					align : "left"
				}, {
					title : '组织ID',
					field : 'orgid',
					width : 100,
					align : "center"
				}, {
					title : '组织类型',
					field : 'orgTypeName',
					width : 100,
					align : "center"
				} ] ],
				onBeforeExpand : function(row) {
					$("#orgList").treegrid("options").url = orgListUrl
							+ "&parentId=" + row.orgid + "&_timer="
							+ new Date().getTime();
				},
				onClickRow : function(rowData) {

					if (appInfo.checkOrg.orgids.length > 0) {
						showMsg("只能选择一个组织");
						return;
					}
					if (addOrg(rowData.orgid, rowData.orgname)) {
						initOrgPut(rowData.orgid, rowData.orgname);
					}
					return;

				},
				onLoadSuccess : function(row, data) {

				}
			});
}

// 删除功能
function delOrgTr(id, name) {
	delOrg(id, name);
	$("#" + id).remove();

}
Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};
Array.prototype.indexOf = function(obj) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == obj) {
			return i;
		}
	}
	return -1;
}

function addOrg(id, name) {
	var idx = appInfo.checkOrg.orgids.indexOf(id);//
	if (idx < 0) {
		appInfo.checkOrg.orgids.push(id);
		appInfo.checkOrg.orgnames.push(name);
		return true;
	}
	return false;
}
function delOrg(id, name) {

	var idx = appInfo.checkOrg.orgids.indexOf(id);//
	if (idx > -1) {
		appInfo.checkOrg.orgids.remove(id);
		appInfo.checkOrg.orgnames.remove(name);
	}
}
function initUser(user) {
	appInfo.checkOrg.orgids = user.orgidsStr.split(",");
	appInfo.checkOrg.orgnames = user.orgidNamesStr.split(",");
	for (var i = 0; i < appInfo.checkOrg.orgids.length; i++) {
		console.log("1 - " + appInfo.checkOrg.orgids[i]);
		if (appInfo.checkOrg.orgids[i] != null
				&& appInfo.checkOrg.orgids[i] != '') {
			initOrgPut(appInfo.checkOrg.orgids[i], appInfo.checkOrg.orgnames[i]);
		}
	}
}
function initOrgPut(orgid, orgname) {
	console.log(orgid + " - " + orgname);
	$("#inputOrgDiv")
			.append(
					"<p id='"
							+ orgid
							+ "'>组织：<span>"
							+ orgname
							+ "</span>&nbsp;&nbsp;<a href='#' onclick=\"javascript:delOrgTr(\'"
							+ orgid + "\',\'" + orgname + "\')\">删除</a></p>");
}