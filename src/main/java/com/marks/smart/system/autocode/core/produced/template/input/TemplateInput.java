package com.marks.smart.system.autocode.core.produced.template.input;

import java.io.File;

import com.marks.smart.system.autocode.core.produced.template.TemplateStream;

/**
 * 文件读入
 * @author ykai5
 *
 */
public interface TemplateInput extends TemplateStream{

	public StringBuffer readTemplate(File templateFile);
	
}
