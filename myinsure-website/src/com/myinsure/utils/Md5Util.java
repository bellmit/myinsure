package com.myinsure.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class Md5Util
{

    /**
     * md5加密处理
     * 
     * @param value
     * @return
     */
    public static String md5Value(String value)
    {
	try
	{
	    MessageDigest digest = MessageDigest.getInstance("md5");
	    byte result[] = digest.digest(value.getBytes());
	    BASE64Encoder encoder = new BASE64Encoder();
	    return encoder.encode(result);
	} catch (NoSuchAlgorithmException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return "";
    }
}
