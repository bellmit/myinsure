package com.myinsure.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadTxtToExcel
{

    /**
     * Read txt
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public static List<String> readTxt(File file) throws Exception
    {
	List<String> txtValueList = new ArrayList<String>();
	String encoding = "UTF-8";
	if (file.exists() && file.canRead() && (file.getName().lastIndexOf(".txt") < 0))
	{
	    InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
	    BufferedReader buffer = new BufferedReader(read);
	    String lineTxt = null;
	    while ((lineTxt = buffer.readLine()) != null)
	    {
		txtValueList.add(lineTxt);
	    }
	    if (read != null)
	    {
		read.close();
	    }
	}
	return txtValueList;
    }

    /**
     * 功能：Java读取txt文件的内容 步骤：1：先获得文件句柄 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流 4：一行一行的输出。readline()。 备注：需要考虑的是异常情况
     * 
     * @param filePath
     */
    public static void readTxtFile(String filePath)
    {
	try
	{
	    String encoding = "GBK";
	    File file = new File(filePath);
	    if (file.isFile() && file.exists())
	    { // 判断文件是否存在
		InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null)
		{
		    System.out.println(lineTxt);
		}
		read.close();
	    } else
	    {
		System.out.println("找不到指定的文件");
	    }
	} catch (Exception e)
	{
	    System.out.println("读取文件内容出错");
	    e.printStackTrace();
	}

    }

    public static void readTxtFileLine(String filePath)
    {
	try
	{
	    String encoding = "GBK";
	    File file = new File(filePath);
	    if (file.isFile() && file.exists())
	    { // 判断文件是否存在
		InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null)
		{
		    String [] sr = lineTxt.split(",");
		    for (int i = 0; i < sr.length; i++)
		    {
			System.out.println(sr[i]);
		    }

		}
		read.close();
	    } else
	    {
		System.out.println("找不到指定的文件");
	    }
	} catch (Exception e)
	{
	    System.out.println("读取文件内容出错");
	    e.printStackTrace();
	}

    }

    /*
     * public static void wirteExcel() {
     * 
     * try { //打开文件 WritableWorkbook book= Workbook.createWorkbook(new
     * File("E:\\goods.xls")); //生成名为“第一页”的工作表，参数0表示这是第一页 WritableSheet
     * sheet=book.createSheet("第一页",0); //在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
     * //以及单元格内容为test
     * 
     * Label label=new Label(0,0,"测试"); //将定义好的单元格添加到工作表中 sheet.addCell(label);
     * jxl.write.Number number = new jxl.write.Number(1,0,789.123);
     * sheet.addCell(number); jxl.write.Label s=new jxl.write.Label(1, 2,
     * "三十三"); sheet.addCell(s); //写入数据并关闭文件 book.write(); book.close();
     * //最好在finally中关闭，此处仅作为示例不太规范 } catch(Exception e) { System.out.println(e);
     * } }
     */

    public static void main(String argv[])
    {
	String filePath = "E:\\goods.txt";
	// "res/";
	readTxtFileLine(filePath);
	// wirteExcel();
    }
}
