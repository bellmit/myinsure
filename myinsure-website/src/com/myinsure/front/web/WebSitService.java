package com.myinsure.front.web;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.myinsure.admin.website.article.Article;
import com.myinsure.admin.website.carousel.WebImage;
import com.myinsure.admin.website.company.WebCompany;
import com.myinsure.admin.website.web.WebColumn;
import com.myinsure.admin.website.web.WebConfig;
import com.myinsure.admin.website.web.WebConfigService;
import com.myinsure.front.web.article.WebArticleService;
import com.myinsure.front.web.carousel.WebImageService;
import com.myinsure.front.web.column.WebFrontColumnService;
import com.myinsure.front.web.company.WebCompanyService;
import com.myinsure.front.web.html.HtmlParse;

public class WebSitService
{
	@SuppressWarnings("unused")
	private static final Log log = Log.getLog(WebSitService.class);
	private static final WebSitService webSitService = new WebSitService();
	public static WebSitService me()
	{
		return webSitService;
	}
	/**
	 * 根据字符串生成html文件
	 * @param htmlStr
	 * @param filePath
	 */
	public boolean outHtml(String htmlStr,String filePath){
		try
		{
			File filedir = new File(filePath.substring(0, filePath.lastIndexOf("/")));
			if(!filedir.exists()){
				filedir.mkdirs();
			}
			String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
			File file = new File(filedir,filename);
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			byte[] bytes = htmlStr.getBytes("utf-8");
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 模版填充公用方法
	 * @param t
	 * @param filePath
	 * {"type":"column","quantity":1,"name":"","param":"companynews"}
	 */
	public Template commonService(GroupTemplate gt,String filePath){
		List<WebColumn> columns = WebFrontColumnService.me().getColumns();//导航栏
		List<WebConfig> webConfig = WebConfigService.me().getWebConfig();//底部数据
		String rgex = "\\$\\[(.*)\\]";
		String htmlStr = HtmlParse.htmlToString(filePath);
    	List<JSONObject> tags = HtmlParse.getTags(htmlStr, rgex);
    	htmlStr = htmlStr.replaceAll(rgex,"");//将获取到的字符串替换为空串
    	Template t = gt.getTemplate(htmlStr);
		t.binding("columns", columns);//绑定导航条数据
		for (WebConfig webConfigs : webConfig)
		{
			t.binding(webConfigs.getStr("name"), webConfigs.getStr("value"));//绑定底部数据
		}
		for (JSONObject tag : tags)//根据标签查询对应数据，并将数据绑定到template上，从而填充到模版中
		{
			String type = tag.getString("type");
			Integer quantity = tag.getInteger("quantity");
			String name = tag.getString("name");
			String param = tag.getString("param");
			if(type.equalsIgnoreCase("column")){
				WebColumn column = WebFrontColumnService.me().getColumnByName(param);
				t.binding(name, column);
			} else if(type.equalsIgnoreCase("img")){
				List<WebImage> list = WebImageService.me().getImages(param, quantity);
				t.binding(name, list);
			}else if(type.equalsIgnoreCase("news")){
				List<Article> list = WebArticleService.me().findByColumn(param, quantity);
				t.binding(name, list);
			}else if(type.equalsIgnoreCase("columns")){
				WebColumn column = WebFrontColumnService.me().findSonColumnsByColumn(param, quantity);
				t.binding(name, column);
			}else if(type.equalsIgnoreCase("companies")){
				List<WebCompany> companies = WebCompanyService.me().getCompanies();//合作保险公司
				t.binding(name, companies);
			}
		}
		return t;
	}
	/**
	 * 通用填充方法
	 * @return
	 */
	public Template generateHtml(GroupTemplate gt,String sourceHtml){
		String filePath = PathKit.getWebRootPath() + "/templete/" + sourceHtml + ".html";
		Template t = commonService(gt,filePath);
		return t;
	}
}
