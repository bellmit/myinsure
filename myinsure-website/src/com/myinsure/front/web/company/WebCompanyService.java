package com.myinsure.front.web.company;

import java.util.List;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.myinsure.admin.website.company.WebCompany;
/**
 * 保险公司
 * @author 卢飞
 */
public class WebCompanyService
{
	@SuppressWarnings("unused")
	private static final Log log = Log.getLog(WebCompanyService.class);
	private static final WebCompanyService rnsCompanyService = new WebCompanyService();
	public static WebCompanyService me()
	{
		return rnsCompanyService;
	}
	/** 
	 * 查询合作的保险公司
	 * @return
	 */
	public List<WebCompany> getCompanies(){
		String sql = " from rns_company";
		Page<WebCompany> page = WebCompany.dao.paginate(1, 10, "select * ", sql);
		List<WebCompany> list = page.getList();
		return list;
	}
}









