package com.myinsure.utils;

import java.io.IOException;
import java.util.Properties;

public class ZaLocationRead
{
    static Properties props = new Properties();

    static
    {
	try
	{
	    props.load(ReadPropertity.class.getClassLoader().getResourceAsStream("zhongan.properties"));// load
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
