package com.myinsure.admin.login;

import com.myinsure.admin.user.SysUser;
import com.myinsure.interceptor.AdminInterceptor;
import com.myinsure.utils.CookieUtils;
import com.myinsure.utils.Password;
import com.myinsure.utils.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;

/**
 * 登陆处理
 */
@Before({ AdminInterceptor.class })
public class LoginController extends Controller
{

    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(LoginController.class);

    /**
     * 准备登陆
     */
    public void index()
    {
	render("/sysadmin/login.html");
    }

    /**
     * 后台首页
     */
    public void backindex()
    {
	String cpath = this.getRequest().getContextPath();
	setAttr("cxt", cpath);
	render("/sysadmin/backindex.html");
    }

    /**
     * 登陆验证
     */
    @Clear({ AdminInterceptor.class })
    public void alogin()
    {
	String username = getPara("username");
	String password = getPara("password");

	if (!StringUtils.areNotEmpty(new String[] { username, password }))
	{
	    render("/sysadmin/login.html");
	    return;
	}
	SysUser user = SysUser.dao.findFirst("select * from sys_user where user_code='" + username + "' and is_del = 1");

	String md5pass = Password.md5(password);
	if (null != user && md5pass.equals(user.getStr("user_pwd")))
	{
	    System.out.println(user.getStr("user_id"));
	    CookieUtils.put(this, "user_id", user.getStr("user_id"));

	    this.setSessionAttr("user", user);
//	    render("/sysadmin/backindex.html");
	    redirect("/sysadmin/main/backindex");
	    return;
	} else
	{
	    setAttr("msg", "登录失败！");
	    render("/sysadmin/login.html");
	    return;
	}
    }

    @Clear(AdminInterceptor.class)
    public void logout()
    {
	CookieUtils.remove(this, "user_id");
	redirect("/sysadmin");
    }

}
