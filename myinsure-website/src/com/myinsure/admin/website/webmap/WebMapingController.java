package com.myinsure.admin.website.webmap;

import com.myinsure.interceptor.AdminInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
/**
 * 文章——栏目——专题对应关系管理
 * 
 * @author Administrator
 * 
 */
@Before(
{ AdminInterceptor.class })
public class WebMapingController extends Controller
{
	Log log = Log.getLog(WebMapingController.class);

}
