var appInfo = {
	listUrl : top.window.urlBase + '/inner/dispatchInfo/list.do',// 获取采购单列表接口
	// DispatchInfo
	saveUrl : top.window.urlBase + '/inner/dispatchInfo/save.do',// 保存新增采购单接口
	updateUrl : top.window.urlBase + '/inner/dispatchInfo/update.do',// 编辑采购单信息接口
	deleteUrl : top.window.urlBase + '/inner/dispatchInfo/delete.do',// 删除采购单接口
	selectedId : -1,
	selectedData : {},
	requestParam : {
		page_number : 1,
		page_size : 10,
		keyword : "",
		ywCode:2
	},
	formStatus : "new"
};
// -----------------权限控制功能 start---------------
// 新增
function add() {
	var path = "dispatchGood.html?formStatus=new&returnType=0&idNo=1";
	location.href = path;
}

// 编辑
function edit() {
	if (!isSelectedOne(appInfo.selectedId)) {
		return;
	}
	if (appInfo.selectedData.checkStatus == 1) {
		showMsg("审核通过，不可编辑");
		return;
	}
	if (appInfo.selectedData.checkStatus == 3) {
		showMsg("审核中，不可编辑");
		return;
	}
	var path = "dispatchGood.html?formStatus=edit&returnType=0&idNo="
			+ appInfo.selectedId;
	location.href = path;
}
function receive() {
	if (!isSelectedOne(appInfo.selectedId)) {
		return;
	}
	if (appInfo.selectedData.checkStatus != 1) {
		showMsg("审核未通过，不可收货");
		return;
	}
	var path = "dispatchGood.html?formStatus=receive&returnType=0&idNo="
			+ appInfo.selectedId;
	location.href = path;
}

function send(){
	if (!isSelectedOne(appInfo.selectedId)) {
		return;
	}
	if (appInfo.selectedData.checkStatus != 1) {
		showMsg("审核未通过，不可发货");
		return;
	}
	if (appInfo.selectedData.typeCode != "11") {
		showMsg("非要货单，无需发货");
		return;
	}
	var path = "dispatchGood.html?formStatus=send&returnType=0&idNo="
			+ appInfo.selectedId;
	location.href = path;
}
// 删除
function del() {
	if (!isSelectedOne(appInfo.selectedId)) {
		return;
	}

	$.messager.confirm('确认', '确认要删除该记录吗?', function(r) {
		if (r) {
			var parms = "orderId=" + appInfo.selectedId;
			$.post(appInfo.deleteUrl, parms, function(data) {
				if (data.retcode == '0') {
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
// -----------------权限控制功能 end---------------
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

		idField : 'orderId',
		height : tool.screenHeight,
		rownumbers : true,
		pagination : true,
		pageNumber : appInfo.requestParam.page_number,
		pageSize : appInfo.requestParam.page_size,
		singleSelect : true,
		columns : [ [ {
			title : '单号',
			field : 'orderId',
			width : 200,
			align : "center",
		}, {
			title : '类型',
			field : 'typeName',
			width : 100,
			align : "center"
		}, {
			title : '单状态',
			field : 'statusName',
			width : 100,
			align : "center"
		}, {
			title : '收货门店',
			field : 'receiveOrgName',
			width : 100,
			align : "center"
		}, {
			title : '发货门店',
			field : 'sendOrgName',
			width : 100,
			align : "center"
		}, {
			title : '下单时间',
			field : 'commitTime',
			width : 100,
			align : "center"
		}, {
			title : '商品总额',
			field : 'totalAmt',
			width : 100,
			align : "center"
		}, {
			title : '应付金额',
			field : 'payableAmt',
			width : 100,
			align : "center"
		}, {
			title : '实付金额',
			field : 'payAmt',
			width : 100,
			align : "center"
		}, {
			title : '未付金额',
			field : 'unpayAmt',
			width : 100,
			align : "center"
		}, {
			title : '总数量',
			field : 'totalNums',
			width : 100,
			align : "center"
		}, {
			title : '销售金额',
			field : 'salesAmt',
			width : 100,
			align : "center"
		}, {
			title : '赠送总数',
			field : 'sendNums',
			width : 100,
			align : "center"
		}, {
			title : '收货数量',
			field : 'receiveNums',
			width : 100,
			align : "center"
		}, {
			title : '配送数量',
			field : 'dispatchNums',
			width : 100,
			align : "center"
//		}, {
//			title : '申请人编号',
//			field : 'applyManCode',
//			width : 100,
//			align : "center"
		}, {
			title : '申请人',
			field : 'applyMan',
			width : 100,
			align : "center"
		}, {
			title : '联系人',
			field : 'sendContactor',
			width : 100,
			align : "center"
		}, {
			title : '联系电话',
			field : 'sendContact',
			width : 100,
			align : "center"
		}, {
			title : '审核时间',
			field : 'checkTime',
			width : 100,
			align : "center"
		}, {
			title : '审核人',
			field : 'checker',
			width : 100,
			align : "center"
		}, {
			title : '审核状态',
			field : 'checkStatusName',
			width : 100,
			align : "center"
		}, {
			title : '年份',
			field : 'i_year',
			width : 100,
			align : "center"
		}, {
			title : '月份',
			field : 'i_month',
			width : 100,
			align : "center"
		}, {
			title : '季度',
			field : 'i_season',
			width : 100,
			align : "center"
		} ] ],
		loader : function(params, success, loadError) {
			var that = $(this);
			loader(that, params, success, loadError);
		},
		onClickRow : function(rowIndex, rowData) {
			appInfo.selectedId = rowData.orderId;
			appInfo.selectedData = rowData;
		},
		onDblClickRow : function(rowIndex, rowData) {
			appInfo.selectedId = rowData.orderId;
			appInfo.selectedData = rowData;
			edit();
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
		$.ajax({
			url : opts.url,
			type : "get",
			data : appInfo.requestParam,
			dataType : "json",
			success : function(data, status, xhr) {
				if (data.retcode == '0') {
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
