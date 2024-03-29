package com.marks.smart.count.note.plan.pojo;

import java.io.Serializable;

public class Plan implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
    *ID
    *
    */
    private String id;
    /**
    *计划
    *
    */
    private String planTxt;
    /**
    *完成情况
    *
    */
    private String completeTxt;
    /**
    *是否完成
    *
    */
    private String isComplete;
    /**
    *创建时间
    *
    */
	private String createtime;
    /**
    *更新时间
    *
    */
	private String updatetime;
    /**
    *创建者
    *
    */
    private String creator;
    /**
    *手机号
    *
    */
    private String mobile;



    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getPlanTxt(){
        return planTxt;
    }
    public void setPlanTxt(String planTxt){
        this.planTxt = planTxt;
    }

    public String getCompleteTxt(){
        return completeTxt;
    }
    public void setCompleteTxt(String completeTxt){
        this.completeTxt = completeTxt;
    }

    public String getIsComplete(){
        return isComplete;
    }
    public void setIsComplete(String isComplete){
        this.isComplete = isComplete;
    }

	public String getCreatetime() {
        return createtime;
    }

	public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

	public String getUpdatetime() {
        return updatetime;
    }

	public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCreator(){
        return creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public String getMobile(){
        return mobile;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }


	public String toLog(){
		return " - id:" +String.valueOf(id)+" - planTxt:" +String.valueOf(planTxt)+" - completeTxt:" +String.valueOf(completeTxt)+" - isComplete:" +String.valueOf(isComplete)+" - createtime:" +String.valueOf(createtime)+" - updatetime:" +String.valueOf(updatetime)+" - creator:" +String.valueOf(creator)+" - mobile:" +String.valueOf(mobile);
	}
}