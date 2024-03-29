var appInfo = {
	listUrl : top.window.urlBase + '/inner/sysUser/list.do',// 获取用户管理列表接口
	// SysUser
	saveUrl : top.window.urlBase + '/inner/sysUser/save.do',// 保存新增用户管理接口
	updateUrl : top.window.urlBase + '/inner/sysUser/update.do',// 编辑用户管理信息接口
	deleteUrl : top.window.urlBase + '/inner/sysUser/delete.do',// 删除用户管理接口
	resetPwdUrl : top.window.urlBase + '/inner/sysUser/resetPwd.do',// 删除用户管理接口
	activeUrl : top.window.urlBase + '/inner/sysUser/updateActiveFlag.do',// 删除用户管理接口
	selectedId : -1,
	selectedData : {},
	requestParam : {
		page_number : 1,
		page_size : 10,
		keyword : "",
		ssorgid : "",
		s_role : null,
		roleYwType : 1
	},
	formStatus : "new",
};

// 新增
function add() {
	$("#editWin").window({
		title : "新增"
	}).window("open");
	$('#ff').form('clear');
	appInfo.formStatus = "new";
	$("#roleType").combobox("reload");
	$("#roleYwType").val(appInfo.requestParam.roleYwType);
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
// 启禁用
function activeBtn() {
	if (isSelectedOne(appInfo.selectedId)) {
		$.messager.confirm('确认', '确认执行此操作吗?', function(r) {
			if (r) {
				var parms = "userid=" + appInfo.selectedId;
				$.post(appInfo.activeUrl, parms, function(data) {
					if (data.retcode == "0") {
						showMsg("操作成功");
						app.myreload("#tbList");
						appInfo.selectedData = {};
						appInfo.selectedId = -1;
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
	var roleId = $("#roleId").combobox("getValue");
	if (roleId == null || roleId == '') {
		showMsg("默认职位为空");
		return;
	}
	var roleId1 = $("#roleId1").combobox("getValue");
	var roleId2 = $("#roleId2").combobox("getValue");
	if(null !=roleId1 && roleId1 !=''){
		if(roleId==roleId1){
			showMsg("默认职位与职位2相同");
			return;
		}
		if(null !=roleId2 && roleId2 !=''){
			if(roleId1==roleId2){
				showMsg("职位1与职位2相同");
				return;
			}
		}
	}
	if(null !=roleId2 && roleId2 !=''){
		if(roleId==roleId2){
			showMsg("默认职位与职位3相同");
			return;
		}
	}
	
	
	var phoneVar = checkPhone($("#bind_mobile").val());
	if (!phoneVar) {
		showMsg("手机号码格式有误，请重填");
		return;
	}

	var reqUrl = appInfo.formStatus == "new" ? appInfo.saveUrl
			: appInfo.updateUrl;
	var parms = $("#ff").serialize();
	parms += "&formStatus=" + appInfo.formStatus;
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
		height : tool.screenHeight,
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