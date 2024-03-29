package com.marks.smart.system.system.syslog.pojo;

import com.marks.common.util.IDUtil;
import com.marks.common.util.date.DateUtil;

public class SysLog {

	private String id;
	private String userid;
	private String username;
	private String retain1;//保留字段1
	private String retain2;//保留字段2
	private String retain3;//保留字段2
	private int source;//来源0:内管，1消息中心 2：前端
	private String createtime;// 创建时间
	private String menuname;//菜单名称
	private String opername;//操作名称
	private String ip;
	
	private String url;
	private String menuid;// 菜单ID
	
	
	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public SysLog(){
		id=IDUtil.getUUID();
		createtime = DateUtil.getCurrDateStr();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRetain1() {
		return retain1;
	}
	public void setRetain1(String retain1) {
		this.retain1 = retain1;
	}
	public String getRetain2() {
		return retain2;
	}
	public void setRetain2(String retain2) {
		this.retain2 = retain2;
	}
	
	public String getRetain3() {
		return retain3;
	}
	public void setRetain3(String retain3) {
		this.retain3 = retain3;
	}
	public String getMenuname() {
		return menuname;
	}
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
	public String getOpername() {
		return opername;
	}
	public void setOpername(String opername) {
		this.opername = opername;
	}
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SysLog)
			return id.equals(((SysLog) obj).id);
		else
			return false;
	}
}
