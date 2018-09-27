package com.myinsure.admin.layout;

import java.util.List;

import com.myinsure.admin.fun.SysRoleFun;
import com.myinsure.admin.fun.SysRoleFunService;
import com.myinsure.admin.module.SysModule;
import com.myinsure.admin.module.SysModuleService;
import com.myinsure.admin.role.SysUserRole;
import com.myinsure.admin.role.SysUserRoleService;
import com.myinsure.admin.user.SysUser;
import com.myinsure.admin.user.SysUserVO;
import com.myinsure.interceptor.AdminInterceptor;
import com.myinsure.utils.CookieUtils;
import com.myinsure.utils.Password;

import org.json.JSONArray;
import org.json.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Record;

/**
 * 后台登陆，加载主页哥哥模块
 * 
 */
@Before(AdminInterceptor.class)
public class AdminController extends Controller
{
    Log log = Log.getLog(AdminController.class);

    /**
     * 框架头部
     */
    public void backindex()
    {
		List<SysModule> moduleList = SysModuleService.me().moduleList();//所有的一级模块，在顶部显示
		this.setAttr("moduleList", moduleList);
		render("/sysadmin/backindex.html");
    }

    /**
     * 返回json格式 { "title": "基本元素", "icon": "fa-cubes", "spread": true,
     * "children": [ { "title": "下按钮", "icon": "&#xe641;", "href": "button.html"
     * } ] } 2017-5-10
     */
    public void leftFunJson()
    {
	String user_id = CookieUtils.get(this, "user_id");
	JSONArray dataarray = new JSONArray();
	
	String moduleId = this.getPara("moduleId") == null?"1000":this.getPara("moduleId");//要查询的一级模块id

	// 判断用户是否为空，如果用户没有登录
	// 这里增加用户role_fun 的验证，有该权限的显示fun链接，无就不显示//
	SysUserRole userRole = SysUserRoleService.me().getByUserId(user_id);

	if (userRole != null)
	{
	    String roleId = userRole.getStr("role_id");
	    
	    List<SysModule> moduleList = SysModuleService.me().moduleList(moduleId);

	    // 查询模块列表，根据模块列表，查询fun
	    for (int i = 0; i < moduleList.size(); i++)
	    {
		JSONObject jsonobj = new JSONObject();
		String module_id = moduleList.get(i).get("module_id");
		List<SysRoleFun> rolelist = SysRoleFunService.me().roleFunList(roleId, module_id);
		if (rolelist.size() > 0)
		{
		    jsonobj.put("title", moduleList.get(i).get("module_name"));
		    jsonobj.put("icon", moduleList.get(i).get("icons"));
		    jsonobj.put("spread", "true");
		    JSONArray jsonarray = new JSONArray();
		    for (int j = 0; j < rolelist.size(); j++)
		    {
			JSONObject jsonsub = new JSONObject();
			jsonsub.put("title", rolelist.get(j).get("fun_name"));
			// jsonsub.put("icon", rolelist.get(j).get("icon"));
			jsonsub.put("href", rolelist.get(j).get("url"));
			jsonarray.put(jsonsub);
		    }
		    jsonobj.put("children", jsonarray);
		    dataarray.put(jsonobj);
		}

	    }

	} else
	{

	    renderText("该用户没有赋权！");
	    renderJson(dataarray);
	}

	renderHtml(dataarray.toString());
    }

    /**
     * 框架左边
     */
    public void left()
    {
	SysUserVO uservo = this.getSessionAttr("user");
	List<Record> moduleList = SysLeftNavService.me().getModuleList(uservo.getUserId());
	this.setAttr("moduleList", moduleList);
	render("/sysadmin/common/left.jsp");
    }

    /**
     * 左边fun
     */
    public void getLeftFun()
    {
	try
	{
	    SysUserVO uservo = this.getSessionAttr("user");
	    List<Record> funList = SysLeftNavService.me().getFunList(uservo.getUserId());// 用户有权限且显示的所有页面
	    this.renderJson(funList);
	    List<Record> funListAll = SysLeftNavService.me().getAllFunList(uservo.getUserId());// 用户有权限操作的所有页面,权限设置
	    this.setSessionAttr("userFunList", funListAll);
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * 框架底部
     */
    public void bottom()
    {
	render("/sysadmin/common/bottom.jsp");
    }

    /**
     * 框架欢迎
     */
    public void main()
    {
	render("/sysadmin/main.html");
    }

    /**
     * 获取管理账号
     */
    public void getadmin()
    {
	String user_id = CookieUtils.get(this, "user_id");
	SysUser user = SysUser.dao.findById(user_id);
	setAttr("sys_user", user);
	render("/sysadmin/userSelf.html");
    }

    /**
     * 修改管理账号密码
     */
    public void updatePassword()
    {
	String user_id = getPara("user_id") == null ? "" : getPara("user_id");
	String password = getPara("user_pwd") == null ? "" : getPara("user_pwd");
	if (!password.equals(""))
	{
	    SysUser user = SysUser.dao.findFirst("select * from sys_user where user_id=?", user_id);
	    password = Password.md5(password);
	    user.set("user_pwd", password);
	    boolean isok = user.update();
	    if (isok)
	    {
		renderJson("msg", "密码修改成功!");
		log.info("-----密码修改成功----");
	    } else
	    {
		renderJson("msg", "密码修改失败!");
		log.error("-----密码修改失败----");
	    }
	} else
	{
	    renderJson("msg", "密码未修改!");
	}
	forwardAction("/sysadmin/getadmin");
    }

}
