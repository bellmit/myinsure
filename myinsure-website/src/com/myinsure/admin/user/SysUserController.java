package com.myinsure.admin.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.myinsure.admin.base.SysKeyIdService;
import com.myinsure.admin.role.SysRole;
import com.myinsure.admin.role.SysRoleService;
import com.myinsure.admin.role.SysUserRole;
import com.myinsure.admin.role.SysUserRoleService;
import com.myinsure.interceptor.AdminInterceptor;
import com.myinsure.plugin.DataTable;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mysql.jdbc.StringUtils;

/**
 * 用户管理
 * 
 * @author sxf 2014-7-14
 * 
 */
@Before({ AdminInterceptor.class })
public class SysUserController extends Controller
{
    Log log = Log.getLog(SysUserController.class);

    /**
     * 获取用户列表
     */
    /**
     * index
     */
    public void listdata()
    {
	render("/sysadmin/user/user_table_data.html");
    }

    public void userlist()
    {
	String queryText = this.getPara("queryText");
	this.setAttr("queryText", queryText);
	String pageNumber = this.getPara("pageNumber");
	if (StringUtils.isEmptyOrWhitespaceOnly(pageNumber))
	{
	    pageNumber = "1";
	}
	this.setAttr("pageNumber", pageNumber);
	Page<SysUser> re = SysUserService.me().searchUser(queryText, Integer.parseInt(pageNumber));
	List<SysUser> result = re.getList();
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	for (SysUser record : result)
	{
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("user_id", record.get("user_id").toString());
	    map.put("realName", (String) record.get("real_name"));
	    map.put("code", (String) record.get("user_code"));
	    map.put("roleName", (String) record.get("role_name"));
	    list.add(map);
	}

	this.setAttr("totalRow", re.getTotalRow());
	this.setAttr("totalPage", re.getTotalPage());
	setAttr("userlist", list);
	render("/sysadmin/user/userlist.html");
    }
    
    
    public void userlist1()
    {
    	String queryText = this.getPara("queryText");
    	this.setAttr("queryText", queryText);
    	int draw = this.getParaToInt("draw");
    	int start = this.getParaToInt("start");
    	int length = this.getParaToInt("length");
    	String csrfmiddlewaretoken = this.getPara("csrfmiddlewaretoken");
    	
    	int pageNumber = start/length + 1;
    	this.setAttr("pageNumber", pageNumber);
    	Page<SysUser> re = SysUserService.me().searchUser(queryText, pageNumber);
    	List<SysUser> result = re.getList();
    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    	for (SysUser record : result)
    	{
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("user_id", record.get("user_id").toString());
    		map.put("realName", (String) record.get("real_name"));
    		map.put("code", (String) record.get("user_code"));
    		map.put("roleName", (String) record.get("role_name"));
    		list.add(map);
    	}
    	setAttr("draw", draw);
    	setAttr("pageNumber", pageNumber);
    	setAttr("pageSize", re.getPageSize());
    	this.setAttr("totalRow", re.getTotalRow());
    	this.setAttr("totalPage", re.getTotalPage());
    	setAttr("list", list);
    	renderJson();
    }

    /**
     * res表格数据
     */
    public void tableData()
    {
	int pageNumber = getParaToInt("pageNumber", 1);
	int pageSize = getParaToInt("pageSize", 10);
	String name = getPara("name");

	SysUser sysUser = new SysUser();
	sysUser.set("real_name", name);

	Page<SysUser> userPage = SysUserService.me().findPage(sysUser, pageNumber, pageSize);
	renderJson(new DataTable<SysUser>(userPage));
    }

    /**
     * 新增用户界面
     */
    public void userAdd()
    {
	List<SysRole> roleList = SysRoleService.me().roleList();
	this.setAttr("roleList", roleList);
	this.render("/sysadmin/user/useradd.html");
    }

    /**
     * 新增用户
     */
    public void userAddSave()
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
	int u_id = SysKeyIdService.me().generateKeyMysql("sys_user");
	String userCode = this.getPara("userCode");
	String userPwd = this.getPara("userPwd");
	String realName = this.getPara("realName");
	String sex = this.getPara("sex");
	String userDuty = this.getPara("userDuty");
	String userMob = this.getPara("userMob");
	String userEmail = this.getPara("userEmail");
	String userMemo = this.getPara("userMemo");
	String roleId = this.getPara("roleId");
	Map<String, String> params = new HashMap<String, String>();
	params.put("user_id", String.valueOf(u_id));
	params.put("userCode", userCode);
	params.put("userPwd", userPwd);
	params.put("realName", realName);
	params.put("sex", sex);
	params.put("userDuty", userDuty);
	params.put("userMob", userMob);
	params.put("userEmail", userEmail);
	params.put("userMemo", userMemo);
	JSONObject josn = new JSONObject();
	SysUser sysUser = SysUserService.me().getByUserCode(userCode);
	if (sysUser != null)
	{
	    josn.put("msg", "账号已存在，请重新输入账号");
	    this.renderJson(josn);
	    return;
	}

	int r_id = SysKeyIdService.me().generateKeyMysql("sys_user_role");
	boolean userAdd = SysUserService.me().userAdd(params, cook_userid);
	if (userAdd)
	{
	    SysUser sysUsers = SysUserService.me().getByUserCode(userCode);
	    String uid = sysUsers.get("user_id");
	    boolean user_role_add = SysUserService.me().user_role_add(String.valueOf(r_id), uid, roleId, cook_userid);
	    if (user_role_add)
	    {
		this.renderJson("msg", "新增成功");
		log.info("----用戶新增成功----");
	    } else
	    {
		SysUserRole.dao.deleteById(r_id);
		this.renderJson("msg", "服务器异常");
		log.error("----用戶角色新增時，服務器異常----");
	    }

	} else
	{
	    this.renderJson("msg", "服务器异常");
	    log.error("----用戶新增時，服務器異常----");
	}

    }

    /**
     * 编辑用户界面
     */
    public void userEdit()
    {
	String userId = this.getPara("userId");
	String pageNumber = this.getPara("pageNumber");
	SysUser sysUser = SysUserService.me().getById(userId);
	this.setAttr("sysUser", sysUser);
	SysUserRole sysUserRole = SysUserRoleService.me().getByUserId(userId);
	if (null != sysUserRole)
	{
	    SysRole sysRole = SysRoleService.me().getById(sysUserRole.getStr("role_id"));
	    this.setAttr("sysRole", sysRole);
	}
	this.setAttr("sysUserRole", sysUserRole);
	List<SysRole> roleList = SysRoleService.me().roleList();
	this.setAttr("roleList", roleList);
	this.setAttr("pageNumber", pageNumber);
	this.render("/sysadmin/user/useredit.html");
    }

    /**
     * 编辑用户
     */
    public void userEditSave()
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
	String userId = this.getPara("userId");
	params.put("userId", userId);
	params.put("userCode", this.getPara("userCode"));
	params.put("userPwd", this.getPara("userPwd"));
	params.put("realName", this.getPara("realName"));
	params.put("sex", this.getPara("sex"));
	params.put("userDuty", this.getPara("userDuty"));
	params.put("userMob", this.getPara("userMob"));
	params.put("userEmail", this.getPara("userEmail"));
	params.put("userMemo", this.getPara("userMemo"));
	String roleId = this.getPara("roleId");
	SysUserService sysUserService = new SysUserService();
	if (roleId != null)
	{
	    boolean roleupdate = SysUserService.me().Saverole(userId, roleId, cook_userid);
	    boolean userEdit = sysUserService.userEdit(params, cook_userid);
	    if (roleupdate && userEdit)
	    {
		this.renderJson("msg", "更新成功");
		log.info("----修改用戶信息成功----");
	    } else
	    {
		this.renderJson("msg", "服务器异常");
		log.info("----修改用戶信息時，服務器異常----");
	    }

	}
    }

    /* *//**
	  * 删除用户
	  */
    /*
     * public void userDelete() { try {
     * SysUserService.me().userDelete(this.getPara("id"));
     * this.renderJson("msg","删除成功"); log.info("----用戶刪除成功----"); } catch
     * (Exception e) { e.printStackTrace(); this.renderJson("msg","服务器异常");
     * log.info("----用戶刪除時，服務器異常----"); } }
     */

    /**
     * 批量删除
     */
    public void delBatch()
    {
	for (String userId : this.getParaValues("userId"))
	{
	    SysUserService.me().userDelete(userId);
	}
	this.renderJson("msg", "刪除成功");
	log.info("----用户删除成功----");
    }

    /**
     * 编辑用户角色
     */
    public void userRoleEdit()
    {
	String userId = this.getPara("userId");
	String ss = "SELECT * FROM sys_user_role sur LEFT JOIN sys_role sr ON sr.role_id = sur.role_id WHERE sur.user_id=?";
	Record record = Db.findFirst(ss, userId);
	this.setAttr("record", record);
	String sql = "select * from sys_role";
	this.setAttr("roleList", Db.find(sql));
	this.render("/sysadmin/user/userroleedit.html");
    }

    /**
     * 编辑用户角色
     */
    @SuppressWarnings("unchecked")
    public void userRoleEditSave()
    {
	try
	{
	    String userRoleId = this.getPara("userRoleId");
	    String roleId = this.getPara("roleId");
	    String userId = this.getPara("userId");
	    Map<String,String> params = new HashMap<String,String>();
	    params.put("userRoleId", userRoleId);
	    params.put("roleId", roleId);
	    params.put("userId", userId);
	    SysUserRoleService.me().userRoleEdit(params);
	    this.renderJson("msg", "更新成功");
	} catch (Exception e)
	{
	    e.printStackTrace();
	    this.renderJson("msg", "服务器异常");
	}
    }

    /**
     * 初始化用户密码
     */
    public void userPwd()
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
	String userId = this.getPara("userId");
	params.put("userPwd", "123456");
	params.put("userId", userId);
	boolean userPwd = SysUserService.me().userPwd(params, cook_userid);
	if (userPwd)
	{
	    this.renderJson("msg", "初始化用户密码为123456");
	    log.info("----初始化用户密码成功----");
	} else
	{
	    this.renderJson("msg", "初始化用户失败");
	    log.error("----初始化用户密码失败----");
	}
    }

}
