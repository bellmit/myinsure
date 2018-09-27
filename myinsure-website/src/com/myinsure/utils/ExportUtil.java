package com.myinsure.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Model;

public class ExportUtil
{
    /**
     * 导出json文件
     * @param dataList 要导出到Excel的数据集合
     * @param response 响应对象
     * @return
     */
    public void jsonExport(List<? extends Model<?>> dataList, HttpServletResponse response)
    {
	try
	{
	    String name = String.valueOf(new Date().getTime());
	    name = name + ".json";
	    String fileName = new String(name.getBytes("GBK"), "utf-8");// 根据时间生成文件名

	    response.reset();
	    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);// 指定下载的文件名
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Pragma", "no-cache");
	    response.setHeader("Cache-Control", "no-cache");
	    response.setDateHeader("Expires", 0);
	    OutputStream output = response.getOutputStream();
	    BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);// 缓冲输出流

	    String json = dataList.toString();
	    bufferedOutPut.write(json.getBytes());

	    bufferedOutPut.flush();
	    bufferedOutPut.close();
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * 导出Excel方法
     * @param titleKey 字段与title对应关系，例如：{"id":"序列","money":"钱",...}
     * @param dataList 要导出到Excel的数据集合
     * @param response 响应对象
     * @return 导出成功返回true，否则返回false
     */
    public static void excelExport(JSONObject titleKey, List<? extends Model<?>> dataList, HttpServletResponse response)
    {
	try
	{
	    String name = String.valueOf(new Date().getTime());
	    name = name + ".xls";
	    String fileName = new String(name.getBytes("GBK"), "utf-8");// 根据时间生成文件名

	    response.reset();
	    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);// 指定下载的文件名
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Pragma", "no-cache");
	    response.setHeader("Cache-Control", "no-cache");
	    response.setDateHeader("Expires", 0);

	    OutputStream output = response.getOutputStream();
	    BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);// 缓冲输出流

	    // 创建工作薄
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    // 创建sheet页
	    HSSFSheet sheet = workbook.createSheet("sheet");

	    // 根据title长度循环设置每一列的宽高
	    int size = titleKey.size();
	    for (int i = 0; i < size; i++)
	    {
		sheet.setColumnWidth(i, 25 * 256);
	    }
	    // title样式设置对象
	    HSSFCellStyle titleStyle = workbook.createCellStyle();
	    // title字体设置对象
	    HSSFFont titleFont = workbook.createFont();
	    // title字体大小
	    titleFont.setFontHeightInPoints((short) 24);
	    // title字体类型
	    titleFont.setFontName("楷体");
	    // title字体粗体
	    titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

	    titleStyle.setFont(titleFont);
	    titleStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中

	    Set<String> key = titleKey.keySet();
	    Object [] keys = key.toArray();

	    HSSFRow titleRow = sheet.createRow(0);
	    HSSFCell titleCell = null;
	    // 添加表头
	    for (int i = 0; i < size; i++)
	    {
		titleCell = titleRow.createCell(i);
		titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置单元格数据类型
		titleCell.setCellStyle(titleStyle);// 设置单元格样式
		HSSFRichTextString titleText = new HSSFRichTextString(titleKey.get(keys[i]).toString());
		titleCell.setCellValue(titleText);
	    }

	    // content样式设置对象
	    HSSFCellStyle contentStyle = workbook.createCellStyle();
	    // content字体设置对象
	    HSSFFont contentFont = workbook.createFont();
	    // content字体大小
	    contentFont.setFontHeightInPoints((short) 16);
	    // content字体类型
	    contentFont.setFontName("楷体");
	    // content字体不加粗
	    contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);

	    contentStyle.setFont(contentFont);
	    contentStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中

	    int row = 1;
	    HSSFRow contentRow = null;
	    HSSFCell contentCell = null;
	    for (int i = 0; i < dataList.size(); i++)
	    {
		Model<?> model = dataList.get(i);
		contentRow = sheet.createRow(row);
		for (int j = 0; j < size; j++)
		{
		    contentCell = contentRow.createCell(j);
		    contentCell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置单元格数据类型
		    contentCell.setCellStyle(contentStyle);// 设置单元格样式
		    HSSFRichTextString contentText = new HSSFRichTextString(String.valueOf(model.get(keys[j].toString()) == null ? "" : model.get(keys[j].toString())));
		    contentCell.setCellValue(contentText);// 添加数据
		}
		row++;
	    }

	    File file = new File("D:/66666.xls");
	    OutputStream os = new FileOutputStream(file);
	    workbook.write(os);
	    os.flush();
	    os.close();

	    workbook.write(bufferedOutPut);
	    bufferedOutPut.flush();
	    bufferedOutPut.close();
	    output.close();
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * 导出Excel方法
     * @param titleKey 字段与title对应关系，例如：[{key:"columnName",value:"列名称"},{key:"columnName",value:"列名称"},...}
     * @param dataList 要导出到Excel的数据集合
     * @param response 响应对象
     * @return 导出成功返回true，否则返回false
     */
    public static void exportExcel(JSONArray titleKey, List<? extends Model<?>> dataList, HttpSession session, HttpServletResponse response)
    {
	try
	{
	    String name = String.valueOf(new Date().getTime());
	    name = name + ".xls";
	    String fileName = new String(name.getBytes("GBK"), "utf-8");// 根据时间生成文件名

	    response.reset();
	    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);// 指定下载的文件名
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Pragma", "no-cache");
	    response.setHeader("Cache-Control", "no-cache");
	    response.setDateHeader("Expires", 0);

	    OutputStream output = response.getOutputStream();
	    BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);// 缓冲输出流

	    // 创建工作薄
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    // 创建sheet页
	    HSSFSheet sheet = workbook.createSheet("sheet");

	    // 根据title长度循环设置每一列的宽高
	    int size = titleKey.size();
	    for (int i = 0; i < size; i++)
	    {
		sheet.setColumnWidth(i, 25 * 256);
	    }
	    // title样式设置对象
	    HSSFCellStyle titleStyle = workbook.createCellStyle();
	    // title字体设置对象
	    HSSFFont titleFont = workbook.createFont();
	    // title字体大小
	    titleFont.setFontHeightInPoints((short) 24);
	    // title字体类型
	    titleFont.setFontName("楷体");
	    // title字体粗体
	    titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

	    titleStyle.setFont(titleFont);
	    titleStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中

	    HSSFRow titleRow = sheet.createRow(0);
	    HSSFCell titleCell = null;
	    // 添加表头
	    for (int i = 0; i < size; i++)
	    {
		titleCell = titleRow.createCell(i);
		titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置单元格数据类型
		titleCell.setCellStyle(titleStyle);// 设置单元格样式
		HSSFRichTextString titleText = new HSSFRichTextString(titleKey.getJSONObject(i).getString("value"));
		titleCell.setCellValue(titleText);
	    }

	    // content样式设置对象
	    HSSFCellStyle contentStyle = workbook.createCellStyle();
	    // content字体设置对象
	    HSSFFont contentFont = workbook.createFont();
	    // content字体大小
	    contentFont.setFontHeightInPoints((short) 16);
	    // content字体类型
	    contentFont.setFontName("楷体");
	    // content字体不加粗
	    contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);

	    contentStyle.setFont(contentFont);
	    contentStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中

	    int row = 1;
	    HSSFRow contentRow = null;
	    HSSFCell contentCell = null;
	    if (dataList != null)
	    {
		for (int i = 0; i < dataList.size(); i++)
		{
		    Model<?> model = dataList.get(i);
		    contentRow = sheet.createRow(row);
		    for (int j = 0; j < size; j++)
		    {
			contentCell = contentRow.createCell(j);
			contentCell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置单元格数据类型
			contentCell.setCellStyle(contentStyle);// 设置单元格样式
			HSSFRichTextString contentText = new HSSFRichTextString(String.valueOf(model.get(titleKey.getJSONObject(j).getString("key")) == null ? "" : model.get(titleKey.getJSONObject(j)
			        .getString("key"))));
			contentCell.setCellValue(contentText);// 添加数据
		    }
		    row++;
		}
	    }
	    String realPath = session.getServletContext().getRealPath("download");
	    File file = new File(realPath + "/taizhang.xls");
	    OutputStream os = new FileOutputStream(file);
	    workbook.write(os);
	    os.flush();
	    os.close();

	    workbook.write(bufferedOutPut);
	    bufferedOutPut.flush();
	    bufferedOutPut.close();
	    output.close();
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * 查询要导出的对象集合，并调用导出方法
     * @param classname 类对象的完全限定名，即要导出的对象的完全限定名。例如：List<Person>,classname指的是Person的完全限定名
     * @param columns 字段与title对应关系，例如：[{key:"columnName",value:"列名称"},{key:"columnName",value:"列名称"},...}
     * @param from sql中from子句，包括from关键字
     * @param where sql中where子句，包括where关键字
     * @param session
     * @param response
     */
    public static void getExportInfo(String classname, JSONArray columns, String from, String where, HttpSession session, HttpServletResponse response)
    {
	StringBuilder sql = new StringBuilder("select ");
	for (int i = 0; i < columns.size(); i++)
	{
	    String column = columns.getJSONObject(i).getString("key");
	    if (i == columns.size() - 1)
	    {
		sql.append(column + " `" + column + "` ");
		continue;
	    }
	    sql.append(column + " `" + column + "`, ");
	}
	sql.append(from);
	sql.append(where);
	List<? extends Model<?>> list = null;
	try
	{
	    Class<?> clazz = Class.forName(classname);

	    if (Model.class.isAssignableFrom(clazz))
	    {
		Class<? extends Model<?>> obj = (Class<? extends Model<?>>) clazz;
		list = (List<? extends Model<?>>) obj.newInstance().find(sql.toString());
	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
	exportExcel(columns, list, session, response);
    }
}
