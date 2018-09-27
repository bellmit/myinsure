package com.myinsure.admin.website.webmap;

import java.util.Iterator;
import java.util.List;

import com.jfinal.log.Log;

public class WebMapingService
{
	@SuppressWarnings("unused")
	private static final Log log = Log.getLog(WebMapingService.class);

	private static final WebMapingService webMapingService = new WebMapingService();

	public static WebMapingService me()
	{
		return webMapingService;
	}

	/**
	 * 建立文章——栏目的关系
	 * 
	 * @param articleKey
	 * @param columnId
	 * @return
	 */
	public boolean saveMappingColumn(String articleKey, String columnId) throws Exception
	{
		WebMaping webMaping = new WebMaping();
		webMaping.set("article_key", articleKey);
		webMaping.set("column_id", columnId);
		boolean b = webMaping.save();
		return b;
	}

	/**
	 * 建立文章——专题的关系
	 * 
	 * @param articleKey
	 * @param specialId
	 * @return
	 */
	public boolean saveMappingSpecial(String articleKey, String specialId) throws Exception
	{
		WebMaping webMaping = new WebMaping();
		webMaping.set("article_key", articleKey);
		webMaping.set("special_id", specialId);
		boolean b = webMaping.save();
		return b;
	}

	/**
	 * 查询所有与当前文章对应的栏目和专题
	 * 
	 * @param articleKey
	 * @return
	 */
	public List<WebMaping> getMappings(String articleKey) throws Exception
	{
		String sql = "select * from web_mapping where article_key = ?";
		List<WebMaping> list = WebMaping.dao.find(sql, articleKey);
		return list;
	}

	/**
	 * 删除文章-栏目-专题对应关系
	 * 
	 * @param list
	 * @return
	 */
	public boolean delMapping(List<WebMaping> list) throws Exception
	{
		Iterator<WebMaping> iter = list.iterator();
		try
		{
			while (iter.hasNext())
			{
				WebMaping maping = iter.next();
				WebMaping.dao.deleteById(maping.get("id"));
			}
			return true;
		} catch (Exception e)
		{
			return false;
		}
	}
}
