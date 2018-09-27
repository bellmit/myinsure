package com.myinsure.admin.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myinsure.admin.base.SysKeyIdService;
import com.myinsure.admin.fun.SysRoleFun;
import com.myinsure.admin.fun.SysRoleFunService;
import com.myinsure.admin.module.SysModule;
import com.myinsure.admin.module.SysModuleService;

import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.mysql.jdbc.StringUtils;
/**
 * 角色管理
 * 
 * @author sxf
 * 
 */

public class SysRoleController extends Controller
{
    Log log = Log.getLog(SysRoleController.class);

    /**
     * 角色列表
     */
    public void roleList()
    {
	String queryText = this.getPara("queryText");
	this.setAttr("queryText", queryText);
	String pageNumber = this.getPara("pageNumber");
	if (StringUtils.isEmptyOrWhitespaceOnly(pageNumber))
	{
	    pageNumber = "1";
	}
	this.setAttr("pageNumber", pageNumber);
	Page<SysRole> re = SysRoleService.me().rolePage(queryText, pageNumber);
	List<SysRole> result = re.getList();
	this.setAttr("totalRow", re.getTotalRow());
	this.setAttr("totalPage", re.getTotalPage());

	this.setAttr("roleList", result);
	this.render("/sysadmin/role/rolelist.html");
    }
    /**
     * 角色列表
     */
    public void roleList1()
    {
    	String queryText = this.getPara("queryText");
    	this.setAttr("queryText", queryText);
    	int draw = this.getParaToInt("draw");
    	int start = this.getParaToInt("start");
    	int length = this.getParaToInt("length");
    	String csrfmiddlewaretoken = this.getPara("csrfmiddlewaretoken");
    	
    	int pageNumber = start/length + 1;
    	this.setAttr("pageNumber", pageNumber);
    	Page<SysRole> re = SysRoleService.me().rolePage(queryText, String.valueOf(pageNumber));
    	List<SysRole> result = re.getList();
    	setAttr("draw", draw);
    	setAttr("pageNumber", pageNumber);
    	setAttr("pageSize", re.getPageSize());
    	this.setAttr("totalRow", re.getTotalRow());
    	this.setAttr("totalPage", re.getTotalPage());
    	
    	this.setAttr("list", result);
    	renderJson();
    }

    /**
     * 新增角色页面
     */
    public void roleAdd()
    {

	this.render("/sysadmin/role/roleadd.html");
    }

    /**
     * 新增角色
     */
    public void roleAddSave()
    {
	String cookie = this.getCookie("user_id");
	String cook_userid = "";
	try
	{
	    cook_userid = cookie.substring(63, cookie.length());
	} catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	int r_id = SysKeyIdService.me().generateKeyMysql("sys_role");
	String roleName = this.getPara("roleName");
	String roleMemo = this.getPara("roleMemo");
	Map<String, String> params = new HashMap<String, String>();
	params.put("role_id", String.valueOf(r_id));
	params.put("roleName", roleName);
	params.put("roleMemo", roleMemo);
	boolean roleAdd = SysRoleService.me().roleAdd(params, cook_userid);
	if (roleAdd)
	{
	    this.renderJson("msg", "添加成功");
	    log.info("----角色新增成功----");
	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.info("----新增角色保存时，服务器异常----");
	}

    }

    /**
     * 角色编辑页面
     */
    public void roleEdit()
    {
	String rid = this.getPara("rid");
	SysRole sysRole = SysRoleService.me().getById(rid);
	this.setAttr("sysRole", sysRole);
	this.render("/sysadmin/role/roleedit.html");
    }

    /**
     * 编辑角色
     */
    public void roleEditSave()
    {
	String rid = this.getPara("rid");
	String roleName = this.getPara("roleName");
	String roleMemo = this.getPara("roleMemo");
	Map<String, String> params = new HashMap<String, String>();
	params.put("id", rid);
	params.put("roleName", roleName);
	params.put("roleMemo", roleMemo);
	boolean roleEdit = SysRoleService.me().roleEdit(params);
	if (roleEdit)
	{
	    this.renderJson("msg", "更新成功");
	    log.info("----角色更新成功----");
	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.error("----角色更新时，服务器异常----");
	}

    }

    /**
     * 删除角色 在页面中暂未使用，ajax已定义
     */
    // public void roleDelete()
    // {
    // boolean roleDelete = SysRoleService.me().roleDelete(this.getPara("id"));
    // if (roleDelete)
    // {
    // System.out.println("sss");
    // this.renderText("删除成功");
    // log.info("----角色删除成功----");
    // } else
    // {
    // this.renderText("服务器异常");
    // log.error("----角色删除时，服务器异常----");
    // }
    //
    // }

    /**
     * 角色权限
     */
    public void roleFunList()
    {
	String roleId = this.getPara("id");
	List<SysModule> moduleList = SysModuleService.me().moduleSonList();
	this.setAttr("moduleList", moduleList);
	SysRole sysRole = SysRoleService.me().getById(roleId);
	this.setAttr("roleName", sysRole.get("role_name"));
	this.setAttr("roleId", roleId);
	this.render("/sysadmin/role/rolefunlist.html");
    }

    /**
     * 角色权限功能
     */
    public void roleFun()
    {
	String roleId = this.getPara("id");
	List<SysRoleFun> roleFunList = SysRoleFunService.me().roleFunList(roleId);
	List<SysRoleFun> roleFunToAddList = SysRoleFunService.me().roleFunToAddList(roleId);
	List<Map<String, String>> funList = new ArrayList<Map<String, String>>();
	for (SysRoleFun sysRoleFun : roleFunList)
	{
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("module_id", sysRoleFun.getStr("module_id"));
	    map.put("fun_id", sysRoleFun.getStr("fun_id"));
	    map.put("fun_name", sysRoleFun.getStr("fun_name"));
	    map.put("state", "1");
	    funList.add(map);
	}
	for (SysRoleFun sysRoleFun : roleFunToAddList)
	{
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("module_id", sysRoleFun.getStr("module_id"));
	    map.put("fun_id", sysRoleFun.getStr("fun_id"));
	    map.put("fun_name", sysRoleFun.getStr("fun_name"));
	    map.put("state", "0");

	    funList.add(map);
	}
	this.renderJson(funList);
    }

    /**
     * 新增角色权限
     */
    public void roleFunAddSave()
    {
	String [] funIds = this.getParaValues("funId");
	String roleId = this.getPara("id");
	String cookie = this.getCookie("user_id");
	String cook_userid = "";
	try
	{
	    cook_userid = cookie.substring(63, cookie.length());
	} catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	String roleFun = SysRoleFunService.me().roleFunAdd(funIds, roleId, cook_userid);
	if (roleFun == null)
	{
	    this.renderJson("msg", "编辑成功");
	    log.info("----修改角色权限成功----");
	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.error("----修改角色权限时，服务器异常----");
	}
    }

    /**
     * 批量删除
     */
    public void delBatch()
    {
	String [] roleids = this.getParaValues("roleId");

	for (String roleId : roleids)
	{
	    try
	    {
		SysRoleService.me().roleDelete(roleId);
	    } catch (Exception e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	this.renderJson("msg", "删除成功");
	log.info("----角色删除成功----");
    }
}
