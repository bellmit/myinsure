package com.myinsure.utils;

import com.jfinal.config.Constants;
import com.jfinal.core.JFinal;
import com.jfinal.log.Log;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{
    private static final Log log = Log.getLog(StringUtils.class);

    public static String urlDecode(String string)
    {
	try
	{
	    return URLDecoder.decode(string, JFinal.me().getConstants().getEncoding());
	} catch (UnsupportedEncodingException e)
	{
	    log.error("urlDecode is error", e);
	}
	return string;
    }

    public static String urlEncode(String string)
    {
	try
	{
	    return URLEncoder.encode(string, JFinal.me().getConstants().getEncoding());
	} catch (UnsupportedEncodingException e)
	{
	    log.error("urlEncode is error", e);
	}
	return string;
    }

    public static String urlRedirect(String redirect)
    {
	try
	{
	    redirect = new String(redirect.getBytes(JFinal.me().getConstants().getEncoding()), "ISO8859_1");
	} catch (UnsupportedEncodingException e)
	{
	    log.error("urlRedirect is error", e);
	}
	return redirect;
    }

    public static boolean areNotEmpty(String [] strings)
    {
	if ((strings == null) || (strings.length == 0))
	{
	    return false;
	}
	for (String string : strings)
	{
	    if ((string == null) || ("".equals(string)))
	    {
		return false;
	    }
	}
	return true;
    }

    public static boolean isNotEmpty(String string)
    {
	return (string != null) && (!string.equals(""));
    }

    public static boolean areNotBlank(String [] strings)
    {
	if ((strings == null) || (strings.length == 0))
	{
	    return false;
	}
	for (String string : strings)
	{
	    if ((string == null) || ("".equals(string.trim())))
	    {
		return false;
	    }
	}
	return true;
    }

    public static boolean isNotBlank(String string)
    {
	return (string != null) && (!string.trim().equals(""));
    }

    public static boolean isBlank(String string)
    {
	return (string == null) || (string.trim().equals(""));
    }

    public static long toLong(String value, Long defaultValue)
    {
	try
	{
	    if ((value == null) || ("".equals(value.trim())))
		return defaultValue.longValue();
	    value = value.trim();
	    if ((value.startsWith("N")) || (value.startsWith("n")))
		return -Long.parseLong(value.substring(1));
	    return Long.parseLong(value);
	} catch (Exception e)
	{
	}
	return defaultValue.longValue();
    }

    public static int toInt(String value, int defaultValue)
    {
	try
	{
	    if ((value == null) || ("".equals(value.trim())))
		return defaultValue;
	    value = value.trim();
	    if ((value.startsWith("N")) || (value.startsWith("n")))
		return -Integer.parseInt(value.substring(1));
	    return Integer.parseInt(value);
	} catch (Exception e)
	{
	}
	return defaultValue;
    }

    public static BigInteger toBigInteger(String value, BigInteger defaultValue)
    {
	try
	{
	    if ((value == null) || ("".equals(value.trim())))
		return defaultValue;
	    value = value.trim();
	    if ((value.startsWith("N")) || (value.startsWith("n")))
		return new BigInteger(value).negate();
	    return new BigInteger(value);
	} catch (Exception e)
	{
	}
	return defaultValue;
    }

    public static boolean match(String string, String regex)
    {
	Pattern pattern = Pattern.compile(regex, 2);
	Matcher matcher = pattern.matcher(string);
	return matcher.matches();
    }

    public static boolean isNumeric(String str)
    {
	if (str == null)
	    return false;
	int i = str.length();
	while (true)
	{
	    i--;
	    if (i < 0)
		break;
	    int chr = str.charAt(i);
	    if ((chr < 48) || (chr > 57))
		return false;
	}
	return true;
    }

    /**
     * 搜索 XSS过滤
     * 
     * @param word
     * @return
     */
    public static String getSearchWord(String word)
    {
	word = word.trim();
	word = word.replace('<', ' ');
	word = word.replace('>', ' ');
	word = word.replace('"', ' ');
	word = word.replace('\'', ' ');
	word = word.replace('/', ' ');
	word = word.replace('%', ' ');
	word = word.replace(';', ' ');
	word = word.replace('(', ' ');
	word = word.replace(')', ' ');
	word = word.replace('&', ' ');
	word = word.replace('+', '_');
	return word;
    }

    /**
     * MessageFormat用来格式化一个消息
     * 
     * @param message
     * @param array
     * @return
     */
    public static String messageFormat(String message, String [] array)
    {
	MessageFormat messageFormat = new MessageFormat(message);
	String value = messageFormat.format(array);
	return value;
    }

    /**
     * 获取文件扩展名
     * 
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename)
    {
	if ((filename != null) && (filename.length() > 0))
	{
	    int dot = filename.lastIndexOf('.');
	    if ((dot > -1) && (dot < (filename.length() - 1)))
	    {
		return filename.substring(dot + 1);
	    }
	}
	return filename;
    }

    /**
     * MD5加密处理
     * 
     * @param s
     * @return
     */
    public final static String MD5(String s)
    {
	char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	try
	{
	    byte [] btInput = s.getBytes();
	    /** 获得MD5摘要算法的 MessageDigest 对象 **/
	    MessageDigest mdInst = MessageDigest.getInstance("MD5");
	    /** 使用指定的字节更新摘要 **/
	    mdInst.update(btInput);
	    /** 获得密文 **/
	    byte [] md = mdInst.digest();
	    /** 把密文转换成十六进制的字符串形式 **/
	    int j = md.length;
	    char str[] = new char[j * 2];
	    int k = 0;
	    for (int i = 0; i < j; i++)
	    {
		byte byte0 = md[i];
		str[k++] = hexDigits[byte0 >>> 4 & 0xf];
		str[k++] = hexDigits[byte0 & 0xf];
	    }
	    return new String(str);
	} catch (Exception e)
	{
	    e.printStackTrace();
	    return null;
	}
    }

}