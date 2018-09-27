package com.myinsure.admin.module;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.mysql.jdbc.StringUtils;

/**
 * 后台模块管理
 */
public class SysModuleController extends Controller
{
    Log log = Log.getLog(SysModuleController.class);

    /**
     * 模块列表页
     */
    public void moduleList()
    {
	String queryText = this.getPara("queryText");

	String pageNumber = this.getPara("pageNumber");
	if (StringUtils.isEmptyOrWhitespaceOnly(pageNumber))
	{
	    pageNumber = "1";
	}
	this.setAttr("pageNumber", pageNumber);
	Page<SysModule> re = SysModuleService.me().modulePage(queryText, pageNumber);
	List<SysModule> result = re.getList();

	this.setAttr("queryText", queryText);
	this.setAttr("moduleList", result);
	this.setAttr("totalRow", re.getTotalRow());
	this.setAttr("totalPage", re.getTotalPage());
	this.render("/sysadmin/module/modulelist.html");

    }
    /**
     * 模块列表页
     */
    public void moduleList1()
    {
    	String queryText = this.getPara("queryText");
    	
    	int draw = this.getParaToInt("draw");
    	int start = this.getParaToInt("start");
    	int length = this.getParaToInt("length");
    	String csrfmiddlewaretoken = this.getPara("csrfmiddlewaretoken");
    	
    	int pageNumber = start/length + 1;
    	this.setAttr("pageNumber", pageNumber);
    	Page<SysModule> re = SysModuleService.me().modulePage(queryText, String.valueOf(pageNumber));
    	List<SysModule> result = re.getList();
    	
    	this.setAttr("queryText", queryText);
    	this.setAttr("list", result);
    	setAttr("draw", draw);
    	setAttr("pageNumber", pageNumber);
    	setAttr("pageSize", re.getPageSize());
    	this.setAttr("totalRow", re.getTotalRow());
    	this.setAttr("totalPage", re.getTotalPage());
    	renderJson();
    	
    }

    /**
     * 模块编辑页面
     */
    public void moduleEdit()
    {
	String id = this.getPara("id");
	SysModule sysModule = SysModuleService.me().getById(id);
	this.setAttr("sysModule", sysModule);
	this.setAttr("moduleList", SysModuleService.me().moduleList());
	this.render("/sysadmin/module/moduleedit.html");
    }

    /**
     * 编辑模块
     */
    public void moduleEditSave()
    {

	Map<String, String> params = new HashMap<String, String>();
	String moduleId = this.getPara("moduleId");
	String moduleName = this.getPara("moduleName");
	String icons = this.getPara("icons");
	String moduleCode = this.getPara("moduleCode");
	String parentId = this.getPara("parentId");
	String moduleOrder = StringUtils.isEmptyOrWhitespaceOnly(this.getPara("moduleOrder")) ? "0" : this.getPara("moduleOrder");
	String imgurl = this.getPara("imgurl");
	params.put("moduleId", moduleId);
	params.put("moduleName", moduleName);
	params.put("icons", icons);
	params.put("moduleCode", moduleCode);
	params.put("parentId", parentId);
	params.put("moduleOrder", moduleOrder);
	params.put("imgurl", imgurl);
	if(com.myinsure.utils.StringUtils.isBlank(parentId)){//如果父级模块为空
		params.put("moduleLevel", "0");
	}else{
		params.put("moduleLevel", "1");
	}
	boolean moduleUpdate = SysModuleService.me().moduleUpdate(params);
	if (moduleUpdate)
	{
	    this.renderJson("msg", "修改成功");
	    log.info("----后台模块修改成功----");
	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.info("----后台模块修改时，服务器异常----");
	}

    }

    /**
     *排序修改 
     * @throws IOException 
     */
    public void sortUpdate()
    {
	String sorts = getPara("sorts");
	String id = getPara("id");
	String queryText = this.getPara("queryText");

	String pageNumber = this.getPara("pageNumber");
	if (StringUtils.isEmptyOrWhitespaceOnly(pageNumber))
	{
	    pageNumber = "1";
	}
	this.setAttr("pageNumber", pageNumber);
	Page<SysModule> re = SysModuleService.me().modulePage(queryText, pageNumber);
	List<SysModule> result = re.getList();
	boolean bol = SysModuleService.me().sortsupdate(id, sorts);
	if (bol)
	{
	    this.setAttr("queryText", queryText);
	    this.setAttr("moduleList", result);
	    this.setAttr("totalRow", re.getTotalRow());
	    this.setAttr("totalPage", re.getTotalPage());
	    this.renderJson("msg", "修改成功");
	    log.info("----后台排序修改成功----");
	} else
	{
	    this.renderJson("msg", "修改失败");
	    log.error("----后台排序修改失败----");
	}

    }

    /**
     * 新增模块页面
     */
    public void moduleAdd()
    {
    	this.setAttr("moduleList", SysModuleService.me().moduleList());
    	this.render("/sysadmin/module/moduleadd.html");
    }

    /**
     * 新增模块
     */
    public void moduleAddSave()
    {
	String cookie = this.getCookie("user_id");
	String cook_userid = "";
	try
	{
	    cook_userid = cookie.substring(63, cookie.length());
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
	Map<String, String> params = new HashMap<String, String>();
	String moduleName = this.getPara("moduleName");
	String moduleCode = this.getPara("moduleCode");
	String icons = this.getPara("icons");
	String parentId = this.getPara("parentId");
	String moduleOrder = StringUtils.isEmptyOrWhitespaceOnly(this.getPara("moduleOrder")) ? "0" : this.getPara("moduleOrder");
	String imgurl = this.getPara("imgurl");
	params.put("moduleName", moduleName);
	params.put("moduleCode", moduleCode);
	params.put("parentId", parentId);
	params.put("icons", icons);
	params.put("moduleOrder", moduleOrder);
	params.put("imgurl", imgurl);
	if(com.myinsure.utils.StringUtils.isBlank(parentId)){//如果父级模块为空
		params.put("moduleLevel", "0");
	}else{
		params.put("moduleLevel", "1");
	}
	boolean moduleAdd = SysModuleService.me().moduleAdd(params, cook_userid);
	if (moduleAdd)
	{
	    this.renderJson("msg", "添加成功");
	    log.info("----后台模块新增成功----");
	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.error("----后台模块添加时，服务器异常----");
	}
    }

    /**
     * 删除模块
     */
    public void moduleDelete()
    {
	boolean moduleDe = SysModuleService.me().moduleDelete(this.getPara("id"));
	if (moduleDe)
	{
	    this.renderJson("msg", "删除成功");
	    log.info("----后台模块删除成功----");
	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.error("----后台模块删除时，服务器异常----");
	}
    }

    /**
     * 批量删除
     */
    public void delBatch()
    {
	for (String moduleId : this.getParaValues("moduleId"))
	{
	    boolean moduleDelete = SysModuleService.me().moduleDelete(moduleId);
	    if (!moduleDelete)
	    {
		this.renderJson("msg", "删除失败");
		log.error("----后台模块删除失败----");
		return;
	    }
	}
	this.renderJson("msg", "删除成功");
	log.info("----后台模块删除成功----");

    }
}
