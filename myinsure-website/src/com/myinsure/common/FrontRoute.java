package com.myinsure.common;

import com.jfinal.config.Routes;
import com.myinsure.front.web.WebController;

/**
 * 微信保险商城管理
 * @author sxf
 * 2017-5-13
 */

public class FrontRoute extends Routes
{

    @Override
    public void config()
    {
    	add("/front", WebController.class);
    }

}
