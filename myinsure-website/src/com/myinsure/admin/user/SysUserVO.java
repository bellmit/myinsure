package com.myinsure.admin.user;

import java.io.Serializable;

public class SysUserVO implements Serializable
{

	private static final long serialVersionUID = 7419139385426446738L;

	private String userId;
	private String userCode;
	private String userName;
	private String userPwd;
	private String sex;
	private String duty;
	private String mob;
	private String email;
	private String isvalid;
	private String signPc;
	private String roleId; // 用户角色
	private String d_id;

	public String getD_id()
	{
		return d_id;
	}

	public void setD_id(String d_id)
	{
		this.d_id = d_id;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getUserCode()
	{
		return userCode;
	}

	public void setUserCode(String userCode)
	{
		this.userCode = userCode;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserPwd()
	{
		return userPwd;
	}

	public void setUserPwd(String userPwd)
	{
		this.userPwd = userPwd;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public String getDuty()
	{
		return duty;
	}

	public void setDuty(String duty)
	{
		this.duty = duty;
	}

	public String getMob()
	{
		return mob;
	}

	public void setMob(String mob)
	{
		this.mob = mob;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getIsvalid()
	{
		return isvalid;
	}

	public void setIsvalid(String isvalid)
	{
		this.isvalid = isvalid;
	}

	public String getSignPc()
	{
		return signPc;
	}

	public void setSignPc(String signPc)
	{
		this.signPc = signPc;
	}

	public String getRoleId()
	{
		return roleId;
	}

	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}

}
