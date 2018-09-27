package com.myinsure.admin.module;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.ListenerManager;

import com.myinsure.admin.base.SysKeyId;
import com.myinsure.admin.base.SysKeyIdService;
import com.myinsure.admin.login.LoginService;
import com.myinsure.utils.DateUtil;
import com.myinsure.utils.StringUtils;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;

public class SysModuleService
{
    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(SysModuleService.class);

    private static final SysModuleService sysModuleService = new SysModuleService();

    public static SysModuleService me()
    {
	return sysModuleService;
    }

    /**
     * 获取所有一级模块列表
     * @return
     */
    public List<SysModule> moduleList()
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_module where is_show = 1 and module_level = 0 order by order_numb asc ");
	return SysModule.dao.find(sql.toString());
    }

    /**
     * 根据 一级模块module_id 获取二级模块
     * @return
     */
    public List<SysModule> moduleList(String moduleId)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_module where is_show = 1 and parent_id = " + moduleId + " order by order_numb desc ");
	return SysModule.dao.find(sql.toString());
    }
    /**
     * 获取所有二级模块列表
     * @return
     */
    public List<SysModule> moduleSonList(){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select * from sys_module where is_show = 1 and module_level = 1 order by order_numb asc ");
    	return SysModule.dao.find(sql.toString());
    }
    

    /**
     * 分页查询模块
     * 
     * @param queryText
     * @param pageNumber
     * @return
     */
    public Page<SysModule> modulePage(String queryText, String pageNumber)
    {
	String where = "";
	if (StringUtils.isBlank(queryText))
	{
	    where = "from sys_module order by order_numb   ";
	} else
	{
	    where = "from sys_module where module_name like '%" + queryText + "%'   order by order_numb  ";
	}
	Page<SysModule> modulePage = SysModule.dao.paginate(Integer.parseInt(pageNumber), 10, "select *", where);
	return modulePage;
    }

    /**
     * 新增模块
     * 
     * @param params
     * @return
     */
    public boolean moduleAdd(Map params, String userid)
    {

	SysModule sysModule = new SysModule();
	sysModule.set("module_id", generateModuleId());
	sysModule.set("module_name", params.get("moduleName"));
	sysModule.set("icons", params.get("icons"));
	sysModule.set("module_code", params.get("moduleCode"));
	sysModule.set("parent_id", params.get("parentId"));
	sysModule.set("order_numb", params.get("moduleOrder"));
	sysModule.set("module_level", params.get("moduleLevel"));
	sysModule.set("is_show", 1);
	sysModule.set("imgurl", params.get("imgurl"));
	sysModule.set("add_userid", userid);
	sysModule.set("add_date", DateUtil.getTodaySec());
	boolean bolmod = sysModule.save();

	return bolmod;
    }

    /**
     * 删除模块
     * 
     * @param id
     * @return
     */
    public boolean moduleDelete(String id)
    {
	SysModule sysModule = SysModule.dao.findById(id);
	boolean bol = sysModule.delete();
	return bol;

    }

    /**
     * 通过id获取模块
     * 
     * @param id
     * @return
     */
    public SysModule getById(String id)
    {
	return SysModule.dao.findById(id);

    }

    /**
     * 更新模块
     * 
     * @param params
     * @return
     */
    public boolean moduleUpdate(Map params)
    {
	SysModule sysModule = SysModule.dao.findById(params.get("moduleId"));
	sysModule.set("module_name", params.get("moduleName"));
	sysModule.set("module_code", params.get("moduleCode"));
	sysModule.set("parent_id", params.get("parentId"));
	sysModule.set("module_level", params.get("moduleLevel"));
	sysModule.set("icons", params.get("icons"));
	sysModule.set("order_numb", params.get("moduleOrder"));
	sysModule.set("imgurl", params.get("imgurl"));
	sysModule.set("update_time", DateUtil.getTodaySec());
	boolean bolmodule = sysModule.update();
	return bolmodule;

    }

    /**
     * 更改模块排序
     */
    public boolean sortsupdate(String sid, String sort)
    {
	boolean bol;
	SysModule sysModule = SysModule.dao.findById(sid);
	sysModule.set("order_numb", sort);
	bol = sysModule.update();
	return bol;
    }

    /**
     * 获取最大id
     * 
     * @return
     */
    public String getMaxId()
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select max(module_id) max_id from sys_module ");
	SysModule sysModule = SysModule.dao.findFirst(sql.toString());
	return sysModule.getStr("max_id");

    }

    /**
     * 生成模块ID
     * 
     * 模块id四位
     * 
     * @return
     */
    public String generateModuleId()
    {
	String moduleId = null;
	SysKeyId sysKeyId = SysKeyIdService.me().getByCodeExt("sys_dept", "MD");
	if (null == sysKeyId)
	{
	    String maxId = getMaxId();
	    if (null == maxId || StringUtils.isBlank(maxId))
	    {
		moduleId = "1001";
		SysKeyIdService.me().updateOrSave("sys_dept", "MD", moduleId);
	    } else
	    {
		int id = Integer.parseInt(maxId);
		moduleId = String.valueOf(id + 1);
		SysKeyIdService.me().updateOrSave("sys_dept", "MD", moduleId);
	    }
	} else
	{
	    int id = Integer.parseInt((String) sysKeyId.get("max_value"));
	    moduleId = String.valueOf(id + 1);
	    SysKeyIdService.me().updateOrSave("sys_dept", "MD", moduleId);
	}
	return moduleId;
    }

}
