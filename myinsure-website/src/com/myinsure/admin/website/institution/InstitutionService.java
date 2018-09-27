package com.myinsure.admin.website.institution;

import java.util.Map;

import com.jfinal.plugin.activerecord.Page;

/**
 * 分支机构管理
 * @author X T
 *
 */
public class InstitutionService
{
	private static final InstitutionService inse = new InstitutionService();

	public static final InstitutionService me()
	{
		return inse;
	}
	
	/**
	 * 查询所有分支机构
	 * @param pageNumber
	 * @return
	 */
	public Page<Institution> getInstitList(int pageNumber){
		StringBuffer sql=new StringBuffer();
		Page<Institution> paginate = Institution.dao.paginate(pageNumber, 10, "select * " , " from web_institution ");
		return paginate;
	}
	/**
	 * 修改保存分支机构
	 * @param map
	 * @return
	 */
	public boolean update_instit(Map map){
		boolean update;
		Institution findFirst = Institution.dao.findFirst("select * from web_institution where id='"+map.get("id")+"'");
		if (findFirst!=null&&!findFirst.equals(""))
		{
			findFirst.set("ins_name", map.get("name"));
			findFirst.set("ins_address", map.get("address"));
			findFirst.set("ins_phone", map.get("phone"));
			findFirst.set("ins_range", map.get("range"));
			findFirst.set("ins_controller", map.get("controller"));
			update = findFirst.update();
			return update;
		}else {
			Institution ins=new Institution();
			ins.set("ins_name", map.get("name"));
			ins.set("ins_address", map.get("address"));
			ins.set("ins_phone", map.get("phone"));
			ins.set("ins_range", map.get("range"));
			ins.set("ins_controller", map.get("controller"));
			update = ins.save();
			return update;
		}
	}
	/**
	 * 删除分支机构
	 * @param id
	 * @return
	 */
	public boolean delete_instit(String id){
		boolean deleteById = Institution.dao.deleteById(id);
		return deleteById;
	}
	/**
	 * 根据城市名查询
	 * @param province
	 * @return
	 */
	public Institution getInfo(String province){
		Institution findFirst = Institution.dao.findFirst("select * from web_institution where ins_name like'%"+province+"%'");
		return findFirst;
	}
	public Institution getInfoId(String id){
		Institution findById = Institution.dao.findById(id);
		return findById;
	}
}
