package com.myinsure.utils;

import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AttachmentUtils
{
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    static List<String> imageSuffix = new ArrayList();

    public static String moveFile(UploadFile uploadFile)
    {
	if (uploadFile == null)
	{
	    return null;
	}
	File file = uploadFile.getFile();
	if (!file.exists())
	{
	    return null;
	}

	String webRoot = PathKit.getWebRootPath();

	String uuid = UUID.randomUUID().toString().replace("-", "");

	StringBuilder newFileName = new StringBuilder(webRoot).append(File.separator).append("attachment").append(File.separator).append(dateFormat.format(new Date())).append(File.separator)
	        .append(uuid).append(FileUtils.getSuffix(file.getName()));

	File newfile = new File(newFileName.toString());

	if (!newfile.getParentFile().exists())
	{
	    newfile.getParentFile().mkdirs();
	}

	file.renameTo(newfile);

	return FileUtils.removePrefix(newfile.getAbsolutePath(), webRoot);
    }

    public static boolean isImage(String path)
    {
	String sufffix = FileUtils.getSuffix(path);
	if (StringUtils.isNotBlank(sufffix))
	    return imageSuffix.contains(sufffix.toLowerCase());
	return false;
    }

    public static void main(String [] args)
    {
	System.out.println(FileUtils.getSuffix("xxx.jpg"));
    }

    static
    {
	imageSuffix.add(".jpg");
	imageSuffix.add(".jpeg");
	imageSuffix.add(".png");
	imageSuffix.add(".bmp");
	imageSuffix.add(".gif");
    }
}