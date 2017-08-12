<%@page language="Java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@taglib prefix="wt" uri="/taglib/common.tld"%>
<!DOCTYPE html>
<!-- WxMenu.html -->
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/wxMenu.css" />

<%@include file="../../include/common.jsp"%>
</head>

<body>

	<div id="mainPanel">
		<p class="nav-header-cls">
			微信菜单管理(<span style="color: red;">一级菜单3个菜单有效，二级菜单5个有效,新建二级菜单后一级菜单访问内容自动无效,另双击可编辑</span>)
		</p>
		<div id="tb" style="padding: 5px 0;">
			<table>
				<tr>
					<td><a id="addAcc" href="javascript:void(0)"
						class="easyui-linkbutton menuBtnCls"
						data-options="iconCls:'icon-search'" onclick="addAcc()">添加菜单</a> <wt:button /></td>
				</tr>
			</table>
		</div>
		<table id="tbList">
		</table>
	</div>

	<div id="accWin" class="easyui-window"
		data-options="modal:true,closed:true,
		minimizable:false,
		maximizable:false,
		draggable:true,
		collapsible:false"
		style="width: 500px; height: 300px; padding: 10px;">
		<div align="center" style="width: 100%;">
			<form id="accff" name="accff" method="post">
			  <input type="hidden" name="id">
				<table class="out-win-cls">
					<tr>
					
						<th>菜单类型</th>
						<td><input id="menutype" class="easyui-combobox"
							name="menutype" style="width: 200px;"
							data-options="required:true,valueField: 'label',
		textField: 'value',
		data: [{
			label: '0',
			value: '通用菜单'
		},{
			label: '1',
			value: '个性化菜单'
		
		}],onSelect:selectMenuType" />

						</td>
					</tr>
					<tr>
						<th>菜单名称</th>
						<td><input name="name" class="easyui-validatebox"
							data-options="required:true" style="width: 200px;"></td>
					</tr>
					<tr>
						<th>公众号</th>
						<td><input id="accountid" name="accountid"
							class="easyui-combobox"
							data-options="required:true,valueField:'accountId',textField:'accountname',url:'<%=request.getContextPath()%>/inner/wxAccount/combox.do',onSelect:reloadtagList"
							style="width: 200px;"></td>
					</tr>
					<tr id="tagidTr" style="display: none;">
						<th>标签</th>
						<td><input id="tagid" name="tagid" class="easyui-combobox"
							data-options="valueField:'tagid',textField:'name'"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center"><input
							type="button" id="btnOKAcc" name="btnOKAcc" value=" 保 存 " />
							&nbsp;&nbsp;&nbsp; <input type="button" id="btnCancelAcc"
							name="btnCancelAcc" value=" 取 消 " /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<div id="editWin" class="easyui-window"
		data-options="modal:true,closed:true,
		minimizable:false,
		maximizable:false,
		draggable:true,
		collapsible:false"
		style="width: 500px; height: 300px; padding: 10px;">
		<div align="center" style="width: 100%;">
			<form id="ff" name="ff" method="post">
				<input type="hidden" id="id" name="id"> <input type="hidden"
					id="accountid" name="accountid"> <input type="hidden"
					id="lvl" name="lvl">
				<table class="out-win-cls">
					<tr style="display: none;">
						<th>父ID</th>
						<td><input id="parent_id" name="parent_id"></td>
					</tr>
					<tr>
						<th>菜单名称</th>
						<td><input id="name" name="name" class="easyui-validatebox"
							data-options="required:true" style="width: 400px;"></td>
					</tr>
					<tr>
						<th>菜单类型</th>
						<td><label><input name="type" type="radio"
								value="view" checked="checked" />链接 </label> <label><input
								name="type" type="radio" value="click" />click </label> <label><input
								name="type" type="radio" value="1" />无</label></td>
					</tr>
					<tr>
						<th>访问内容</th>
						<td><input id="content" name="content"
							class="easyui-validatebox" data-options="required:true"
							style="width: 400px;"></td>
					</tr>
					<tr>
						<th>排序</th>
						<td><input id="sort" name="sort" class="easyui-numberbox"
							data-options="required:true" style="width: 400px;"></td>
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
	</div>

</body>
<script src="js/wxMenu.js"></script>
</html>
