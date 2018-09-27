package com.myinsure.admin.website.web;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jfinal.log.Log;

public class WebColumnService
{
	@SuppressWarnings("unused")
	private static final Log log = Log.getLog(WebColumnService.class);

	private static final WebColumnService webColumnService = new WebColumnService();

	public static WebColumnService me()
	{
		return webColumnService;
	}

	/**
	 * 查询栏目信息
	 * 
	 * @return
	 */
	public List<WebColumn> getWebColumn()
	{
		String sql = "select * from web_column where isshow=1 order by orders asc";
		List<WebColumn> webColumns = WebColumn.dao.find(sql);
		return webColumns;
	}
	/**
	 * 查询所有一级栏目及其二级栏目
	 * @return
	 */
	public List<WebColumn> getWebColumns()
	{
		String sql = "select * from web_column where isshow=1 and wap_nav is null order by orders asc";
		List<WebColumn> webColumns = WebColumn.dao.find(sql);
		digui(webColumns);
		return webColumns;
	}
	/**
	 * 查询所有一级栏目
	 * @return
	 */
	public List<WebColumn> getFirstWebColumns(){
		String sql = "select * from web_column where isshow=1 and wap_nav is null order by orders asc";
		List<WebColumn> webColumns = WebColumn.dao.find(sql);
		return webColumns;
	}
	/**
	 * 根据一级栏目id查询二级栏目
	 * @param columnId
	 * @return
	 */
	public List<WebColumn> getWebColumnsByParent(String columnId){
		String sql = "select * from web_column where isshow=1 and wap_nav = ? order by orders asc";
		List<WebColumn> webColumns = WebColumn.dao.find(sql,columnId);
		return webColumns;
		
	}

	// 递归查询导航的子导航
	private void digui(List<WebColumn> webColumns)
	{
		String sql = "select * from web_column where isshow=1 and wap_nav = ? order by orders asc";

		Iterator<WebColumn> iter = webColumns.iterator();
		while (iter.hasNext())
		{
			WebColumn webColumn = iter.next();
			List<WebColumn> columns = WebColumn.dao.find(sql, webColumn.getStr("id"));
			webColumn.setWebColumns(columns);
			digui(columns);
		}
	}

	/**
	 * 修改栏目
	 * 
	 * @param params
	 * @return
	 */
	public boolean updateColumn(Map<String, String> map)
	{
		WebColumn webColumn = WebColumn.dao.findById(map.get("id"));
		webColumn.set("name", map.get("name"));
		webColumn.set("orders", map.get("orders"));
		webColumn.set("nav", map.get("nav"));
		webColumn.set("faclass", map.get("faclass"));
		webColumn.set("foldername", map.get("foldername"));
		String outUrl = "";
		if("简单列表".equalsIgnoreCase(map.get("faclass")) || "带图列表".equalsIgnoreCase(map.get("faclass"))){
			outUrl = "front/getNewses?columnId=" + map.get("id");
		}else{//首页模版和文章模版
			outUrl = map.get("foldername") + ".html";
		}
		webColumn.set("out_url", outUrl);
		return webColumn.update();
	}
	/**
	 * 根据id查询导航
	 * @param id
	 * @return
	 */
	public WebColumn getById(String id){
		WebColumn webColumn = WebColumn.dao.findById(id);
		return webColumn;
	}

	/**
	 * 保存新栏目
	 * @param params
	 * @return
	 */
	public boolean saveColumn(Map<String, String> map)
	{
		WebColumn webColumn = new WebColumn();
		webColumn.set("id", map.get("id"));
		webColumn.set("name", map.get("name"));
		webColumn.set("orders", map.get("orders"));
		webColumn.set("nav", map.get("nav"));
		webColumn.set("faclass", map.get("faclass"));
		webColumn.set("foldername", map.get("foldername"));
		webColumn.set("wap_nav", map.get("wapNav"));
		String outUrl = "";
		if("简单列表".equalsIgnoreCase(map.get("faclass")) || "带图列表".equalsIgnoreCase(map.get("faclass"))){
			outUrl = "front/getNewses?columnId=" + map.get("id");
		}else{//首页模版和文章模版
			outUrl = map.get("foldername") + ".html";
		}
		webColumn.set("out_url", outUrl);
		webColumn.set("isshow", "1");
		return webColumn.save();
	}
	/**
	 * 保存导航的图片
	 * @param columnId
	 * @param src
	 * @return
	 */
	public boolean saveColumnImg(String columnId,String src){
		WebColumn webColumn = WebColumn.dao.findById(columnId);
		webColumn.set("img_src", src);
		boolean b = webColumn.update();
		return b;
	}
	/**
	 * 删除栏目信息
	 * 
	 * @return
	 */
	public boolean columnDel(String id)
	{
		return WebColumn.dao.deleteById(id);
	}

	// 更新web_column表，用以改变所属栏目
	public boolean updateColumn(String parentid, String childid)
	{
		String sql = "select * from web_column where id = ?";
		WebColumn webColumn = WebColumn.dao.findFirst(sql, childid);
		webColumn.set("wap_nav", parentid);
		boolean b = webColumn.update();
		return b;
	}
	/**
	 * 根据新闻key查找新闻所属的导航栏目
	 * @param articleId
	 * @return
	 */
	public List<WebColumn> getByArticleKey(String articleKey){
		String sql = " select wc.* from web_column wc "
				   + " left join web_mapping wm on wc.id = wm.column_id "
				   + " where wm.article_key = " + articleKey;
		List<WebColumn> list = WebColumn.dao.find(sql);
		Iterator<WebColumn> iter = list.iterator();
		WebConfig sysDomain = WebConfigService.me().getWebConfigByName("sys_domain");
		WebConfig webDomain = WebConfigService.me().getWebConfigByName("web_domain");
		while(iter.hasNext()){
			WebColumn column2 = iter.next();
			if("简单列表".equalsIgnoreCase(column2.getStr("faclass")) || "带图列表".equalsIgnoreCase(column2.getStr("faclass"))){
				column2.set("out_url", sysDomain.getStr("value") + "/" + column2.getStr("out_url"));
			}else{
				column2.set("out_url", webDomain.getStr("value") + "/" + column2.getStr("out_url"));
			}
		}
		return list;
	}
}













