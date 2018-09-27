package com.myinsure.admin.user;

import java.util.Map;

import com.myinsure.admin.role.SysUserRole;
import com.myinsure.admin.role.SysUserRoleService;
import com.myinsure.utils.DateUtil;
import com.myinsure.utils.Password;
import com.myinsure.utils.StringUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 该service方法用来处理用户的操作
 * 
 * @author sxf 2014-7-16
 * 
 */

public class SysUserService
{
    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(SysUserService.class);

    private static final SysUserService sysUserService = new SysUserService();

    public static SysUserService me()
    {
	return sysUserService;
    }

    /**
     * 根据 关键字和页码查询用户
     * 
     * @param word
     * @param pageNumber
     * @return
     */
    public Page<SysUser> searchUser(String word, int pageNumber)
    {
	String select = " SELECT su.*,sr.role_id,sr.role_name  ";
	StringBuffer where = new StringBuffer();
	if (StringUtils.isBlank(word))
	{
	    where.append(" FROM sys_user su ");
	    where.append(" LEFT JOIN sys_user_role sur ON sur.user_id=su.user_id ");
	    where.append(" LEFT JOIN sys_role sr ON sr.role_id=sur.role_id ");
	    where.append(" where  is_del = 1 ORDER  BY sr.ordunm desc");
	} else
	{
	    where.append(" FROM sys_user su ");
	    where.append(" LEFT JOIN sys_user_role sur ON sur.user_id=su.user_id ");
	    where.append(" LEFT JOIN sys_role sr ON sr.role_id=sur.role_id ");
	    where.append(" where  is_del = 1 ");
	    where.append(" and (user_name like '%?%' ");
	    where.append(" or user_code like '%?%' ) ");
	    where.append(" ORDER  BY sr.ordunm desc");
	}
	Page<SysUser> userpage = SysUser.dao.paginate(pageNumber, 8, select, where.toString());
	return userpage;
    }

    /**
     * 通过用户账号获取用户
     * 
     * @param userCode
     * @return
     */
    public SysUser getByUserCode(String userCode)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_user where user_code=? and is_del = 1 ");
	SysUser sysUser = SysUser.dao.findFirst(sql.toString(), userCode);
	return sysUser;
    }
    /**
     * 分页查询系统用户信息
     * @param sysUser
     * @return
     */
    public Page<SysUser> findPage(SysUser user, int pageNumber, int pageSize) {

	String select = "select *  ";
	StringBuffer where = new StringBuffer();
        if (StrKit.notBlank(user.getStr("real_name)"))) {
            where.append(" from sys_user where real_name like '%"+user.getStr("real_name")+"%' ");
        }else{
            where.append(" from sys_user" );
        }
        Page<SysUser> userpage = SysUser.dao.paginate(pageNumber, pageSize, select, where.toString());
        return userpage;
    }

    /**
     * 新增用户
     * 
     * @param params
     * @return
     */
    public boolean userAdd(Map<String,String> params, String userid)
    {
	SysUser sysUser = new SysUser();
	sysUser.set("user_id", params.get("user_id"));
	sysUser.set("user_code", params.get("userCode"));
	sysUser.set("user_pwd", Password.md5((String) params.get("userPwd")));
	sysUser.set("real_name", params.get("realName"));
	sysUser.set("sex", params.get("sex"));
	sysUser.set("duty", params.get("userDuty"));
	sysUser.set("mob", params.get("userMob"));
	sysUser.set("email", params.get("userEmail"));
	sysUser.set("user_memo", params.get("userMemo"));// 备注
	sysUser.set("create_user_id", userid);
	sysUser.set("create_time", DateUtil.getTodaySec());
	boolean boluser = sysUser.save();
	return boluser;
    }

    /**
     * 给新用户添加角色
     */
    public boolean user_role_add(String id, String uid, String rid, String cid)
    {
	SysUserRole user_role = new SysUserRole();
	user_role.set("user_role_id", id);
	user_role.set("user_id", uid);
	user_role.set("role_id", rid);
	user_role.set("add_date", DateUtil.getTodaySec());
	user_role.set("add_userid", cid);
	boolean bol = user_role.save();
	return bol;
    }

    /**
     * 通过用户id获取用户
     * 
     * @param userId
     * @return
     */
    public SysUser getById(String userId)
    {
	SysUser sysUser = SysUser.dao.findById(userId);
	return sysUser;
    }

    /**
     * 通过用户id删除用户
     */
    public void userDelete(String userId)
    {

	SysUser sysUser = SysUser.dao.findById(userId);
	// 删除用户
	if (null != sysUser)
	{
	    sysUser.set("is_del", 0);
	    sysUser.update();
	    SysUserRoleService SysUserRoleService = new SysUserRoleService();
	    SysUserRoleService.userRoleDeleteByUserId(userId);
	}

    }

    /**
     * 编辑用户
     * 
     * @param params
     * @return
     */
    public boolean userEdit(Map<String, String> params, String userid)
    {
	SysUser sysUser = SysUser.dao.findById(params.get("userId"));
	if (StringUtils.isNotEmpty((String) params.get("userPwd")))
	{
	    sysUser.set("user_pwd", Password.md5((String) params.get("userPwd")));
	}
	sysUser.set("real_name", params.get("realName"));
	sysUser.set("sex", params.get("sex"));
	sysUser.set("duty", params.get("userDuty"));
	sysUser.set("mob", params.get("userMob"));
	sysUser.set("email", params.get("userEmail"));
	sysUser.set("user_memo", params.get("userMemo"));// 备注
	sysUser.set("update_user_id", userid);
	sysUser.set("update_time", DateUtil.getTodaySec());
	boolean bol = sysUser.update();
	return bol;
    }

    // 用户设置角色
    public boolean Saverole(String userId, String roleId, String cid)
    {
	boolean bol = false;
	String sql = "select * from sys_user_role where user_id= ? ";
	if (StringUtils.isBlank(userId))
	{
	    return bol;
	}
	SysUserRole sysUserRole = new SysUserRole();
	Record record = Db.findFirst(sql, userId);

	if (null != record)
	{
	    // 修改角色
	    if (StringUtils.isNotEmpty(roleId))
	    {
//		String sqlString = "selec * from sys_user_role where user_id=?";
		SysUserRole userRole = sysUserRole.dao().findFirst(sql, userId);
		bol = true;
		if (!userRole.get("role_id").equals(roleId))
		{
		    userRole.set("role_id", roleId);
		    userRole.set("update_userid", cid);
		    userRole.set("update_time", DateUtil.getTodaySec());
		    bol = userRole.update();
		}

		return bol;
	    } else
	    {
		// 如果没有角色，则删除角色。
		SysUserRole userRole = sysUserRole.dao().findFirst(sql, userId);
		bol = userRole.delete();
		return bol;
	    }
	} else
	{
	    sysUserRole.set("user_id", userId);
	    sysUserRole.set("role_id", roleId);
	    bol = sysUserRole.save();
	    return bol;
	}
    }

    /**
     * 后台增加医生同时 同步数据到sys_user表
     * 
     * @param params
     * @return
     */
    public boolean doctorUserAdd(Map<String,String> params)
    {
	boolean bool = false;
	SysUser sysUser = new SysUser();
	sysUser.set("user_id", params.get("user_id"));
	sysUser.set("user_code", params.get("user_code"));
	sysUser.set("user_pwd", Password.md5((String) params.get("user_pwd")));
	sysUser.set("real_name", params.get("real_name"));
	bool = sysUser.save();
	return bool;
    }

    /**
     * 初始化用户密码
     * 
     * @param params
     * @return
     */
    public boolean userPwd(Map<String, String> params, String userid)
    {
	SysUser sysUser = SysUser.dao.findById(params.get("userId"));
	if (StringUtils.isNotEmpty((String) params.get("userPwd")))
	{
	    sysUser.set("user_pwd", Password.md5((String) params.get("userPwd")));
	    sysUser.set("update_user_id", userid);
	    sysUser.set("update_time", DateUtil.getTodaySec());
	}
	boolean boluser = sysUser.update();
	return boluser;
    }
    /**
     * 根据后台用户id查询用户角色
     * @param userId
     * @return
     */
    public SysUser getByUserId(String userId){
    	String sql = " select * from sys_user su "
    			   + " left join sys_user_role sur on su.user_id = sur.user_id "
    			   + " left join sys_role sr on sr.role_id = sur.role_id "
    			   + " where su.user_id = " + userId;
    	SysUser sysUser = SysUser.dao.findFirst(sql);
    	return sysUser;
    }
}


