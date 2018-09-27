package com.myinsure.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 * Servlet implementation class UploadAction
 */
public class UploadImagesUtil
{

    public String uploadImages(HttpServletRequest request, HttpServletResponse response) throws Exception
    {

	// 文件保存目录路径
	String savePath = "";

	String path = "";

	// 临时文件目录
	// String tempPath = this.getServletContext().getRealPath("/") +
	// dirTemp;
	String tempPath = this.getClass().getClassLoader().getResource("/").getPath();
	String realPath = tempPath.substring(0, tempPath.indexOf("/WEB-INF"));
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	String ymd = "upload/" + sdf.format(new Date());
	savePath = realPath + "/" + ymd;
	// 创建文件夹
	File dirFile = new File(savePath);
	if (!dirFile.exists())
	{
	    dirFile.mkdirs();
	}

	tempPath += "/" + ymd;
	// 创建临时文件夹
	File dirTempFile = new File(tempPath);
	if (!dirTempFile.exists())
	{
	    dirTempFile.mkdirs();
	}

	DiskFileItemFactory factory = new DiskFileItemFactory();
	factory.setSizeThreshold(20 * 1024 * 1024); // 设定使用内存超过5M时，将产生临时文件并存储于临时目录中。
	factory.setRepository(new File(tempPath)); // 设定存储临时文件的目录。
	ServletFileUpload upload = new ServletFileUpload(factory);
	upload.setHeaderEncoding("UTF-8");
	try
	{
	    List items = upload.parseRequest(request);
	    // System.out.println("items = " + items);
	    Iterator itr = items.iterator();

	    while (itr.hasNext())
	    {
		FileItem item = (FileItem) itr.next();
		String fileName = item.getName();
		long fileSize = item.getSize();
		if (!item.isFormField())
		{
		    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		    String newFileName = df.format(new Date()) + "_" + new Random().nextInt(10000) + "." + fileExt;
		    try
		    {
			Thread.sleep(300);
			File uploadedFile = new File(savePath, newFileName);
			// 第二种方法
			OutputStream os = new FileOutputStream(uploadedFile);
			InputStream is = item.getInputStream();
			byte buf[] = new byte[1024];// 可以修改 1024 以提高读取速度
			int length = 0;
			while ((length = is.read(buf)) > 0)
			{
			    os.write(buf, 0, length);
			}
			// 关闭流
			os.flush();
			os.close();
			is.close();
			System.out.println("上传成功！路径：" + savePath + "/" + newFileName);
			path = ymd + "/" + newFileName;
		    } catch (Exception e)
		    {
			e.printStackTrace();
			return null;
		    }
		} else
		{
		    String filedName = item.getFieldName();
		    System.out.println("===============");
		    System.out.println("FieldName：" + filedName);
		    System.out.println("String：" + item.getString());
		}
	    }

	} catch (Exception e)
	{
	    e.printStackTrace();
	    return null;
	}
	return path;
    }

}
