var appInfo = {
	listUrl : top.window.urlBase + '/goodInfo/list.do',// 获取商品管理列表接口 GoodInfo
	saveUrl : top.window.urlBase + '/goodInfo/save.do',// 保存新增商品管理接口
	updateUrl : top.window.urlBase + '/goodInfo/update.do',// 编辑商品管理信息接口
	deleteUrl : top.window.urlBase + '/goodInfo/delete.do',// 删除商品管理接口
	selectedId : -1,
	selectedData : {},
	requestParam : {
		page_number : 1,
		page_size : 10,
		keyword : ""
	},
	formStatus : "new"
};

$(function() {
	// 加载列表
	loadList();

	// 搜索
	$("#doSearch").on("click", function(e) {
		app.myreload("#tbList");
		appInfo.selectedData = {};
		appInfo.selectedId = -1;
	});

	// 新增
	$("#add").on("click", function() {
		$("#remove").html("");
		$("#editWin").window({
			title : "新增"
		}).window("open");
		$('#ff').form('clear');
		appInfo.formStatus = "new";
		initForPic("");
	});

	// 编辑
	$("#edit").on("click", function() {
		if (isSelectedOne(appInfo.selectedId)) {
			$("#remove").html("");
			$("#editWin").window({
				title : "编辑"
			}).window("open");
			appInfo.formStatus = "edit";
			$('#ff').form('load', appInfo.selectedData);
			initForPic(appInfo.selectedData.imageUrl);
		}
	});

	// 删除
	$("#delete").on("click", function() {
		if (isSelectedOne(appInfo.selectedId)) {
			$.messager.confirm('Confirm', '确认要删除该记录吗?', function(r) {
				if (r) {
					var parms = "goodId=" + appInfo.selectedId;
					$.post(appInfo.deleteUrl, parms, function(data) {
						if (data.retcode == 0) {
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
	var path = $(".ke-img").attr("src"); // 图路径
	if (path == undefined) {
		top.G.alert("必须添加图片!");
		return false;
	}
	path = path.substr(path.indexOf("=") + 1);

	$("#imageUrl").val(path);
	var reqUrl = appInfo.formStatus == "new" ? appInfo.saveUrl
			: appInfo.updateUrl;
	$('#ff').form('submit', {
		url : reqUrl,
		onSubmit : function(param) {
			param.formStatus = appInfo.formStatus;
		},
		success : function(data) {
			if (typeof data === 'string') {
				try {
					data = $.parseJSON(data);
				} catch (e0) {
					showMsg("json 格式 错误");
					return;
				}
			}
			if (data.retcode == 0) {
				$("#editWin").window("close");
				app.myreload("#tbList");
				appInfo.selectedData = {};
				appInfo.selectedId = -1;
				showMsg("保存成功");
			} else {
				showMsg(data.retmsg);
			}
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
		pagination : true,
		idField : 'goodId',
		pagination : true,
		pageNumber : appInfo.requestParam.page_number,
		pageSize : appInfo.requestParam.page_size,
		singleSelect : true,
		columns : [ [ {
			title : '商品ID',
			field : 'goodId',
			width : 100,
			align : "center"
		}, {
			title : '商品名称',
			field : 'goodName',
			width : 100,
			align : "center"
		}, {
			title : '商品单价',
			field : 'goodPrice',
			width : 100,
			align : "center"
		}, {
			title : '商品单位',
			field : 'unit',
			width : 100,
			align : "center"
		}, {
			title : '商品主图',
			field : 'imageUrl',
			width : 100,
			align : "center"
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
			appInfo.selectedId = rowData.goodId;
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
		$.ajax({
			url : opts.url,
			type : "get",
			data : appInfo.requestParam,
			dataType : "json",
			success : function(data, status, xhr) {
				checkLogin(data);
				if (data.retcode == 0) {
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

function initForPic(main) {
	var mainPic = $("#mainPic").html();
	$("#remove").append(mainPic);
	var flag = false;
	if (main != null && main != "") {
		flag = true;
	}
	initForModPic("buttonPlaceholder", "photoContainer", "errorMsg1", 2, flag,
			[ main ]);

}
function initForModPic(buttonPlaceholder, photoContainer, errorMsg, i, flag,
		edit_pic) {
	var options = {
		flash_url : top.window.urlBase + "/js/uploadImage/swfupload.swf",
		upload_url : top.window.urlBase + "/upload/image.do", // 处理上传的servlet
		file_size_limit : "1 MB",// 文件的大小 注意: 中间要有空格
		types : "*.jpg;*.jpeg;*.gif", // 注意是 " ; " 分割
		typesdesc : "web iamge file", // 这里可以自定义
		file_upload_limit : i,
		button_image_url : top.window.urlBase + "/images/add.png",// 必传
		button_placeholder_id : buttonPlaceholder,// 必传
		custom_settings : {
			photoContainer_Id : photoContainer, // 图片的容器id
			btnUpload_ID : "", // 上传按钮
			upload_type : "1",
			errorMsg_Id : errorMsg, // 错误信息
			errorMsg_fadeOutTime : 2000, // 错误信息谈出的时间
			width : 160,// 图片显示宽度
			height : 100, // 图片显示高度
			edit : flag,
			edit_pic : edit_pic
		},
		post_params : {
			"uploadType" : "1"
		}
	// 针对一个页面需要多个上传控件上传不同类别的图片
	};
	initSwfupload(options);
}