package com.myinsure.admin.website.company;

import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;
import com.myinsure.admin.base.SysKeyIdService;
import com.myinsure.utils.DateUtil;
/**
 * 保险公司
 * @author 卢飞
 */
public class WebCompanyService
{
	private static final WebCompanyService rnsCompanyService = new WebCompanyService();
	public static WebCompanyService me()
	{
		return rnsCompanyService;
	}
	/**
	 * 查询所有保险公司
	 * @return
	 */
	public List<WebCompany> getAllCompany(){
		String sql = "select * from rns_company";
		List<WebCompany> list = WebCompany.dao.find(sql);
		return list;
	}
	/**
	 * 分页查询保险公司
	 */
	public Page<WebCompany> getCompanys(Integer pageNum){
		String sql = " from rns_company order by company_id desc";
		Page<WebCompany> page = WebCompany.dao.paginate(pageNum, 50, "select * ", sql);
		return page;
	}
	/**
	 * 根据id查询保险公司信息
	 * @param companyId
	 * @return
	 */
	public WebCompany getCompanyById(String companyId){
		WebCompany company = WebCompany.dao.findById(companyId);
		return company;
	}
	/**
	 * 保存保险公司信息
	 * @param parmas
	 * @return
	 */
	public boolean saveCompany(Map<String, String> params){
		WebCompany company = getCompanyById(params.get("companyId"));
		boolean flag = false;
		if(company == null){
			company = new WebCompany();
			SysKeyIdService sysKeyIdService = new SysKeyIdService();
			int companyId = sysKeyIdService.generateKeyMysql("rns_company");
			company.set("company_id", companyId);
			company.set("company_url", params.get("companyUrl") + "?companyId=" + companyId);
			company.set("create_time", DateUtil.getTodaySec());
			flag = true;
		}
		company.set("company_name", params.get("companyName"));
		company.set("claims_phone", params.get("claimsPhone"));
		company.set("company_logo", params.get("companyLogo"));
//		company.set("test_url", params.get("testUrl"));
		company.set("company_introduce", params.get("companyIntroduce"));
		company.set("update_time", DateUtil.getTodaySec());
		boolean b = false;
		if(flag){
			b = company.save();
		}else{
			company.set("company_url", params.get("companyUrl") + "?companyId=" + params.get("companyId"));
			b = company.update();
		}
		return b;
	}
	/**
	 * 删除保险公司
	 * @param companyId
	 * @return
	 */
	public boolean delCompany(String companyId){
		boolean b = WebCompany.dao.deleteById(companyId);
		return b;
	}
	
}









