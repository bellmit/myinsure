package com.myinsure.admin.login;

import com.jfinal.log.Log;

public class LoginService
{

    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(LoginService.class);

    private static final LoginService loginService = new LoginService();

    public static LoginService me()
    {
	return loginService;
    }
}
