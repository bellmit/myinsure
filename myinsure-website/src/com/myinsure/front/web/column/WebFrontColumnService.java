package com.myinsure.front.web.column;

import java.util.Iterator;
import java.util.List;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.myinsure.admin.website.web.WebColumn;
import com.myinsure.admin.website.web.WebConfig;
import com.myinsure.admin.website.web.WebConfigService;

public class WebFrontColumnService
{
	@SuppressWarnings("unused")
	private static final Log log = Log.getLog(WebFrontColumnService.class);
	private static final WebFrontColumnService webColumnService = new WebFrontColumnService();
	public static WebFrontColumnService me()
	{
		return webColumnService;
	}
	/**
	 * 查找新闻栏目
	 * @return
	 */
	public WebColumn getColumnByName(String name){
		String sql = "select * from web_column where name = '" + name + "'";
		WebColumn column = WebColumn.dao.findFirst(sql);
		if(column == null){
			return null;
		}
		WebConfig sysDomain = WebConfigService.me().getWebConfigByName("sys_domain");
		WebConfig webDomain = WebConfigService.me().getWebConfigByName("web_domain");
		if("简单列表".equalsIgnoreCase(column.getStr("faclass")) || "带图列表".equalsIgnoreCase(column.getStr("faclass"))){
			column.set("out_url", sysDomain.getStr("value") + "/" + column.getStr("out_url"));
		}else{
			column.set("out_url", webDomain.getStr("value") + "/" + column.getStr("out_url"));
		}
		return column;
	}
	/**
	 * 查找所有一级导航及二级导航
	 * @return
	 */
	public List<WebColumn> getColumns(){
		String sql = "select * from web_column where isshow=1 and nav=1 and wap_nav is null order by orders asc";
		List<WebColumn> columns = WebColumn.dao.find(sql);
		Iterator<WebColumn> iter = columns.iterator();
		String sql2 = "select * from web_column where isshow=1 and nav=1 and wap_nav=? order by orders";
		WebConfig sysDomain = WebConfigService.me().getWebConfigByName("sys_domain");
		WebConfig webDomain = WebConfigService.me().getWebConfigByName("web_domain");
		while(iter.hasNext()){
			WebColumn column = iter.next();
			if("简单列表".equalsIgnoreCase(column.getStr("faclass")) || "带图列表".equalsIgnoreCase(column.getStr("faclass"))){
				column.set("out_url", sysDomain.getStr("value") + "/" + column.getStr("out_url"));
			}else{
				column.set("out_url", webDomain.getStr("value") + "/" + column.getStr("out_url"));
			}
			List<WebColumn> list = WebColumn.dao.find(sql2,column.getStr("id"));
			Iterator<WebColumn> iterator = list.iterator();
			while(iterator.hasNext()){
				WebColumn column2 = iterator.next();
				if("简单列表".equalsIgnoreCase(column2.getStr("faclass")) || "带图列表".equalsIgnoreCase(column2.getStr("faclass"))){
					column2.set("out_url", sysDomain.getStr("value") + "/" + column2.getStr("out_url"));
				}else{
					column2.set("out_url", webDomain.getStr("value") + "/" + column2.getStr("out_url"));
				}
			}
			column.put("sonColumns", list);
		}
		return columns;
	}
	/**
	 * 根据一级导航查找二级导航
	 * @return
	 */
	public WebColumn findSonColumnsByColumn(String param,Integer size){
		WebColumn column = getColumnByName(param);
		String sql = " from web_column where isshow=1 and nav=1 and wap_nav = '" + column.getStr("id") + "' order by orders";
		Page<WebColumn> page = WebColumn.dao.paginate(1, size, "select * ", sql);
		List<WebColumn> list = page.getList();
		WebConfig sysDomain = WebConfigService.me().getWebConfigByName("sys_domain");
		WebConfig webDomain = WebConfigService.me().getWebConfigByName("web_domain");
		Iterator<WebColumn> iter = list.iterator();
		while(iter.hasNext()){
			WebColumn column2 = iter.next();
			if("简单列表".equalsIgnoreCase(column2.getStr("faclass")) || "带图列表".equalsIgnoreCase(column2.getStr("faclass"))){
				column2.set("out_url", sysDomain.getStr("value") + "/" + column2.getStr("out_url"));
			}else{
				column2.set("out_url", webDomain.getStr("value") + "/" + column2.getStr("out_url"));
			}
		}
		column.put("list",list);
		return column;
	}
	/**
	 * 根据一级导航查找所有二级导航
	 * @param columnId
	 * @return
	 */
	public WebColumn findSonColumnByColumnId(String columnId){
		WebConfig sysDomain = WebConfigService.me().getWebConfigByName("sys_domain");
		WebConfig webDomain = WebConfigService.me().getWebConfigByName("web_domain");
		
		WebColumn column = WebColumn.dao.findById(columnId);
		if("简单列表".equalsIgnoreCase(column.getStr("faclass")) || "带图列表".equalsIgnoreCase(column.getStr("faclass"))){
			column.set("out_url", sysDomain.getStr("value") + "/" + column.getStr("out_url"));
		}else{
			column.set("out_url", webDomain.getStr("value") + "/" + column.getStr("out_url"));
		}
		String sql = "select * from web_column where isshow=1 and nav=1 and wap_nav = '" + columnId + "' order by orders";
		List<WebColumn> list = WebColumn.dao.find(sql);
		Iterator<WebColumn> iter = list.iterator();
		while(iter.hasNext()){
			WebColumn column2 = iter.next();
			if("简单列表".equalsIgnoreCase(column2.getStr("faclass")) || "带图列表".equalsIgnoreCase(column2.getStr("faclass"))){
				column2.set("out_url", sysDomain.getStr("value") + "/" + column2.getStr("out_url"));
			}else{
				column2.set("out_url", webDomain.getStr("value") + "/" + column2.getStr("out_url"));
			}
		}
		column.put("list", list);
		return column;
	}
	/**
	 * 查找二级导航，及其所属的一级导航
	 * @return
	 */
	public WebColumn getColumnBySonColumn(String columnId){
		WebColumn column = WebColumn.dao.findById(columnId); 
		String sql = "select * from web_column where isshow=1 and id = " + column.getStr("wap_nav");
		WebColumn firstColumn = WebColumn.dao.findFirst(sql);
		column.put("firstColumn", firstColumn);
		return column;
	}
}









