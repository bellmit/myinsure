package com.myinsure.admin.role;

import java.util.Map;

import com.jfinal.log.Log;

public class SysUserRoleService
{
    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(SysUserRoleService.class);

    private static final SysUserRoleService sysUserRoleService = new SysUserRoleService();

    public static SysUserRoleService me()
    {
	return sysUserRoleService;
    }

    /**
     * 通过id获取用户角色
     * 
     * @param userId
     * @return
     */
    public SysUserRole getByUserId(String userId)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_user_role where user_id=?");
	SysUserRole sysUserRole = SysUserRole.dao.findFirst(sql.toString(), userId);
	return sysUserRole;
    }

    /**
     * 编辑用户角色
     * 
     * @param params
     * @return
     */
    public String userRoleEdit(Map params)
    {
	SysUserRole sysUserRole = SysUserRole.dao.findById(params.get("userRoleId"));
	if (null != sysUserRole)
	{
	    sysUserRole.set("role_id", params.get("roleId"));
	    sysUserRole.update();
	} else
	{
	    SysUserRole sysUserRoleAdd = new SysUserRole();
	    sysUserRoleAdd.set("user_id", params.get("userId"));
	    sysUserRoleAdd.set("role_id", params.get("roleId"));
	    sysUserRoleAdd.save();
	}

	return null;
    }

    /**
     * 通过id删除用户角色
     * 
     * @param userId
     * @return
     */
    public String userRoleDeleteByUserId(String userId)
    {
	SysUserRole sysUserRole = getByUserId(userId);
	if (null == sysUserRole)
	{
	    return null;
	} else
	{
	    // 删除用户下的角色
	    sysUserRole.delete();
	}
	return null;
    }

    /**
     * 后台增加医生同时 同步数据到sys_user表 默认医生权限
     * 
     * @param params
     * @return
     */
    public boolean doctorUserRoleAdd(Map params)
    {
	boolean bool = false;
	SysUserRole sysUserRole = new SysUserRole();
	sysUserRole.set("user_role_id", params.get("user_role_id"));
	sysUserRole.set("user_id", params.get("user_id"));
	sysUserRole.set("role_id", "6");
	bool = sysUserRole.save();
	return bool;
    }
}
