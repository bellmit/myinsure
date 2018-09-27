package com.myinsure.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件
 * 
 * @author sxf 2014-7-8
 * 
 */
public class ReadPropertity
{

    static Properties props = new Properties();

    static
    {
	try
	{
	    props.load(ReadPropertity.class.getClassLoader().getResourceAsStream("config.properties"));// load
		                                                                                       // InputStream
	} catch (IOException e1)
	{
	    e1.printStackTrace();
	}
    }

    public static String getProperty(String key)
    {
	return props.getProperty(key); // get key value
    }
}
