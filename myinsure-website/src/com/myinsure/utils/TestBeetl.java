package com.myinsure.utils;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

public class TestBeetl
{

    public static void main(String [] args) throws Exception
    {

	StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
	Configuration cfg = Configuration.defaultConfiguration();
	GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
	Template t = gt.getTemplate("hello,${name},${name2}");
	t.binding("name", "beetl");
	t.binding("name2", "beetl2");
	String str = t.render();
	System.out.println(str);
    }

}
