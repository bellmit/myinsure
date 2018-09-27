package com.myinsure.admin.role;

import java.util.List;
import java.util.Map;

import com.myinsure.utils.DateUtil;
import com.myinsure.utils.StringUtils;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class SysRoleService
{
    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(SysRoleService.class);

    private static final SysRoleService sysRoleService = new SysRoleService();

    public static SysRoleService me()
    {
	return sysRoleService;
    }

    /**
     * 角色列表
     * 
     * @return
     */
    public List<SysRole> roleList()
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_role ");
	List<SysRole> result = SysRole.dao.find(sql.toString());
	return result;

    }

    /**
     * 分页查询角色列表
     * 
     * @param queryText
     * @param pageNumber
     * @return
     */
    public Page<SysRole> rolePage(String queryText, String pageNumber)
    {
	String where = "";
	if (StringUtils.isBlank(queryText))
	{
	    where = "from sys_role ";
	} else
	{
	    where = "from sys_role where role_name like '%" + queryText + "%' ";
	}
	Page<SysRole> rolePage = SysRole.dao.paginate(Integer.parseInt(pageNumber), 8, "select *", where);
	return rolePage;
    }

    /**
     * 新增角色
     * 
     * @param params
     * @return
     */
    public boolean roleAdd(Map params, String cook_userid)
    {
	boolean bol;
	SysRole sysRole = new SysRole();
	sysRole.set("role_id", params.get("role_id"));
	sysRole.set("role_name", params.get("roleName"));
	sysRole.set("role_memo", params.get("roleMemo"));
	sysRole.set("add_userid", cook_userid);
	sysRole.set("add_time", DateUtil.getTodaySec());
	bol = sysRole.save();
	return bol;

    }

    /**
     * 删除角色
     * 
     * @param id
     * @return
     */
    public void roleDelete(String id)
    {
	SysRole sysRole = SysRole.dao.findById(id);
	sysRole.delete();
	// 删除角色的权限
	String sql = "delete from sys_role_fun where role_id='" + id + "'";
	Db.update(sql);

	// 删除用到此角色的用户
	String sqll = "delete from sys_user_role where role_id='" + id + "'";
	Db.update(sqll);
    }

    /**
     * 通过id获取角色
     * 
     * @param id
     * @return
     */
    public SysRole getById(String id)
    {
	SysRole sysRole = SysRole.dao.findById(id);
	return sysRole;
    }

    /**
     * 编辑角色
     * 
     * @param params
     * @return
     */
    public boolean roleEdit(Map params)
    {
	boolean bolroleEdit;
	SysRole sysRole = SysRole.dao.findById(params.get("id"));
	sysRole.set("role_name", params.get("roleName"));
	sysRole.set("role_memo", params.get("roleMemo"));
	sysRole.set("update_time", DateUtil.getTodaySec());
	bolroleEdit = sysRole.update();
	return bolroleEdit;
    }
}
