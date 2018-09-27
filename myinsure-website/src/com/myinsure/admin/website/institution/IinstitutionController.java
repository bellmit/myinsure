package com.myinsure.admin.website.institution;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class IinstitutionController extends Controller
{

	/**
	 * 查询分支机构
	 */
	public void getList(){
		Integer pageNumber = this.getPara("pageNumber") == null?1:this.getParaToInt("pageNumber");
		Page<Institution> page = InstitutionService.me().getInstitList(pageNumber);
		setAttr("list", page.getList());
		setAttr("pageNumber", pageNumber);
		setAttr("pageSize", page.getPageSize());
    	setAttr("totalPage", page.getTotalPage());
    	setAttr("totalRow", page.getTotalRow());
    	render("/sysadmin/website/institution/insList.html");
	}
	public void getLists(){
		int draw = this.getParaToInt("draw");
		int start = this.getParaToInt("start");
    	int length = this.getParaToInt("length");
    	String csrfmiddlewaretoken = this.getPara("csrfmiddlewaretoken");
    	int pageNum = start/length + 1;
		Page<Institution> page = InstitutionService.me().getInstitList(pageNum);
		setAttr("list", page.getList());
		setAttr("draw", draw);
		setAttr("pageNumber", pageNum);
		setAttr("pageSize", page.getPageSize());
    	setAttr("totalPage", page.getTotalPage());
    	setAttr("totalRow", page.getTotalRow());
    	renderJson();
	}
	
	/**
	 * 跳转修改页面
	 */
	public void update_page(){
		String id=getPara("id");
		Institution infoId = InstitutionService.me().getInfoId(id);
		setAttr("list", infoId);
		setAttr("id", id);
		setAttr("title", "修改机构信息");
		render("/sysadmin/website/institution/updateInfo.html");
	}
	
	/**
	 * 跳转添加页面
	 */
	public void save_page(){
		setAttr("title", "添加机构信息");
		render("/sysadmin/website/institution/updateInfo.html");
	}
	/**
	 * 添加修改分支机构
	 */
	public void update_save(){
		String id=getPara("id")==null?"":getPara("id");
		String name=getPara("name");
		String address=getPara("address");
		String phone=getPara("phone");
		String range=getPara("range");
		String controller=getPara("controller");
		
		Map map=new HashMap();
		map.put("id", id);
		map.put("name", name);
		map.put("address", address);
		map.put("phone", phone);
		map.put("range", range);
		map.put("controller", controller);
		
		boolean update_instit = InstitutionService.me().update_instit(map);
		if (update_instit)
		{
			renderJson("msg","成功");
		}else {
			renderJson("msg","失败");
		}
	}
	/**
	 * 删除
	 */
	public void dele(){
		String[] val = getParaValues("id");
		for (String id : val)
		{
			boolean	delete_instit= InstitutionService.me().delete_instit(id);
			if (!delete_instit)
			{
				renderJson("msg","失败");
				return;
			}else {
				renderJson("msg","成功");
			}
		}
		
		
	}
	/**
	 * 查询机构信息
	 */
	public void getInsInfo(){
		String province=getPara("province");
		Institution info = InstitutionService.me().getInfo(province);
		renderJson("val",info);
	}
	public void  sd(){
		render("/sysadmin/website/institution/111.html");
	}
}
