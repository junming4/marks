/**
 * File Name: cluster.scheme.autocode.webpage.html.htmlpage.HtmlPageProduced.java
 * 类简述
 * @author:hgwei1@grgbanking.com
 * @Date:2016年6月6日下午4:30:56
 * @see (optional) 
 * @Copyright (c) 2016, 广电运通信息科技有限公司  All Rights Reserved.
 */
package com.marks.smart.system.autocode.core.produced.webpage.html.htmlpage;

import java.util.ArrayList;
import java.util.List;

import com.marks.smart.system.autocode.core.produced.pojo.AttrType;
import com.marks.smart.system.autocode.core.produced.pojo.AutoAttr;
import com.marks.smart.system.autocode.core.produced.pojo.AutoBean;
import com.marks.smart.system.autocode.core.produced.util.StringUtil;
import com.marks.smart.system.autocode.core.produced.webpage.html.AbstractHtmlProduced;

/**
 * File Name:
 * cluster.scheme.autocode.webpage.html.htmlpage.HtmlPageProduced.java
 * 
 * @author:lffei1@grgbanking.com
 * @Date:2016年6月6日下午4:30:56
 * @see (optional)
 * @Copyright (c) 2016, 广电运通信息科技有限公司 All Rights Reserved.
 */
public class HtmlPageProduced extends AbstractHtmlProduced {

	/**
	 * 
	 * @see cluster.scheme.autocode.AbstractProduced#getFileNameAfter()
	 */
	@Override
	public String getFileNameAfter() {
		return "";
	}

	/**
	 * root
	 */
	@Override
	public String getFileSrc(AutoBean autoBean) {
		return autoBean.getFactBeanName().toLowerCase();
	}

	/**
	 * 获取css路径
	 */
	public String producedGetCssRoot(AutoBean autoBean) {
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append(DEFAULT_CSS).append(BACK_SLANT).append(StringUtil.getLowerCaseChar(autoBean.getFactBeanName()));
		sBuffer.append(DOT_VALUE).append(DEFAULT_CSS);

		return sBuffer.toString();
	}
	
	/**
	 * 描述字段
	 */
	public String producedDescAttrs(AutoBean autoBean) {
		StringBuffer sBuffer = new StringBuffer();
		List<AutoAttr> autoAttrs = autoBean.getAutoAttrs();
		for (int i = 0; i < autoAttrs.size(); i++) {
			if("YES".equals(autoAttrs.get(i).getIsQuery())){
				sBuffer.append("/"+autoAttrs.get(i).getAttrDesc());
			}
		}
		return sBuffer.toString().substring(1);
	}

	/**
	 * 获取js路径
	 */
	public String producedGetJsRoot(AutoBean autoBean) {
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append(DEFAULT_JS).append(BACK_SLANT).append(StringUtil.getLowerCaseChar(autoBean.getFactBeanName()))
				.append(DOT_VALUE).append(DEFAULT_JS);

		return sBuffer.toString();
	}

	// caption
	public String producedGetCaption(AutoBean autoBean) {
		return autoBean.getModuleDesc();
	}

	public String producedGetInputField(AutoBean autoBean) {
		StringBuffer sBuffer = new StringBuffer();
		List<AutoAttr> attrs = autoBean.getAutoAttrs();
		for (int i = 0; i < attrs.size(); i++) {
			 if(attrs.get(i).isPK()){
				 continue;
			 }
			 if(createTime.equals(attrs.get(i).getAttrName().toLowerCase()) || updateTime.equals(attrs.get(i).getAttrName().toLowerCase())){
				 continue;
			 }
			String attrName = attrs.get(i).getAttrName();
			String desc = attrs.get(i).getAttrDesc();
			
			String at=attrs.get(i).getAttrType().getMybatisType();
			String type="validatebox";
			String length="maxlength=\""+attrs.get(i).getAttrSize()+"\"";
			if(AttrType.Integer.getMybatisType().equals(at)){
				type="numberbox";
				length="";
			}else if(AttrType.Date.getMybatisType().equals(at)){
				type="datetimebox";
				length="";
			}
			
			String str = "<tr><th style=\"width: 120px;text-align: right;\">" + desc + "&nbsp;&nbsp;:&nbsp;&nbsp;</th><td><input id=\"" + attrName + "\" name=\"" + attrName
					+ "\" class=\"easyui-"+type+"\" data-options=\"required:true\" style=\"width: 200px;\" "+length+"></td></tr>";
			
			sBuffer.append(str);
		}
		return sBuffer.toString();
	}

	 //获取主键
    public String producedPKAttrValue(AutoBean autoBean){
        List<AutoAttr> autoAttrs = autoBean.getAutoAttrs(); 
        for(AutoAttr attr : autoAttrs){
            if(attr.isPK()){
                return String.valueOf(attr.getAttrName() );
               
            }
        }       
        return "";
    }
    
	public static void main(String[] args) {
		AutoBean autoBean = new AutoBean();
		autoBean.setTableName("goodInfo");
		List<AutoAttr> autoAttrs = new ArrayList<AutoAttr>();
		AutoAttr autoAttr = new AutoAttr();
		autoAttr.setAttrName("goodId");
		autoAttr.setAttrDesc("商品ID");
		autoAttr.setAttrType(AttrType.String);
		autoAttr.setPK(true);
		autoAttrs.add(autoAttr);
		AbstractHtmlProduced html = new HtmlPageProduced();
		System.out.println(html.producedBeanName(autoBean));
		// html.setFileSrc(autoBean);
		// html.producedBeanName(autoBean);
		System.out.println(html.getFileSrc(autoBean));
	}
}
