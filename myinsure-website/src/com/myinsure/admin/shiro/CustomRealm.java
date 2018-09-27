package com.myinsure.admin.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


public class CustomRealm extends AuthorizingRealm
{

	//授权
		@Override
		protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0)
		{
			//获取用户名
			String username=(String) arg0.getPrimaryPrincipal();
			//根据用户名查角色
			Set<String> roles=getroles();
			System.err.println(roles);
			//根据用户名查权限
			Set<String> stringPermissions=getstringPermissions();
			SimpleAuthorizationInfo infe=new SimpleAuthorizationInfo();
			infe.setRoles(roles);
			infe.setStringPermissions(stringPermissions);
			// TODO Auto-generated method stub
			return infe;
		}
		
		public static Set<String> getstringPermissions()
		{
			// TODO Auto-generated method stub
			Set<String> sess=new HashSet<String>();
			sess.add("add");
			sess.add("random");
			return sess;
		}

		public static Set<String> getroles()
	{
		// TODO Auto-generated method stub
			Set<String> se=new HashSet<String>();
			se.add("admin");
		return se;
	}
		
		//认证
		@Override
		protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException
		{
			super.setName("customRealm");
			String username=(String) arg0.getPrincipal();
			String sql = "select * from user where user_name=?";
			//RegisterModel regist = RegisterModel.servie.findFirst(sql, username);
			//String pass = regist.getStr("password");
			// TODO Auto-generated method stub
			SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(username,"","customRealm");
			return info;
		}


}
