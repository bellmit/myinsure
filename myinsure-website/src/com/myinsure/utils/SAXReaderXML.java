package com.myinsure.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 解析XML
 * @author Administrator
 *
 */
public class SAXReaderXML
{
    private SAXReader reader;
    private Document document;

    /**
     * 无参构造
     */
    public SAXReaderXML()
    {
	super();
    }

    /**
     * 构造方法
     * @param xmlfile 要解析的xml文件
     */
    public SAXReaderXML(File xmlfile)
    {
	reader = new SAXReader();
	try
	{
	    document = reader.read(xmlfile);
	} catch (DocumentException e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * 构造方法
     * @param xmlStr 要解析的xml字符串
     */
    public SAXReaderXML(String xmlStr)
    {
	reader = new SAXReader();
	try
	{
	    document = reader.read(new ByteArrayInputStream(xmlStr.getBytes("utf-8")));
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * 读取xml文件中根目录下的所有子节点
     * @param xmlfile
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Element> getChildren()
    {
	Element root = document.getRootElement();// 获取根元素
	List<Element> childElements = root.elements();// 获取当前元素下的全部子元素
	return childElements;
    }

    /**
     * 得到父节点下所有子节点的text值
     * @return
     */
    // public Map<String, String> getChildText(Element father){
    //
    // }

    public static void main(String [] args) throws Exception
    {
	File xmlfile = new File("D:/books.xml");
	String xmlStr = "<books>" + "<book>" + "<author>Thomas</author>" + "<title>Java从入门到放弃</title>" + "<publisher>UCCU</publisher>" + "</book>" + "<book>" + "<author>小白</author>"
	        + "<title>MySQL从删库到跑路</title>" + "<publisher>GoDie</publisher>" + "</book>" + "<book>" + "<author>PHPer</author>" + "<title>BestPHP</title>" + "<publisher>PHPchurch</publisher>"
	        + "</book>" + "</books>";
	SAXReaderXML saxReaderXML = new SAXReaderXML(xmlfile);
	List<Element> childElements = saxReaderXML.getChildren();
	Iterator<Element> iter = childElements.iterator();
	while (iter.hasNext())
	{
	    Element child = iter.next();

	}

	for (Element child : childElements)
	{// 循环输出全部book的相关信息
	    List<Element> books = child.elements();
	    for (Element book : books)
	    {
		String name = book.getName();// 获取当前元素名
		String text = book.getText();// 获取当前元素值
		System.out.println(name + ":" + text);
	    }
	}
    }

}
