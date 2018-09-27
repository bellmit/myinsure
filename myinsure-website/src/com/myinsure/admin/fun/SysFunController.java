package com.myinsure.admin.fun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.myinsure.admin.base.SysKeyIdService;
import com.myinsure.admin.module.SysModuleService;
import com.myinsure.interceptor.AdminInterceptor;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.mysql.jdbc.StringUtils;

/**
 * 功能列表
 * 
 */
@Before({ AdminInterceptor.class })
public class SysFunController extends Controller
{
    Log log = Log.getLog(SysFunController.class);

    /**
     * 功能列表页
     */
    public void funList()
    {
	String queryText = this.getPara("queryText");
	this.setAttr("queryText", queryText);
	String pageNumber = this.getPara("pageNumber");
	if (StringUtils.isEmptyOrWhitespaceOnly(pageNumber))
	{
	    pageNumber = "1";
	}
	this.setAttr("pageNumber", pageNumber);
	Page<SysFun> re = SysFunService.me().funPage(queryText, pageNumber);
	List<SysFun> result = re.getList();
	setAttr("pageNumber", pageNumber);
	setAttr("pageSize", re.getPageSize());
	this.setAttr("totalRow", re.getTotalRow());
	this.setAttr("totalPage", re.getTotalPage());
	this.setAttr("funList", result);
	this.render("/sysadmin/fun/funlist.html");
    }
    /**
     * 功能列表页
     */
    public void funList1()
    {
    	int draw = this.getParaToInt("draw");
    	int start = this.getParaToInt("start");
    	int length = this.getParaToInt("length");
    	String csrfmiddlewaretoken = this.getPara("csrfmiddlewaretoken");
    	
    	int pageNumber = start/length + 1;
    	String queryText = this.getPara("queryText");
    	this.setAttr("queryText", queryText);
    	Page<SysFun> re = SysFunService.me().funPage(queryText, String.valueOf(pageNumber));
    	List<SysFun> result = re.getList();
    	
    	setAttr("draw", draw);
    	setAttr("pageNumber", pageNumber);
    	setAttr("pageSize", re.getPageSize());
    	this.setAttr("totalRow", re.getTotalRow());
    	this.setAttr("totalPage", re.getTotalPage());
    	this.setAttr("list", result);
    	renderJson();
    }

    /**
     * 新增功能页面
     */
    public void funAdd()
    {
	this.setAttr("moduleList", (new SysModuleService()).moduleSonList());
	this.render("/sysadmin/fun/funadd.html");

    }

    /**
     * 新增功能
     */
    public void funAddSave()
    {
	String funOrder = StringUtils.isEmptyOrWhitespaceOnly(this.getPara("funOrder")) ? "0" : this.getPara("funOrder");
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
	int fun_id = SysKeyIdService.me().generateKeyMysql("sys_fun");
	params.put("fun_id", String.valueOf(fun_id));
	params.put("isShow", this.getPara("isShow"));
	params.put("moduleId", this.getPara("moduleId"));
	params.put("funName", this.getPara("funName"));
	params.put("funUrl", this.getPara("funUrl"));
	params.put("funOrder", funOrder);
	boolean funAdd = SysFunService.me().funAddSave(params, cook_userid);
	if (funAdd)
	{
	    this.renderJson("msg", "新增成功");
	    log.info("---- 新增功能菜单成功----");
	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.error("----新增功能菜单时，服务器异常 ----");
	}

    }

    /**
     * 编辑功能列表页
     */
    public void funEdit()
    {
	SysFun sysFun = SysFunService.me().getById(this.getPara("fid"));
	this.setAttr("moduleList", SysModuleService.me().moduleSonList());
	this.setAttr("sysFun", sysFun);
	this.render("/sysadmin/fun/funedit.html");
    }

    /**
     * 修改排序
     */
    public void sortUp()
    {
	String id = getPara("id");
	String sort = getPara("sort");
	boolean funEdit = SysFunService.me().sortsUp(id, sort);
	String queryText = this.getPara("queryText");
	this.setAttr("queryText", queryText);
	String pageNumbers = this.getPara("pageNumber");
	if (StringUtils.isEmptyOrWhitespaceOnly(pageNumbers))
	{
	    pageNumbers = "1";
	}

	Page<SysFun> re = SysFunService.me().funPage(queryText, pageNumbers);
	List<SysFun> result = re.getList();
	int pageNumber = re.getPageNumber();
	int pageSize = re.getPageSize();
	int totalPage = re.getTotalPage();
	int totalRow = re.getTotalRow();
	this.setAttr("pageNumber", pageNumber);
	this.setAttr("pageSize", pageSize);
	this.setAttr("totalPage", totalPage);
	this.setAttr("totalRow", totalRow);
	this.setAttr("funList", result);
	if (funEdit)
	{
	    this.renderJson("msg", "修改成功");
	    log.info("功能排序修改成功");
	} else
	{
	    this.renderJson("msg", "修改失败");
	    log.error("功能排序修失败");
	}
    }

    /**
     * 编辑功能
     */
    public void funEditSave()
    {
	String funOrder = StringUtils.isEmptyOrWhitespaceOnly(this.getPara("funOrder")) ? "0" : this.getPara("funOrder");
	Map<String, String> params = new HashMap<String, String>();
	params.put("fid", this.getPara("fid"));
	params.put("isShow", this.getPara("isShow"));
	params.put("moduleId", this.getPara("moduleId"));
	params.put("funName", this.getPara("funName"));
	params.put("funUrl", this.getPara("funUrl"));
	params.put("funOrder", funOrder);
	boolean funEdit = SysFunService.me().funEditSave(params);
	if (funEdit)
	{
	    this.renderJson("msg", "更新成功");
	    log.info("----更新功能菜单保存成功 ----");
	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.error("---- 更新功能菜单时，服务器异常----");
	}
    }

    /**
     * 删除功能
     */
    public void funDelete()
    {
	String funid = this.getPara("fid");
	boolean funDelete = SysFunService.me().funDelete(funid);
	if (funDelete)
	{
	    this.renderJson("msg", "删除成功");
	    log.info("----功能菜单删除成功----");
	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.error("----功能菜单删除时，服务器异常----");
	}
    }

    /**
     * 批量删除
     */
    public void delBatch()
    {
	for (String funId : this.getParaValues("funId"))
	{
	    boolean funDelete = SysFunService.me().funDelete(funId);
	    if (!funDelete)
	    {
		this.renderJson("msg", "删除失败");
		log.error(funId + "---- 功能菜单删除失败----");
		return;
	    }
	}
	this.renderJson("msg", "删除成功");
	log.info("---- 功能菜单删除成功----");
    }
}
