package com.myinsure.admin.fun;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.myinsure.utils.DateUtil;
import com.myinsure.utils.StringUtils;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;

public class SysFunService
{

    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(SysFunService.class);// 日志

    // 单例模式
    private static final SysFunService sysFunService = new SysFunService();

    public static SysFunService me()
    {
	return sysFunService;
    }

    /**
     * 获取功能列表
     * 
     * @return
     */
    public List<SysFun> funList()
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select *,sf.is_show sfshow from sys_fun sf left join sys_module sm on sf.module_id=sm.module_id order by sf.order desc ");
	List<SysFun> result = SysFun.dao.find(sql.toString());
	return result;
    }

    /**
     * 获取功能列表
     * 
     * @return
     */
    public List<SysFun> funList(String module_id)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_fun where isshow = 1 and module_id = " + module_id + " order by  order desc ");
	List<SysFun> result = SysFun.dao.find(sql.toString());
	return result;
    }

    /**
     * 分页查询功能列表
     * 
     * @param queryText
     * @param pageNumber
     * @return
     */
    public Page<SysFun> funPage(String queryText, String pageNumber)
    {
	String where = "";
	if (StringUtils.isBlank(queryText))
	{
	    where = "from sys_fun sf left join sys_module sm on sf.module_id=sm.module_id order by sm.order_numb, sf.orders   ";
	} else
	{
	    where = "from sys_fun sf left join sys_module sm on sf.module_id=sm.module_id where sf.fun_name like '%" + queryText + "%'  order by sm.order_numb , sf.orders   ";
	}
	Page<SysFun> funPage = SysFun.dao.paginate(Integer.parseInt(pageNumber), 10, "select *,sf.is_show sfshow ", where);
	return funPage;
    }

    /**
     * 新增功能
     * 
     * @param params
     * @return
     */
    public boolean funAddSave(Map<String, String> params, String userid)
    {
	boolean bolFun;
	SysFun sysFun = new SysFun();
	sysFun.set("fun_id", params.get("fun_id"));
	sysFun.set("fun_name", params.get("funName"));
	sysFun.set("module_id", params.get("moduleId"));
	sysFun.set("url", params.get("funUrl"));
	sysFun.set("orders", Integer.valueOf(params.get("funOrder")));
	sysFun.set("is_show", params.get("isShow"));
	sysFun.set("add_userid", userid);
	sysFun.set("add_date", DateUtil.getTodaySec());
	bolFun = sysFun.save();
	return bolFun;
    }

    /**
     * 删除功能
     * 
     * @param id
     * @return
     */
    public boolean funDelete(String id)
    {
	SysFun sysFun = SysFun.dao.findById(id);
	boolean bool = sysFun.delete();
	return bool;

    }

    /**
     * 通过id获取功能
     */
    public SysFun getById(String id)
    {
	SysFun sysFun = SysFun.dao.findById(id);
	return sysFun;

    }

    /**
     * 编辑功能
     */
    public boolean funEditSave(Map<String, String> params)
    {
	SysFun sysFun = SysFun.dao.findById(params.get("fid"));
	sysFun.set("fun_name", params.get("funName"));
	sysFun.set("module_id", params.get("moduleId"));
	sysFun.set("url", params.get("funUrl"));
	sysFun.set("is_show", params.get("isShow"));
	sysFun.set("orders", params.get("funOrder"));
	sysFun.set("update_time", DateUtil.getTodaySec());
	boolean update = sysFun.update();
	return update;

    }

    public boolean sortsUp(String id, String sort)
    {
	SysFun sysFun = SysFun.dao.findById(id);
	sysFun.set("orders", sort);
	boolean bol = sysFun.update();
	return bol;
    }
    /**
	 * 根据url查询链接名及其所属模块
	 * @param url
	 * @return
	 */
	public SysFun getFunByUrl(String url){
		String sql = "select * from sys_fun sf left join sys_module sm on sf.module_id = sm.module_id where sf.url = '" + url + "'";
		SysFun sysFun = SysFun.dao.findFirst(sql);
		return sysFun;
	}
}
