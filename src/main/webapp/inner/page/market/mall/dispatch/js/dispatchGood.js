var appInfo = {
	listUrl : top.window.urlBase + '/inner/dispatchInfo/findById.do',// 获取采购商品列表接口
	getGoodUrl : top.window.urlBase + '/inner/dispatchGood/findGoodInfoById.do',// 获取采购商品列表接口
	saveUrl : top.window.urlBase + '/inner/dispatchInfo/save.do',// 保存新增采购单接口
	updateUrl : top.window.urlBase + '/inner/dispatchInfo/update.do',// 编辑采购单信息接口
	approveUrl : top.window.urlBase + '/inner/dispatchInfo/approve.do',// 编辑采购单信息接口
	receiveUrl : top.window.urlBase + '/inner/dispatchInfo/receive.do',// 编辑采购单信息接口
	sendUrl: top.window.urlBase + '/inner/dispatchInfo/send.do',// 编辑采购单信息接口
	// DispatchGood
	selectedId : -1,
	selectedData : {},
	requestParam : {
		page_number : 1,
		page_size : 10,
		keyword : ""
	},
	formStatus : "new",
	typeCode:""
};
var orderId = tool.getUrlParams("idNo");
var edit = tool.getUrlParams("formStatus");
var returnType = tool.getUrlParams("returnType");
// -----------------权限控制功能 start---------------
appInfo.formStatus = edit;
// -----------------权限控制功能 end---------------
$(function() {

	// 加载列表
	loadList();
	// 保存菜单
	$("#btnOK").on("click", function() {
		formSubmit();
	});
	$("#returnParent").on("click", function(e) {
		returnList();
	});
	$("#btnCheck").on("click",function(e){
		$.messager.confirm('确认', '确认提交审核吗?', function(r) {
			if (r) {
				var parms = "idNo=" +$("#orderId").val();
				$.post(appInfo.approveUrl, parms, function(data) {
					if (data.retcode == '0') {
						showMsg("操作成功");
						returnList();
					} else {
						showMsg(data.retmsg);
					}
				});
			}
		});
	});
	$("#tbList").datagrid("hideColumn", "hadDispatchNums"); // 设置隐藏列
	$("#tbList").datagrid("hideColumn", "dispatchNums"); // 设置隐藏列
	$("#tbList").datagrid("hideColumn", "hadReceiveNums"); // 设置隐藏列
	$("#tbList").datagrid("hideColumn", "receiveNums"); // 设置隐藏列
	$("#tbList").datagrid("hideColumn", "oper"); 
});
function returnList(){
	var url = "./dispatchInfo.html?&menuid=M_MALL_DISPATCH_DISPATCHINFO";
	location.href = url;
}
function showOrHide(flag){
	$("#btnDiv").show();
	if(flag==1){
		if(appInfo.typeCode=="11"){
			$("#tbList").datagrid("showColumn", "hadDispatchNums"); // 设置隐藏列
		}
		$("#tbList").datagrid("showColumn", "hadReceiveNums"); // 设置隐藏列
		$("#tbList").datagrid("showColumn", "receiveNums"); // 设置隐藏列
		$("#btnCheck").hide();
	}else if(flag==2){
		$("#tbList").datagrid("showColumn", "hadReceiveNums"); // 设置隐藏列
		$("#tbList").datagrid("showColumn", "receiveNums"); // 设置隐藏列
		$("#btnDiv").hide();
	}else if(flag==3){
		$("#tbList").datagrid("showColumn", "hadDispatchNums"); // 设置隐藏列
		$("#tbList").datagrid("showColumn", "dispatchNums"); // 设置隐藏列
		$("#btnCheck").hide();
	}else{
		$("#tbList").datagrid("showColumn", "oper"); 
		$("#btnCheck").show();
	}
	
}
/**
 * 保存菜单
 */
function formSubmit() {
	if (!$('#ff').form('validate')) {
		showMsg("表单校验不通过");
		return;
	}
	endEdit();
	var attrList = [];
	var row = $("#tbList").datagrid('getRows');
	for (var i = 0; i < row.length; i++) {
		var count = row[i].nums + row[i].sendNums;
		if (row[i].goodNo != "" && count > 0) {
			var items = row[i];
			attrList.push(items);
		}
	}
	if (attrList.length < 1) {
		showMsg("至少添加一个商品！");
		return;
	}

	var reqUrl = appInfo.formStatus == "new" ? appInfo.saveUrl
			: appInfo.updateUrl;
	if("receive" == appInfo.formStatus){
		reqUrl=appInfo.receiveUrl;
	}
	if("send" == appInfo.formStatus){
		reqUrl=appInfo.sendUrl;
	}
	var parms = $("#ff").serialize();
	parms += "&formStatus=" + appInfo.formStatus;
	parms += "&ywCode=2";
	parms += "&goodData=" + JSON.stringify(attrList);
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
			showMsg("保存成功");
		} else {
			showMsg(data.retmsg);
		}
	});
}
function endEdit() {
	var rows = $("#tbList").datagrid('getRows');
	for (var i = 0; i < rows.length; i++) {
		$("#tbList").datagrid('endEdit', i);
	}
}

function delRow(idx) {
	$('#tbList').datagrid('deleteRow', idx);
}
function addRow(){
	getGoodInfo(0, "","1");
}
function setEditing(rowIndex) {
	var editors = $('#tbList').datagrid('getEditors', rowIndex);
	console.log(editors);
	var goodCodeEditor = editors[0];
	var numsEditor = editors[1];
	var costPriceEditor = editors[3];
	var amtEditor = editors[4];
	var salePriceEditor = editors[5];
	var saleAmtEditor = editors[6];
	goodCodeEditor.target.bind('change', function() {
		var goodCode = goodCodeEditor.target.val()
		getGoodInfo(rowIndex, goodCode,"0");
	});
	numsEditor.target.bind('change', function() {
		calculateNums();
	});
	
	function calculateNums() {
		var amt = numsEditor.target.val() * costPriceEditor.target.val();
		var saleAmt = numsEditor.target.val() * salePriceEditor.target.val();
		$(amtEditor.target).numberbox('setValue', amt);
		$(saleAmtEditor.target).numberbox('setValue', saleAmt);
	}
}
function getGoodInfo(rowIndex, goodCode,edit) {
	var orgId ="";
	if ("1" != edit) {
		orgId = $("#sendOrgId").combobox("getValue");
		if (orgId == null || orgId == '') {
			showMsg("请先选择发货门店");
			return;
		}
	}
	$.ajax({
		url : appInfo.getGoodUrl,
		type : "get",
		data : {
			"goodNo" : goodCode,
			"edit":edit,
			"orgId":orgId
		},
		dataType : "json",
		success : function(data, status, xhr) {
			if (data.retcode == '0') {
				var rowData = data.info;
				if("1"==edit){
					$('#tbList').datagrid('appendRow',rowData);
				}else{
					$('#tbList').datagrid('updateRow', {
						index : rowIndex,
						row : rowData
					});
				}
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
function loadList() {
	$('#tbList')
			.datagrid(
					{
						url : appInfo.listUrl,
						// toolbar : "#tb",
						idField : 'orderGoodId',
						height : 350,
						rownumbers : true,
						columns : [ [
								{
									title : '单号',
									field : 'orderGoodId',
									width : 100,
									align : "center",
									hidden : true

								// },
								// {
								// title : '',
								// field : 'cks',
								// width : 100,
								// align : "center",
								// checkbox : true

								},
								{
									title : '操作',
									field : 'oper',
									width : 100,
									align : "center",
									formatter : function(value, row, index) {
										return "<a href='#' onclick=\"javascript:addRow()\">添加</a>&nbsp;&nbsp;<a href='#' onclick=\"javascript:delRow("
										+ index + ")\">删除</a>";
									}
								}, {

									title : '编号',
									field : 'goodNo',
									width : 100,
									align : "center",
									editor : "validatebox"
								}, {
									title : '条码',
									field : 'barNo',
									width : 100,
									align : "center"
								}, {
									title : '品名',
									field : 'goodName',
									width : 100,
									align : "center"
								}, {
									title : '单位',
									field : 'unit',
									width : 100,
									align : "center"
								}, {
									title : '规格',
									field : 'rank',
									width : 100,
									align : "center"
								}, {
									title : '型号',
									field : 'model',
									width : 100,
									align : "center"
								}, {
									title : '品牌',
									field : 'brandName',
									width : 100,
									align : "center"
								}, {
									title : '库存数量',
									field : 'stockNums',
									width : 100,
									align : "center"
								}, {
									title : '数量',
									field : 'nums',
									width : 100,
									align : "center",
									editor : "validatebox"
								}, {
									title : '赠送数量',
									field : 'sendNums',
									width : 100,
									align : "center",
									editor : {
										type : 'numberbox',
										options : {
											precision : 0
										}
									}
								
								}, {
									title : '配送价',
									field : 'dispatchPrice',
									width : 100,
									align : "center",
									editor : {
										type : 'numberbox',
										options : {
											precision : 2
										}
									}
								}, {
									title : '金额',
									field : 'amt',
									width : 100,
									align : "center",
									editor : {
										type : 'numberbox',
										options : {
											precision : 2
										}
									}
								}, {
									title : '零售价',
									field : 'salePrice',
									width : 100,
									align : "center",
									editor : {
										type : 'numberbox',
										options : {
											precision : 2
										}
									}
								}, {
									title : '零售金额',
									field : 'saleAmt',
									width : 100,
									align : "center",
									editor : {
										type : 'numberbox',
										options : {
											precision : 2
										}
									}
								}, {
									title : '已配送数量',
									field : 'hadDispatchNums',
									width : 100,
									align : "center",
								}, {
									title : '配送数量',
									field : 'dispatchNums',
									width : 100,
									align : "center",
									editor : {
										type : 'numberbox',
										options : {
											precision : 0
										}
									}
								}, {
									title : '已收数量',
									field : 'hadReceiveNums',
									width : 100,
									align : "center",
								}, {
									title : '收货数量',
									field : 'receiveNums',
									width : 100,
									align : "center",
									editor : {
										type : 'numberbox',
										options : {
											precision : 0
										}
									}
								
								} ] ],
						loader : function(params, success, loadError) {
							var that = $(this);
							loader(that, params, success, loadError);
						},
						onClickRow : function(rowIndex, rowData) {
							$("#tbList").datagrid("beginEdit", rowIndex);
							setEditing(rowIndex);
						},
						onClickCell : function(rowIndex, field, value) {

						},
						onLoadSuccess : function(data) {
							$("#tbList").datagrid('unselectAll');
							if(appInfo.formStatus=='receive'){
								showOrHide(1);
							}else if(appInfo.formStatus=='check'){
								showOrHide(2);
							}else if(appInfo.formStatus=='send'){
								showOrHide(3);
							}else{
								showOrHide(0);
							}
						}
					});
	// 请求加载数据
	function loader(that, params, success, loadError) {
		var opts = that.datagrid("options");
		appInfo.requestParam.page_number = 1;
		appInfo.requestParam.page_size = 200;
		appInfo.requestParam.orderId = orderId;
		appInfo.requestParam.formStatus=appInfo.formStatus
		$.ajax({
			url : opts.url,
			type : "get",
			data : appInfo.requestParam,
			dataType : "json",
			success : function(data, status, xhr) {
				if (data.retcode == '0') {
					$('#ff').form('load', data.info);
					appInfo.typeCode=data.info.typeCode;
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
