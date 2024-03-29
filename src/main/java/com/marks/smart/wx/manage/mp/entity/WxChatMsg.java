package com.marks.smart.wx.manage.mp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WxChatMsg implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
    *ID
    */
    private String id;
   
    /**
    *类型
    */
    private int c_type;
    private String username;
    /**
    *回复者ID
    */
    private String userid;
    /**
    *内容
    */
    private String c_content;
    /**
    *创建时间
    */
    private Date createtime;
    /**
    *回复类型
    */
    private String c_replayType;
    /**
    *回话ID
    */
    private String session_id;
    /**
    *openid
    */
    private String openid;
    /**
    *公众号ID
    */
    private String accountid;
    
    private int is_replay;

    private List<WxChatMsg> replayList=new ArrayList<WxChatMsg>();
    
	private String companyId;
    
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public int getIs_replay() {
		return is_replay;
	}
	public void setIs_replay(int is_replay) {
		this.is_replay = is_replay;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

  
    public int getC_type(){
        return c_type;
    }
    public void setC_type(int c_type){
        this.c_type = c_type;
    }

    public String getUserid(){
        return userid;
    }
    public void setUserid(String userid){
        this.userid = userid;
    }

    public String getC_content(){
        return c_content;
    }
    public void setC_content(String c_content){
        this.c_content = c_content;
    }

    public Date getCreatetime(){
        return createtime;
    }
    public void setCreatetime(Date createtime){
        this.createtime = createtime;
    }

    public String getC_replayType(){
        return c_replayType;
    }
    public void setC_replayType(String c_replayType){
        this.c_replayType = c_replayType;
    }

    public String getSession_id(){
        return session_id;
    }
    public void setSession_id(String session_id){
        this.session_id = session_id;
    }

    public String getOpenid(){
        return openid;
    }
    public void setOpenid(String openid){
        this.openid = openid;
    }

    public String getAccountid(){
        return accountid;
    }
    public void setAccountid(String accountid){
        this.accountid = accountid;
    }

	
	
	public List<WxChatMsg> getReplayList() {
		return replayList;
	}
	public void setReplayList(List<WxChatMsg> replayList) {
		this.replayList = replayList;
	}
	public String toLog(){
		return " - id:" +String.valueOf(id)+" - c_type:" +String.valueOf(c_type)+" - userid:" +String.valueOf(userid)+" - c_content:" +String.valueOf(c_content)+" - createtime:" +String.valueOf(createtime)+" - c_replayType:" +String.valueOf(c_replayType)+" - session_id:" +String.valueOf(session_id)+" - openid:" +String.valueOf(openid)+" - accountid:" +String.valueOf(accountid);
	}
}