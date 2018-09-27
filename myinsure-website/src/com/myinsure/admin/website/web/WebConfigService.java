package com.myinsure.admin.website.web;

import java.util.List;
import com.jfinal.log.Log;

public class WebConfigService
{
	@SuppressWarnings("unused")
	private static final Log log = Log.getLog(WebConfigService.class);

	private static final WebConfigService webConfigService = new WebConfigService();

	public static WebConfigService me()
	{
		return webConfigService;
	}

	/**
	 * 修改网站设置
	 * @param params
	 * @return
	 */
	public boolean configsave(String key, String value)
	{
		String sql = "select * from web_config where name=?";
		WebConfig webConfig = WebConfig.dao.findFirst(sql, key);
		webConfig.set("value", value);
		return webConfig.update();
	}

	/**
	 * 查询网站配置文件
	 * @return
	 */
	public List<WebConfig> getWebConfig()
	{
		String sql = "select * from web_config";
		List<WebConfig> webConfig = WebConfig.dao.find(sql);
		return webConfig;
	}
	/**
	 * 根据名称查询网站配置的值
	 * @param name
	 */
	public WebConfig getWebConfigByName(String name){
		String sql = "select * from web_config where name=?";
		WebConfig webConfig = WebConfig.dao.findFirst(sql, name);
		return webConfig;
	}
}
