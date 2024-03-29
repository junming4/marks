
//base-config.js
window.nfb = {};
window.urlBase = "/i";
app = {};

app.getUrlParams = function(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

function setupAjax(){
	$.ajaxSetup({cache:false
		,timeout:30000
		,beforeSend:function(xhr,opts){
			if(opts.url.indexOf("_t=")>=0){
				return;
			}
			if(opts.url.indexOf("?")>=0){
				opts.url +="&_t="+(+(new Date()));		
			}else{
				opts.url +="?_t="+(+(new Date()));		
			}	
		}
		,complete:function(xhr){				
			if(xhr.statusText!=undefined && xhr.status==0 && xhr.statusText=="timeout"){
				$.messager.alert("超时","操作超时！请返回重试！","info",function(){xhr.abort();});
			}		
		}			
	});	
}
setupAjax();
$(document).ajaxComplete(function(event, xhr, settings) {	
  	var data;	  	
  	if (settings.dataType=="script" || settings.dataType=="html"){
  		return;
  	}
  	if(   xhr.responseText){
  		data = $.parseJSON(xhr.responseText);
  		if(data && data.retcode=="-1000"){
  			top.location.replace("/inner/login.html");
    		return false;
  		}
  	}
});
/**
 * 为grid添加自己重新加载方法,解决带条件查询的时候分页栏不能回到首页问题
 */
app.myreload = function(newposition) {
	// 显示第一页数据
	$(newposition).datagrid("options").pageNumber = 1;
	// 分页栏上跳转到第一页
	$(newposition).datagrid('getPager').pagination({
		pageNumber : 1
	});
	$(newposition).datagrid("reload");
	$(newposition).datagrid('unselectAll');
};

function addTab(title, url, oldtitleprefix) {
	var tabs = $("#mainregion").tabs("tabs");
	var choosedIdx = -1;
	for (var ti = 0; ti < tabs.length; ti++) {
		var pOpts = $($("#mainregion").tabs('getTab', ti)).panel('options');
		if (oldtitleprefix && pOpts.title != undefined) {
			if (pOpts.title.indexOf(oldtitleprefix) >= 0
					&& title.indexOf(oldtitleprefix) >= 0) {
				choosedIdx = ti;
				break;
			}
		} else {
			continue;
		}
	}
	$width = $('#mainregion').tabs('options').width;
	var content = "<iframe scrolling=\"auto\" frameborder=\"0\"  src=\"" + url
			+ "\" style=\"width:100%;height:99%;\"></iframe>";
	if (choosedIdx > -1) {
		var tab = $("#mainregion").tabs('getTab', choosedIdx);
		$('#mainregion').tabs('update', {
			tab : tab,
			options : {
				title : title,
				content : content
			}
		});
		$('#mainregion').tabs('select', title);
	} else {
		if ($('#mainregion').tabs('exists', title)) {
			$('#mainregion').tabs('select', title);
		} else {
			$('#mainregion').tabs('add', {
				title : title,
				content : content,
				closable : true,
				toolPosition : 'left',
				tools : [ {
					iconCls : 'icon-mini-refresh',
					handler : function() {
						refreshTab("");
					}
				} ]
			});
		}
	}
}

function closeTab(title) {
	var pp = $('#mainregion').tabs('getSelected');
	var index = $('#mainregion').tabs('getTabIndex', pp);
	var tab = $("#mainregion").tabs("getTab", index);
	$('#mainregion').tabs('close', index);
}

function refreshTab(title) {
	var pp = $('#mainregion').tabs('getSelected');
	var index = $('#mainregion').tabs('getTabIndex', pp);
	var tab = $("#mainregion").tabs("getTab", index);
	var ifr = tab.find("iframe").get(0);
	var href = ifr.contentDocument.location.href;
	href = href.replace("#", "");
	ifr.contentDocument.location.replace(href);
}

function formatDateToYMD(t) {
	var y = t.getFullYear();
	var m = t.getMonth() + 1;
	if (m < 10)
		m = '0' + m;
	var d = t.getDate();
	if (d < 10)
		d = '0' + d;
	return y + "-" + m + "-" + d;
}
function formatDateToYMDHMS(t) {
	var y = t.getFullYear();
	var m = t.getMonth() + 1;
	if (m < 10)
		m = '0' + m;
	var d = t.getDate();
	if (d < 10)
		d = '0' + d;
	var h = t.getHours();
	if (h < 10)
		h = '0' + h;
	var mi = t.getMinutes();
	if (mi < 10)
		mi = '0' + mi;
	var s = t.getSeconds();
	if (s < 10)
		s = '0' + s;
	return y + "-" + m + "-" + d + " " + h + ":" + mi + ":" + s;
}
// 日期加减
function addAndDeleteDate(date, days) {
	var d = date;
	d.setDate(d.getDate() + days);
	var m = d.getMonth() + 1;
	return d.getFullYear() + '-' + m + '-' + d.getDate();
}
// stringToTime 兼容所有浏览器
function stringToTime(str) {
	if (str == null)
		return false;
	var strArray = [];
	strArray = str.split('-');
	var date = new Date();
	date.setUTCFullYear(strArray[0], strArray[1] - 1, strArray[2]);
	date.setUTCHours(0, 0, 0, 0);
	return date;
}

// 金额(分)转化为(元),10000分->100.00元 @zqsheng
function fen2yuan(val) {
	var str = (val / 100).toFixed(2) + '';
	var intSum = str.substring(0, str.indexOf(".")).replace(
			/\B(?=(?:\d{3})+$)/g, ',');// 取到整数部分
	var dot = str.substring(str.length, str.indexOf("."))// 取到小数部分
	var ret = intSum + dot;
	return ret;
}
// 当返回为-1000时，提示去登陆
function checkLogin(data) {
	/*if (data.retcode == "-1000") {
		alert("未登录,请重新登录！")
		top.location.replace(window.urlBase + "/inner/login.html");
		return;
	}*/
}

// 选择一条记录判断方法
function isSelectedOne(idx) {
	if (idx == -1) {
		showMsg("请选择一条记录");
		return false;
	}
	return true;
}
function showMsg(message) {
	$.messager.show({
		title : '系统提示',
		msg : message,
		showType : 'show',
		style : {
			right : '',
			top : document.body.scrollTop + document.documentElement.scrollTop,
			bottom : ''
		}
	});
}

function myfarmat(date){  
    var y = date.getFullYear();  
    var m = date.getMonth()+1;  
    var d = date.getDate();  
    var h = date.getHours();  
    var min = date.getMinutes();
    var sec = date.getSeconds();
    return  y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(min<10?('0'+min):min)+':'+(sec<10?('0'+sec):sec);  
      
}  

$(window).on("resize",function(e){
	$("#tbList").datagrid("resize",{width:$(window).width()});//修复调整窗口时，内容表格不会随窗伸缩；
});


function del_html_tags(str, reallyDo, replaceWith) {
	var e = new RegExp(reallyDo, "g");
	words = str.replace(e, replaceWith);
	return words;
}

//动态加载js文件
function loadScript(url, callback) {
	var script = document.createElement("script");
	script.type = "text/javascript";
	if (typeof (callback) != "undefined") {
		if (script.readyState) {
			script.onreadystatechange = function() {
				if (script.readyState == "loaded"
						|| script.readyState == "complete") {
					script.onreadystatechange = null;
					callback();
				}
			};
		} else {
			script.onload = function() {
				callback();
			};
		}
	}
	script.src = url;
	document.body.appendChild(script);
}