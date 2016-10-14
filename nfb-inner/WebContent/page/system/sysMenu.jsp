<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@taglib prefix="wt" uri="/taglib/common.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理平台</title>
<%@include file="../../include/common.jsp"%>
</head>
<body>
	<div id="mainPanel">
		<p class="nav-header-cls">系统菜单管理>>主页</p>
		<div style="padding: 5px 0;">
			<wt:button />
		</div>
		<table id="tbList">
		</table>
	</div>
	<!-- 新增和编辑窗口 -->
	<div id="editWin" class="easyui-window"
		data-options="modal:true,closed:true,
		minimizable:false,
		maximizable:false,
		draggable:true,
		collapsible:false"
		style="width: 400px; height: 300px; padding: 10px;">
		<form id="ff" name="ff" method="post">
			<table class="out-win-cls">
				<tr>
					<th>菜单名称</th>
					<td><input id="menuitemPut" name="menuitemPut"
						class="easyui-validatebox" data-options="required:true"
						maxlength="60"></td>
				</tr>
				<tr>
					<th>父菜单</th>
					<td><input id="parentidPut" name="parentidPut"
						data-options="required:true"></td>
				</tr>
				<tr>
					<th>链接URL</th>
					<td><input id="urlPut" name="urlPut"
						class="easyui-validatebox" data-options="required:true" value="#">
					</td>
				</tr>
				<tr>
					<th>排序</th>
					<td><input id="sortPut" name="sortPut" type="text" class="easyui-numberbox" value="150"
						data-options="min:0,precision:0"></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center"><input
						type="button" id="btnOK" name="btnOK" value=" 保 存 " />
						&nbsp;&nbsp;&nbsp; <input type="button" id="btnCancel"
						name="btnCancel" value=" 取 消 " /></td>
				</tr>
			</table>
		</form>
	</div>


</body>
<script type="text/javascript"
	src="sysMenu.js?gt=<%=new Date().getTime()%>"></script>
</html>