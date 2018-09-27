package com.myinsure.common;

import com.myinsure.admin.fun.SysFunController;
import com.myinsure.admin.layout.AdminController;
import com.myinsure.admin.login.LoginController;
import com.myinsure.admin.logs.SyslogsController;
import com.myinsure.admin.module.SysModuleController;
import com.myinsure.admin.role.SysRoleController;
import com.myinsure.admin.ueditor.UeditorController;
import com.myinsure.admin.user.SysUserController;
import com.myinsure.admin.website.article.ArticleController;
import com.myinsure.admin.website.carousel.WebImageController;
import com.myinsure.admin.website.company.WebCompanyController;
import com.myinsure.admin.website.html.GenerateHtml;
import com.myinsure.admin.website.institution.IinstitutionController;
import com.myinsure.admin.website.web.WebConfigController;
import com.myinsure.admin.website.webmap.WebMapingController;

import com.jfinal.config.Routes;
/**
 * 系统后台路由配置
 * 
 * @author sxf
 * 
 *         2017-5-13
 */
public class AdminRoute extends Routes
{
    @Override
    public void config()
    {
    add("/sysadmin/ueditor", UeditorController.class);
	add("/sysadmin", LoginController.class);
	add("/sysadmin/main", AdminController.class);
	add("/sysadmin/user", SysUserController.class);
	add("/sysadmin/module", SysModuleController.class);
	add("/sysadmin/role", SysRoleController.class);
	add("/sysadmin/fun", SysFunController.class);
	add("/sysadmin/logs", SyslogsController.class);
	add("/sysadmin/website/web", WebConfigController.class);
	add("/sysadmin/website/article", ArticleController.class);
	add("/sysadmin/website/mapping", WebMapingController.class);
	add("/sysadmin/website/company", WebCompanyController.class);
	add("/sysadmin/website/carousel", WebImageController.class);
	add("/sysadmin/website/generate", GenerateHtml.class);
	add("/sysadmin/website/inst", IinstitutionController.class);
    }

}
