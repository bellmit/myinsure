package com.myinsure.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码生成类 Password
 * 
 * @author sxf
 * 
 *         2013-5-27上午08:23:37
 * 
 */
public class Password
{
    // MD5密码的密匙
    private static final String _key = "QIAOLING";
    // 自定义密码的密匙
    private static final byte [] _oneKey = { 12, 3, 2, 13, 6, 3, 2, 14, 9, 2, 8, 5 };

    // 生成密码值
    public static String md5(String paramString)
    {
	if ((paramString == null) || (paramString.length() == 0))
	    return "";
	byte [] arrayOfByte1 = paramString.getBytes();
	try
	{
	    MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
	    localMessageDigest.update(arrayOfByte1);
	    // MD5 的计算结果是一个 128 位的长整数
	    byte [] arrayOfByte2 = localMessageDigest.digest("QIAOLING".getBytes());
	    return byte2hex(arrayOfByte2);
	} catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
	{
	    localNoSuchAlgorithmException.printStackTrace();
	}
	return "";
    }

    // 自定义加密字符串
    public static String encrypt(String paramString)
    {
	if ((paramString == null) || (paramString.length() == 0))
	    return "";
	byte [] arrayOfByte = paramString.getBytes();
	StringBuilder localStringBuilder = new StringBuilder();
	int i = 0;
	int j = arrayOfByte.length;
	while (i < j)
	{
	    int k = arrayOfByte[i];
	    if (i < _oneKey.length)
		k += _oneKey[i];
	    int m = k & 0xFF;
	    if (m < 16)
		localStringBuilder.append("0");
	    localStringBuilder.append(Integer.toHexString(m));
	    i++;
	}
	return localStringBuilder.toString().toUpperCase();
    }

    // 自定义解密字符串
    public static String decrypt(String paramString)
    {
	if ((paramString == null) || (paramString.length() == 0))
	    return "";
	char [] arrayOfChar = paramString.toCharArray();
	StringBuilder localStringBuilder1 = new StringBuilder();
	int i = 0;
	int j = arrayOfChar.length / 2;
	while (i < j)
	{
	    StringBuilder localStringBuilder2 = new StringBuilder();
	    localStringBuilder2.append(arrayOfChar[(i * 2)]);
	    localStringBuilder2.append(arrayOfChar[(i * 2 + 1)]);
	    int k = Integer.parseInt(localStringBuilder2.toString(), 16);
	    if (i < _oneKey.length)
		k -= _oneKey[i];
	    localStringBuilder1.append((char) k);
	    i++;
	}
	return localStringBuilder1.toString();
    }

    public static String byte2hex(byte [] paramArrayOfByte)
    {
	if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0))
	    return "";
	StringBuilder localStringBuilder = new StringBuilder();
	for (int i = 0; i < paramArrayOfByte.length; i++)
	{
	    int j = paramArrayOfByte[i] & 0xFF;
	    if (j < 16)
		localStringBuilder.append("0");
	    localStringBuilder.append(Integer.toHexString(j));
	}
	return localStringBuilder.toString().toUpperCase();
    }

    public static String decodeNum(String paramString)
    {
	if (paramString == null)
	    return "";
	paramString = decrypt(paramString);
	if (paramString.length() < "QIAOLING".length())
	    return "";
	return paramString.substring("QIAOLING".length());
    }

    public static String encodeNum(String paramString)
    {
	if (paramString == null)
	    return "";
	paramString = "QIAOLING" + paramString;
	return encrypt(paramString);
    }
}