var appInfo = {
	listUrl : top.window.urlBase + '/inner/sysRole/list.do',// 获取角色管理列表接口
	// SysRole
	saveUrl : top.window.urlBase + '/inner/sysRole/save.do',// 保存新增角色管理接口
	updateUrl : top.window.urlBase + '/inner/sysRole/update.do',// 编辑角色管理信息接口
	deleteUrl : top.window.urlBase + '/inner/sysRole/delete.do',// 删除角色管理接口
	funcListUrl : top.window.urlBase + '/inner/sysRole/funclist.do',// 删除角色管理接口
	funcSaveUrl : top.window.urlBase + '/inner/sysRole/funcSave.do',// 删除角色管理接口
	selectedId : -1,
	selectedData : {},
	requestParam : {
		page_number : 1,
		page_size : 10,
		keyword : "",
		s_lvl : "",
		roleYwType : 2
	},
	formStatus : "new",
	isDeveloper : 0
};

// 新增
function add() {
	$("#editWin").window({
		title : "新增"
	}).window("open");
	$('#ff').form('clear');
	appInfo.formStatus = "new";
	$('#userTypeTr').show();
	$('#showFlagTr').show();
	$('#delFlagTr').show();
	$("#roleYwType").val(2);
	$("#showFlag").combobox("setValue", 1);
	$("#delFlag").combobox("setValue", 1);
	$('#showFlagTr').hide();
}

// 编辑
function edit() {
	if (isSelectedOne(appInfo.selectedId)) {
		if ("developer" == appInfo.selectedId) {
			showMsg("此记录不可编辑");
			return;
		}
		$("#editWin").window({
			title : "编辑"
		}).window("open");
		appInfo.formStatus = "edit";
		$('#ff').form('load', appInfo.selectedData);
		$('#userTypeTr').hide();
		if (appInfo.selectedData.delFlag == 0) {
			$('#delFlagTr').hide();
		} else {
			$('#delFlagTr').show();
		}
	}
}

// 删除
function del() {
	if (!isSelectedOne(appInfo.selectedId)) {
		return;
	}
	if (appInfo.selectedData.delFlag == 0) {
		showMsg("该角色不可删除");
		return;
	}
	$.messager.confirm('确认', '确认要删除该记录吗?', function(r) {
		if (r) {
			var parms = "roleid=" + appInfo.selectedId;
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

function addFunc() {
	if (isSelectedOne(appInfo.selectedId)) {
		funcList(appInfo.selectedId);
		$("#funcWin").window({
			title : "功能管理"
		}).window("open");
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
/**
 * 保存菜单
 */
function formSubmit() {
	if (!$('#ff').form('validate')) {
		showMsg("表单校验不通过");
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
		idField : 'roleid',
		pagination : true,
		pageNumber : appInfo.requestParam.page_number,
		pageSize : appInfo.requestParam.page_size,
		singleSelect : true,
		columns : [ [ {
			title : '编号',
			field : 'roleType',
			width : 100,
			align : "center"
		}, {
			title : '角色名称',
			field : 'rolename',
			width : 100,
			align : "center"
		// }, {
		// title : '组织',
		// field : 'companyName',
		// width : 100,
		// align : "center"
		}, {
			title : '层级',
			field : 'lvl',
			width : 100,
			align : "center",
			formatter : function(value, row, index) {
				return "L" + value;
			}
		}, {
			title : '删除标识',
			field : 'delFlag',
			width : 100,
			align : "center",
			formatter : function(value, row, index) {
				if (value == 0) {
					return "不可删除";
				}
				return "可删除";
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
		}, {
			title : '创建者',
			field : 'creator',
			width : 100,
			align : "center"
		} ] ],
		loader : function(params, success, loadError) {
			var that = $(this);
			loader(that, params, success, loadError);
		},
		onClickRow : function(rowIndex, rowData) {
			appInfo.selectedId = rowData.roleid;
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
		appInfo.requestParam.s_lvl = $("#s_lvl").combobox("getValue");
		$.ajax({
			url : opts.url,
			type : "get",
			data : appInfo.requestParam,
			dataType : "json",
			success : function(data, status, xhr) {
				checkLogin(data);
				if (data.retcode == "0") {
					appInfo.isDeveloper = data.isDeveloper;
					var list = data.list;
					that.data().datagrid["cache"] = data;
					success({
						"total" : list.length,
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
function saveFuncList(roleId) {
	var reqUrl = appInfo.funcSaveUrl;
	var parms = $("#funcff").serialize();
	parms += "&roleId=" + roleId;
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
			$("#funcWin").window("close");
			showMsg("保存成功");
		} else {
			showMsg(data.retmsg);
		}
	});
}

// 全选按钮
function getAll(row) {
	if (row.children.length > 0) {
		for (var i = 0; i < row.children.length; i++) {
			if (row.children[i].oper_list.length > 0) {
				for (var j = 0; j < row.children[i].oper_list.length; j++) {
					if ($("#" + row.menuid).is(':checked')) {
						$("#" + row.children[i].oper_list[j].funcid + "").prop(
								'checked', true);
					} else {
						$("#" + row.children[i].oper_list[j].funcid + "").prop(
								'checked', false);
					}

				}
			}
		}

	} else if (row.oper_list.length > 0) {
		for (var i = 0; i < row.oper_list.length; i++) {
			$("#" + row.oper_list[i].funcid + "").prop('checked', true);
		}
	}
}

function funcList(roleId) {
	$('#tbFuncList')
			.treegrid(
					{
						url : appInfo.funcListUrl,
						rownumbers : true,
						animate : false,
						collapsible : true,
						fitColumns : true,
						idField : 'menuid',
						treeField : 'menuitem',
						toolbar : [ {
							text : "默认功能保存",
							iconCls : "icon-save",
							handler : function() {
								saveFuncList(roleId);
							}
						} ],
						frozenColumns : [ [ {
							title : '菜单ID',
							field : 'menuid',
							width : 100,
							hidden : true
						}, {
							title : '菜单名称',
							field : 'menuitem',
							width : 300
						} ] ],
						columns : [ [ {
							title : '操作',
							field : 'operate',
							width : 350,
							formatter : function(value, row, index) {
								var str = "";
								if (row.children.length > 0) {
									str = '<label><input type="checkbox" name="" checked="checked"  id="'
											+ row.menuid
											+ '" onclick=getAll('
											+ JSON.stringify(row)
											+ ') '
											+ '/>'
											+ '全选' + ' </label>';
								}
								var funcList = row.oper_list;
								if (funcList.length > 0) {
									for (var i = 0; i < funcList.length; i++) {
										str += '<label><input name="funcId" type="checkbox" id="'
												+ funcList[i].funcid
												+ '" value="'
												+ funcList[i].funcid
												+ '" '
												+ funcList[i].state
												+ '/>'
												+ funcList[i].opername
												+ ' </label>';
									}
								}
								return str;
							}
						} ] ],
						loader : function(params, success, loadError) {
							var that = $(this);
							funcloader(that, params, success, loadError);
						},
						onClickRow : function(rowIndex, rowData) {
							// $("#tbFuncList").treegrid("beginEdit", rowIndex);
						},
						onLoadSuccess : function(row, data) {
							var list = data.rows;
							for (var i = 0; i < list.length; i++) {
								if (list[i].children.length > 0) {
									for (var j = 0; j < list[i].children.length; j++) {
										if (list[i].children[j].oper_list.length > 0) {
											for (var k = 0; k < list[i].children[j].oper_list.length; k++) {
												if (!list[i].children[j].oper_list[k].state) {
													$("#" + list[i].menuid)
															.prop("checked",
																	false);
													break;
												}
											}

										}
									}
								}
							}
						}
					});
	// 请求加载数据
	function funcloader(that, params, success, loadError) {
		var opts = that.treegrid("options");
		$.ajax({
			url : opts.url,
			data : {
				"roleId" : roleId
			},
			dataType : "json",
			type : "get",
			success : function(data, status, xhr) {
				checkLogin(data);
				if (data.retcode == "0") {
					var list = data.funcList;
					that.data().treegrid["cache"] = data;
					success({
						"total" : data.total_count,
						"rows" : list
					});
					return true;
				} else {
					showMsg(data.retmsg);
				}
			},
			error : function(err) {
				loadError.apply(this, arguments);
			}
		});
	}
}
