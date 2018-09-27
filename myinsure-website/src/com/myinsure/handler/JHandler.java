package com.myinsure.handler;

import com.jfinal.handler.Handler;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.myinsure.admin.fun.SysFun;
import com.myinsure.admin.fun.SysFunService;
import com.myinsure.admin.logs.Syslogs;
import com.myinsure.admin.user.SysUser;
import com.myinsure.admin.user.SysUserService;
import com.myinsure.utils.DateUtil;

public class JHandler extends Handler
{
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean [] isHandled)
    {
		// String CPATH1 =
		// OptionQuery.me().findValue("web_domain")+request.getContextPath();//网络
		String CPATH = request.getContextPath();// 本地
		String clientsideName = request.getRemoteHost();// 获取客户端的主机名
		String clientsideIp = request.getRemoteAddr();// 获取客户端的ip
		Cookie[] cook_user = request.getCookies();
		String cook_userid = "";
		if (cook_user != null)
		{

			for (int i = 0; i < cook_user.length; i++)
			{
				Cookie cookie = cook_user[i];
				if (cookie.getName().equals("user_id"))
				{
					cook_userid = cookie.getValue();
				}
			}
		}
		String userRole = "";//用户角色
		String realName = "";
		if(!cook_userid.equalsIgnoreCase("")){//如果操作用户不为空
			SysUser sysUser = SysUserService.me().getByUserId(cook_userid.substring(63, cook_userid.length()));
			userRole = sysUser.getStr("role_name");
			realName = sysUser.getStr("real_name");
		}
		HttpSession session = request.getSession();
		String begUrl = request.getRequestURL().toString();// 请求的url
		String methodName = request.getServletPath();// 方法名
		String funName = "";//操作的菜单路径名称
		int index = methodName.indexOf("sysadmin");
		if(index > 0 && methodName.length() > (index + "sysadmin".length() + 7)){//如果是后端操作
			String funUrl = methodName.substring(index + "sysadmin".length() + 1);//操作的与数据库对应的链接
			SysFun fun = SysFunService.me().getFunByUrl(funUrl);
			if(fun != null){
				funName = fun.getStr("module_name") + "-" + fun.getStr("fun_name");
			}
		}
		String begType = request.getMethod();// 请求参数类型
		String sessionid = request.getRequestedSessionId();

		request.setAttribute("cxt", CPATH);
		request.setAttribute("sys_cxt", CPATH + "/sysadmin");
		this.next.handle(target, request, response, isHandled);
		
		if (!begUrl.substring(begUrl.length() - 4, begUrl.length() - 3).equals(".") && !begUrl.substring(begUrl.length() - 3, begUrl.length() - 2).equals("."))
		{
			if(!"".equalsIgnoreCase(funName)){
				Syslogs log = new Syslogs();
				log.set("ids", sessionid != null ? sessionid : "");
				log.set("cookid", cook_userid);
				log.set("user_id", cook_userid != null && cook_userid != "" ? cook_userid.substring(63, cook_userid.length()) : "");
				log.set("real_name", realName);//用户名称
				log.set("role_name", userRole);//用户角色
				log.set("clientsideName", clientsideName);
				log.set("clientsideIp", clientsideIp);
				log.set("begUrl", begUrl);
				log.set("methodName", methodName);
				log.set("fun_name", funName);
				log.set("begType", begType);
				log.set("establishTime", DateUtil.getTodaySec());
				log.save();
			}
			//redis缓存
//			Cache bbsCache = Redis.use("bbs");
//			JSONObject json = new JSONObject();
//			json.put("cookid", cook_userid);
//			json.put("user_id", cook_userid != null && cook_userid != "" ? cook_userid.substring(63, cook_userid.length()) : "");
//			json.put("clientsideName", clientsideName);
//			json.put("clientsideIp", clientsideIp);
//			json.put("begUrl", begUrl);
//			json.put("methodName", methodName);
//			json.put("begType", begType);
//			json.put("establishTime", DateUtil.getTodaySec());
//			bbsCache.lpush("logs", json);
//			List list = bbsCache.lrange("logs", 0, 0);
//			String logs = list.toString();
//			List<SysLogs> logses = JSON.parseArray(logs, SysLogs.class);
//			System.out.println("list=========" + list);
		}
	}

}