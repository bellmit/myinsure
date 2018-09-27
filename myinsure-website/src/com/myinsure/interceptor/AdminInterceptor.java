package com.myinsure.interceptor;

import com.myinsure.admin.user.SysUser;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 后台登录拦截器
 * 
 * 当后台地址输错的时候，直接跳转到前台首页 当后台地址输对且用户为空的时候，跳转到后台登录页面 当后台地址输对且用户不为空的时候，跳转到管理后台首页
 * 
 * @author zxkj
 * 
 */
public class AdminInterceptor implements Interceptor
{

	public void intercept(Invocation inv)
	{
		Controller controller = inv.getController();

		// 1.首先判断sesion中有没有用户
		// SysUser user = controller.getSessionAttr("user");
		SysUser user = InterUtils.tryToGetUser(inv);

		if (user != null)
		{
			controller.setAttr("USER", user);
			controller.setAttr("user_id", user.get("user_id"));
		}
		inv.invoke();
	}
}
