package com.marks.smart.system.autocode.core.produced;

import com.marks.smart.system.autocode.core.produced.pojo.AutoBean;
import com.marks.smart.system.autocode.core.produced.pojo.OutFileContent;
import com.marks.smart.system.autocode.core.produced.util.StringUtil;

public abstract class AbstractProduced implements ModuleProduced {

	protected OutFileContent outFileContent = new OutFileContent();
	
	
	public OutFileContent getOutFileContent() {
		return outFileContent;
	}

	//文件后缀
	private String fileNameAfter;
	
	public abstract String  getFileNameAfter();

	/**
	 * 获取实体bean的包路径
	 */
	public String producedBeanPackageUrl(AutoBean autoBean) {
		return StringUtil.StringJoin(autoBean.getDefaultPackageUrl(), DOT_VALUE, autoBean.getDefaultPojo());
	}
	
//	public abstract String producedPackageUrl(AutoBean autoBean);
	
}
