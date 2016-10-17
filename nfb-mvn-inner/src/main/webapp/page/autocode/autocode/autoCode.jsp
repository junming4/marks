<%@page language="Java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@taglib prefix="wt" uri="/taglib/common.tld"%>
<!DOCTYPE html>
<!-- AutoCode.html -->
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/autoCode.css" />

<%@include file="../../include/common.jsp"%>
</head>

<body>

	<div id="mainPanel">
		<p class="nav-header-cls">菜单管理>>生产代码</p>
		<div id="tb" style="padding: 5px 0;">
			<table>
				<tr>
					<td><input type="text" id="keyword" name="keyword"
						style="width: 260px;" placeholder="关键字" /></td>
					<td><button type="button" id="doSearch" data-oper="query"
							style="cursor: pointer;">查询</button></td>
				</tr>
				<tr>
					<td colspan="7"><wt:button />&nbsp;&nbsp;&nbsp; <input
						type="button" id="autoCodeBtn" name="autoCodeBtn" value=" 自动生成代码  " /></td>
				</tr>
			</table>
		</div>
		<table id="tbList">
		</table>
	</div>

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
					<th>表名称</th>
					<td><input id="tableName" name="tableName"
						class="easyui-validatebox"></td>
				</tr>
				<tr>
					<th>实体类名称</th>
					<td><input id="beanName" name="beanName"
						class="easyui-validatebox"></td>
				</tr>
				<tr>
					<th>描述</th>
					<td><input id="moduleDesc" name="moduleDesc"
						class="easyui-validatebox"></td>
				</tr>
				<tr>
					<th>字段</th>
					<td><a href="javascript:;" id="editAttr">编辑字段</a></td>
				</tr>
				<tr>
					<th>是否生成表</th>
					<td><select id="is_createtable" class="easyui-combobox" name="is_createtable"
						style="width: 200px;">
							<option value="0">不生成</option>
							<option value="1">生成</option>
					</select></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center"><input
						type="button" id="btnOK" name="btnOK" value=" 保 存 " />
						&nbsp;&nbsp;&nbsp; <input type="button" id="btnCancel"
						name="btnCancel" value=" 取 消 " /> </td>
				</tr>
			</table>
		</form>
	</div>

	<div id="attrWin" class="easyui-window"
		data-options="modal:true,closed:true,
		minimizable:false,
		maximizable:false,
		draggable:true,
		collapsible:false,inline:false"
		style="width: 800px; height: 500px; padding: 10px;">
		<table id="dg"></table>
	</div>

</body>
<script src="js/autoCode.js"></script>
</html>