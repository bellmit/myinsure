package com.myinsure.admin.fun;

import java.util.List;
import java.util.Map;

import com.myinsure.admin.base.SysKeyIdService;
import com.myinsure.utils.DateUtil;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;

public class SysRoleFunService
{

    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(SysRoleFunService.class);// 日志

    // 单例模式
    private static final SysRoleFunService sysRoleFunService = new SysRoleFunService();

    public static SysRoleFunService me()
    {
	return sysRoleFunService;
    }

    /**
     * 角色权限功能列表
     * 
     * @param roleId
     * @return
     */

    public List<SysRoleFun> roleFunList(String roleId)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_role_fun srf left join sys_fun sf on srf.fun_id=sf.fun_id  where srf.role_id=?");
	List<SysRoleFun> result = SysRoleFun.dao.find(sql.toString(), roleId);
	return result;
    }

    public List<SysRoleFun> roleFunList(String roleId, String module_id)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_fun AS sy ");
	sql.append(" LEFT JOIN sys_module AS sm ON sy.module_id = sm.module_id");
	sql.append(" LEFT JOIN sys_role_fun AS srf ON sy.fun_id = srf.fun_id");
	sql.append(" LEFT JOIN sys_role AS sr ON sr.role_id = srf.role_id");
	sql.append(" WHERE sy.is_show=1 and sr.role_id =? and sm.module_id=?");
	sql.append(" order by sy.orders asc");
	List<SysRoleFun> result = SysRoleFun.dao.find(sql.toString(), roleId, module_id);
	return result;
    }

    /**
     * 角色所未拥有的功能列表
     * 
     * @param roleId
     * @return
     */
    public List<SysRoleFun> roleFunToAddList(String roleId)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT sf.fun_id,sf.fun_name,srf.role_id,sf.module_id FROM sys_fun sf LEFT JOIN (SELECT * from sys_role_fun  WHERE role_id=?) srf ON sf.fun_id=srf.fun_id  WHERE srf.role_id is null  ");
	List<SysRoleFun> result = SysRoleFun.dao.find(sql.toString(), roleId);
	return result;
    }

    /**
     * 新增角色权限功能
     * 
     * @param funIds
     * @param roleId
     * @return
     */
    public String roleFunAdd(String [] funIds, String roleId, String userid)
    {
	String sql = "delete from sys_role_fun where role_id=?";
	int update = Db.update(sql, roleId);
	if (null == funIds || funIds.length <= 0)
	{
	    return null;
	}

	for (String funId : funIds)
	{
	    boolean bolRoleFun;
	    SysRoleFun sysRoleFun = new SysRoleFun();
	    int role_fun_id = SysKeyIdService.me().generateKeyMysql("sys_role_fun");
	    sysRoleFun.set("role_fun_id", role_fun_id);
	    sysRoleFun.set("role_id", roleId);
	    sysRoleFun.set("fun_id", funId);
	    sysRoleFun.set("is_edit", 1);
	    sysRoleFun.set("is_print", 1);
	    sysRoleFun.set("is_audit", 1);
	    sysRoleFun.set("add_userid", userid);
	    sysRoleFun.set("add_date", DateUtil.getTodaySec());
	    bolRoleFun = sysRoleFun.save();
	    if (!bolRoleFun)
	    {
		return funId;

	    }
	}
	return null;
    }

    /**
     * 删除角色功能
     * 
     * @param roleFunId
     * @return
     */
    public String roleFunDelete(String roleFunId)
    {
	SysRoleFun sysRoleFun = SysRoleFun.dao.findById(roleFunId);
	sysRoleFun.delete();
	return null;
    }

    /**
     * 通过id获取角色功能
     * 
     * @param roleFunId
     * @return
     */
    public SysRoleFun getById(String roleFunId)
    {
	return SysRoleFun.dao.findById(roleFunId);
    }

    /**
     * 编辑角色权限功能
     * 
     * @param params
     * @return
     */
    public String roleFunEdit(Map params)
    {
	SysRoleFun sysRoleFun = SysRoleFun.dao.findById(params.get("roleFunId"));
	sysRoleFun.set("is_edit", params.get("isEdit"));
	sysRoleFun.set("is_print", params.get("isPrint"));
	sysRoleFun.set("is_audit", params.get("isAudit"));
	sysRoleFun.update();
	return null;
    }
}
