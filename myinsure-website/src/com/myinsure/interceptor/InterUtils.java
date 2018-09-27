package com.myinsure.interceptor;

import com.myinsure.admin.user.SysUser;
import com.myinsure.utils.CookieUtils;
import com.myinsure.utils.StringUtils;

import com.jfinal.aop.Invocation;

public class InterUtils
{
	public static SysUser tryToGetUser(Invocation inv)
	{
		String userId = CookieUtils.get(inv.getController(), "user_id");
		if (StringUtils.isNotBlank(userId))
		{
			return SysUser.dao.findById(userId);
		}

		return null;
	}
}