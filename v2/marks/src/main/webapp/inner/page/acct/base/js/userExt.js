var appInfo = {
	listUrl: top.window.urlBase + '/inner/userExt/list.do',//获取用户扩展表列表接口  UserExt
	saveUrl: top.window.urlBase + '/inner/userExt/save.do',//保存新增用户扩展表接口
	updateUrl: top.window.urlBase + '/inner/userExt/update.do',//编辑用户扩展表信息接口
	deleteUrl: top.window.urlBase + '/inner/userExt/delete.do',//删除用户扩展表接口
	selectedId : -1,
	selectedData : {},
	requestParam:{
		page_number : 1,
		page_size : 10,
		keyword : ""
	},
	formStatus:"new"
 };
 //-----------------权限控制功能 start---------------
	//新增
	function add(){
		$("#editWin").window({
			title : "新增"
		}).window("open");
		$('#ff').form('clear');
		appInfo.formStatus="new";
	}
	
	
	//编辑
	function edit(){
		if (!isSelectedOne(appInfo.selectedId)) {
			return;
		}
		$("#editWin").window({
				title : "编辑"
			}).window("open");
			appInfo.formStatus="edit";
			$('#ff').form('load',appInfo.selectedData);
	}
		//删除
	function del(){
		if (!isSelectedOne(appInfo.selectedId)) {
			return;
		}
		
			$.messager.confirm('确认', '确认要删除该记录吗?', function(r) {
				if (r) {
					var parms = "userid=" + appInfo.selectedId;
					$.post(appInfo.deleteUrl, parms, function(data) {
						if (data.retcode == '0') {
							app.myreload("#tbList");
							appInfo.selectedData = {};
							appInfo.selectedId=-1;
							showMsg("删除成功");
						} else {
							showMsg(data.retmsg);
						}
					});
				}
			});
		
	}
 //-----------------权限控制功能 end---------------
$(function(){
//加载列表
 	loadList();

//搜索
	$("#doSearch").on("click", function(e) {
		app.myreload("#tbList");
		appInfo.selectedData = {};
		appInfo.selectedId=-1;
	});
	
		//保存菜单
	$("#btnOK").on("click",function(){
		formSubmit();
	});
	$("#btnCancel").on("click",function(){
		$("#editWin").window("close");
	});
});
/**
 * 保存菜单
 */
function formSubmit(){
	if (!$('#ff').form('validate')) {
		showMsg("表单校验不通过");
		return;
	}
	var reqUrl=appInfo.formStatus=="new"?appInfo.saveUrl:appInfo.updateUrl;
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
//		toolbar : "#tb",
		striped:true,
		nowrap:true,
		animate : true,
		collapsible : true,
		fitColumns : true,
		autoRowHeight:false,
		idField : 'userid',
		height:520,
		rownumbers : true,
		pagination : true,
		pageNumber : appInfo.requestParam.page_number,
		pageSize : appInfo.requestParam.page_size,
		singleSelect : true,
		columns : [ [                 {title:'系统编号',field:'userid',width:100,align:"center",hidden:true },
                {title:'用户编号',field:'userCode',width:100,align:"center"},
                {title:'用户姓名',field:'userName',width:100,align:"center"},
                {title:'公司编号',field:'companyId',width:100,align:"center"},
                {title:'等级编号',field:'lvlId',width:100,align:"center"},
                {title:'等级名称',field:'lvlName',width:100,align:"center"},
                {title:'首次操作时间',field:'first_operate_time',width:100,align:"center"},
                {title:'最后操作时间',field:'last_operate_time',width:100,align:"center"},
                {title:'首次消费时间',field:'first_consume_time',width:100,align:"center"},
                {title:'最后消费时间',field:'last_consume_time',width:100,align:"center"},
                {title:'累计积分',field:'grand_total_point',width:100,align:"center"},
                {title:'累计消费金额',field:'grand_total_consume_amt',width:100,align:"center"},
                {title:'累计消费次数',field:'grand_total_consume_nums',width:100,align:"center"},
                {title:'可用积分',field:'balPoint',width:100,align:"center"},
                {title:'冻结积分',field:'lockPoint',width:100,align:"center"},
                {title:'总积分',field:'totalPoint',width:100,align:"center"},
                {title:'首次积分时间',field:'first_point_time',width:100,align:"center"},
                {title:'最后积分时间',field:'last_point_time',width:100,align:"center"},
                {title:'更新时间',field:'updatetime',width:100,align:"center"},
                {title:'余额',field:'balAmt',width:100,align:"center"},
                {title:'冻结金额',field:'lockAmt',width:100,align:"center"},
                {title:'总额',field:'totalAmt',width:100,align:"center"},
                {title:'首次充值时间',field:'first_recharge_time',width:100,align:"center"},
                {title:'最后充值时间',field:'last_recharge_time',width:100,align:"center"} ] ],
		loader : function(params, success, loadError) {
			var that = $(this);
			loader(that, params, success, loadError);
		},
		onClickRow : function(rowIndex,rowData) {
			appInfo.selectedId = rowData.userid;
			appInfo.selectedData = rowData;
		},
		onDblClickRow : function(rowIndex, rowData) {
			appInfo.selectedId = rowData.userid;
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
		appInfo.requestParam.keyword=$("#keyword").val();
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
		
