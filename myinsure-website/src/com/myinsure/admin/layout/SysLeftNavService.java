package com.myinsure.admin.layout;

import java.util.List;

import com.myinsure.admin.fun.SysFun;
import com.myinsure.admin.module.SysModule;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class SysLeftNavService
{

    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(SysLeftNavService.class);

    private static final SysLeftNavService sysLeftNavService = new SysLeftNavService();

    public static SysLeftNavService me()
    {
	return sysLeftNavService;
    }

    /**
     * 左侧功能
     * 
     * @param userId
     * @return
     */
    public List<Record> getFunList(String userId)
    {
	StringBuffer sql = new StringBuffer();
	sql.append(" SELECT sf.fun_id,sf.fun_name,sf.url,sf.module_id ");
	sql.append(" FROM sys_user su  ");
	sql.append(" LEFT JOIN sys_user_role sur ON  su.user_id=sur.user_id ");
	sql.append(" LEFT JOIN sys_role_fun srf ON sur.role_id=srf.role_id ");
	sql.append(" LEFT JOIN sys_fun sf ON srf.fun_id=sf.fun_id ");
	sql.append(" WHERE su.user_id=? ");
	sql.append(" and sf.is_show=1 ");
	sql.append(" ORDER BY sf.order ");
	List<Record> list = Db.find(sql.toString(), userId);
	return list;

    }

    /**
     * 用户所有权限
     * 
     * @param userId
     * @return
     */
    public List<Record> getAllFunList(String userId)
    {
	StringBuffer sql = new StringBuffer();
	sql.append(" SELECT sf.fun_id,sf.fun_name,sf.url,sf.module_id ");
	sql.append(" FROM sys_user su  ");
	sql.append(" LEFT JOIN sys_user_role sur ON  su.user_id=sur.user_id ");
	sql.append(" LEFT JOIN sys_role_fun srf ON sur.role_id=srf.role_id ");
	sql.append(" LEFT JOIN sys_fun sf ON srf.fun_id=sf.fun_id ");
	sql.append(" WHERE su.user_id=? AND sf.fun_id IS NOT NULL ");
	sql.append(" ORDER BY sf.order   ");
	List<Record> list = Db.find(sql.toString(), userId);
	return list;

    }

    /**
     * 获取系统模块
     */
    public List<Record> getModuleList(String userId)
    {
	StringBuffer sql = new StringBuffer();
	List<Record> funList = getFunList(userId);

	if (null == funList)
	{
	    return null;
	} else
	{
	    sql.append("select * from sys_module where 1=2 ");
	    for (Record record : funList)
	    {
		sql.append(" or module_id='" + record.getStr("module_id") + "' ");
	    }
	    sql.append(" order by order_numb desc  ");
	    return Db.find(sql.toString());
	}
    }

    // 查询模块下面功能
    public List<SysFun> getFun(String moduleId, String roleId)
    {
	String sql = "SELECT * FROM sys_fun sf ,sys_role_fun sr WHERE sf.module_id = ? and sf.fun_id =  sr.fun_id and  sr.role_id = ?  and sf.is_show= ? ORDER BY sf.order   ";
	List<SysFun> list = SysFun.dao.find(sql, moduleId, roleId, "1");
	return list;
    }

    public List<Record> getFunAll(String moduleId, String roleId)
    {
	String sql = "SELECT * FROM sys_fun sf ,sys_role_fun sr WHERE sf.module_id = ? and sf.fun_id =  sr.fun_id and  sr.role_id = ?  and sf.is_show= ? ORDER BY sf.order   ";
	List<Record> list = Db.find(sql, moduleId, roleId, "1");
	return list;
    }

    public SysModule getByModuleId(String moduleId)
    {
	String sql = "SELECT * FROM sys_module  WHERE module_id = ?    ";
	SysModule record = SysModule.dao.findFirst(sql, moduleId);
	return record;
    }
}
