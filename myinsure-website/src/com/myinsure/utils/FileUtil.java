package com.myinsure.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 文件对象处理 FileUtil
 * 
 * @author sxf
 * 
 *         2013-5-23上午11:25:00
 * 
 */

public class FileUtil
{
    public static boolean exists(String fileName)
    {
	if ((fileName == null) || (fileName.length() == 0))
	{
	    return false;
	}

	fileName = fileName.replace('\\', '/');

	File file = new File(fileName);

	return file.exists();
    }

    public static boolean saveFile(String fileName, String content)
    {
	return saveFile(fileName, content, "GBK");
    }

    public static boolean saveFileUtf8(String fileName, String content)
    {
	return saveFile(fileName, content, "UTF-8");
    }

    public static boolean saveFile(String fileName, String content, String charset)
    {
	if ((fileName == null) || (fileName.length() == 0))
	    return false;
	if (content == null)
	    return false;
	if ((charset == null) || (charset.length() == 0))
	{
	    charset = Charset.defaultCharset().name();
	}

	fileName = fileName.replace('\\', '/');

	System.out.println("FileUtil Class filename = " + fileName);

	createPath(fileName.substring(0, fileName.lastIndexOf('/')));

	File file = null;
	FileOutputStream out = null;
	try
	{
	    file = new File(fileName);

	    if (file.exists())
		file.delete();
	    else
	    {
		file.createNewFile();
	    }

	    out = new FileOutputStream(file);

	    if (charset.equalsIgnoreCase("UTF-8"))
	    {
		out.write(new byte[] { -17, -69, -65 });
	    }
	    out.write(content.getBytes(charset));
	} catch (FileNotFoundException e)
	{
	    e.printStackTrace();
	} catch (IOException e)
	{
	    e.printStackTrace();
	} finally
	{
	    if (out != null)
	    {
		try
		{
		    out.flush();
		    out.close();
		} catch (IOException e)
		{
		    e.printStackTrace();
		    return false;
		}
	    }
	}

	return true;
    }

    public static String readFile(String fileName)
    {
	return readFile(fileName, "");
    }

    public static String readFileUtf8(String fileName)
    {
	return readFile(fileName, "UTF-8");
    }

    public static String readFile(String fileName, String charset)
    {
	String ret = "";
	if ((fileName == null) || (fileName.length() == 0))
	    return "";
	if ((charset == null) || (charset.length() == 0))
	{
	    charset = Charset.defaultCharset().name();
	}

	byte [] b = fileToBytes(fileName);
	if ((b == null) || (b.length == 0))
	    return ret;

	try
	{
	    if ((charset.equalsIgnoreCase("UTF-8")) && (b.length > 3) && (b[0] == -17) && (b[1] == -69) && (b[2] == -65))
	    {
		ret = new String(b, 3, b.length - 3, charset);
		return ret;
	    }

	    ret = new String(b, 0, b.length, charset);
	} catch (UnsupportedEncodingException e)
	{
	    e.printStackTrace();
	}

	return ret;
    }

    public static byte [] fileToBytes(String fileName)
    {
	FileInputStream ins = null;
	ByteArrayOutputStream bos = null;
	try
	{
	    ins = new FileInputStream(new File(fileName));

	    bos = new ByteArrayOutputStream();

	    int len = 0;
	    byte [] buf = new byte[256];
	    while ((len = ins.read(buf, 0, 256)) > -1)
	    {
		bos.write(buf, 0, len);
	    }

	    byte [] arrayOfByte1 = bos.toByteArray();
	    return arrayOfByte1;
	} catch (Exception e)
	{
	    e.printStackTrace();
	} finally
	{
	    try
	    {
		if (ins != null)
		{
		    ins.close();
		    ins = null;
		}
		if (bos != null)
		{
		    bos.close();
		    bos = null;
		}
	    } catch (Exception e)
	    {
		e.printStackTrace();
	    }
	}

	return null;
    }

    /**
     * 创建类绝对路径
     * 
     * @param filePath
     * @return
     */
    public static boolean createPath(String filePath)
    {
	if ((filePath == null) || (filePath.length() == 0))
	{
	    return false;
	}

	filePath = filePath.replace('\\', '/');

	String [] paths = filePath.split("/");

	StringBuilder sbpath = new StringBuilder();
	int i = 0;
	for (int n = paths.length; i < n; i++)
	{
	    sbpath.append(paths[i]);

	    // 创建文件
	    File ftmp = new File(sbpath.toString());
	    if (!ftmp.exists())
	    {
		ftmp.mkdir();
	    }

	    sbpath.append("/");
	}

	return true;
    }

    public static File getValidFile(File file)
    {
	if (file == null)
	    return file;

	String orgName = file.getName();
	String [] orgNames = orgName.split("\\.");

	String filePath = file.getParent();
	filePath = filePath.replace('\\', '/') + "/";

	int i = 1;
	while (file.exists())
	{
	    i++;
	    String fileName = filePath + orgNames[0] + "(" + i + ")";
	    if (orgNames.length > 1)
	    {
		fileName = fileName + orgName.substring(orgName.indexOf("."));
	    }

	    file = new File(fileName);
	}

	return file;
    }

    public static File getValidFile(String fileName)
    {
	File file = new File(fileName);
	return getValidFile(file);
    }

    public static String getFileName(String path)
    {
	if ((path == null) || (path.length() == 0))
	    return "";

	path = path.replaceAll("\\\\", "/");
	int last = path.lastIndexOf("/");

	if (last >= 0)
	{
	    return path.substring(last + 1);
	}
	return path;
    }

    public static String getClassPath()
    {
	return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }
}