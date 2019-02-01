package com.jfinalshop.controller.usercenter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
public  class FreemakTest {
	
	public void  freemake() throws IOException, TemplateException{
		Map<String, Object> map =new HashMap<>();
		map.put("name", "小猪佩奇");
		
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
		configuration.setDefaultEncoding("UTF-8");
		configuration.setDirectoryForTemplateLoading(new File("D:\\sts\\ses_web\\.git\\tst\\src\\com\\jfinalshop\\controller\\usercenter\\FreemakTest.java"));
		 Template template= configuration.getTemplate("NewFile.html");
		 template.process(map, new FileWriter("D:\\index.html"));
	}
}
