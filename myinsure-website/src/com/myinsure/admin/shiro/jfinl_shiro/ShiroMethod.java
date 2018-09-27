/**
 * Copyright (C) 2014 wellbole
 * @Package com.wellbole.web.core.shiro  
 * @Title: ShiroExt.java  
 * @Description: Shiro辅助方法
 * @author 李飞     
 * @date 2014年9月10日  下午12:47:57  
 * @since V1.0.0 
 *
 * Modification History:
 * Date         Author      Version     Description
 * -------------------------------------------------------------
 * 2014年9月10日      李飞                       V1.0.0        新建文件   
 */
package com.myinsure.admin.shiro.jfinl_shiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @ClassName: ShiroExt
 * @Description: Shiro辅助方法 gt.registerFunctionPackage("shiro",new ShiroExt ());
 *               你可以在模板里直接调用，譬如 @if(shiro.isGuest()) {
 * @author 李飞
 * @date 2014年9月10日 下午12:47:57
 * @since V1.0.0
 */
public class ShiroMethod {

    /**
     * 获取 Subject
     *
     * @return Subject
     */
    protected Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 验证当前用户是否属于该角色？,使用时与lacksRole 搭配使用
     *
     * @param roleName
     *            角色名
     * @return 属于该角色：true，否则false
     */
    public boolean hasRole(String roleName) {
        return getSubject() != null && roleName != null && roleName.length() > 0 && getSubject().hasRole(roleName);
    }

    /**
     * 与hasRole标签逻辑相反，当用户不属于该角色时验证通过。
     *
     * @param roleName
     *            角色名
     * @return 不属于该角色：true，否则false
     */
    public boolean lacksRole(String roleName) {
        return !hasRole(roleName);
    }

    /**
     * 验证当前用户是否属于以下任意一个角色。
     *
     * @param roleNames
     *            角色列表
     * @return 属于:true,否则false
     */
    public boolean hasAnyRoles(String ... roleNames) {
        Subject subject = getSubject();
        boolean ret = true && subject != null && roleNames != null && roleNames.length > 0;
        if (!ret) {
            return false;
        }
        for (String role : roleNames) {
            if (subject.hasRole(role.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证当前用户是否具有所列的所有角色
     * 
     * @param roleNames
     *            角色列表
     * @return 拥有所有角色，返回true，否则返回false
     */
    public boolean hasAllRoles(String... roleNames) {
        Subject subject = getSubject();
        boolean ret = true && subject != null && roleNames != null && roleNames.length > 0;
        if (!ret) {
            return ret;
        }
        List<String> roles = new ArrayList<String>();
        Collections.addAll(roles, roleNames);
        return subject.hasAllRoles(roles);
    }

    /**
     * 验证当前用户是否拥有指定权限,使用时与lacksPermission 搭配使用
     *
     * @param permission
     *            权限名
     * @return 拥有权限：true，否则false
     */
    public boolean hasPermission(String permission) {
        boolean ret = true;
        ret = ret && getSubject() != null && permission != null && permission.length() > 0;
        ret = ret && getSubject().isPermitted(permission);
        return ret;
    }

    /**
     * 验证当前用户是否具有所列的所有权限
     * 
     * @param permissions
     *            权限列表
     * @return 拥有所有权限，返回true，否则返回false
     */
    public boolean hasAllPermissions(String... permissions) {
        boolean ret = true;
        ret = ret && getSubject() != null && permissions != null && permissions.length > 0;
        ret = ret && getSubject().isPermittedAll(permissions);
        return ret;
    }

    /**
     * 验证当前用户是否具有所列权限之一
     * 
     * @param permissions
     *            权限列表
     * @return 拥有权限之一，返回true，都不拥有，返回false
     */
    public boolean hasAnyPermissions(String... permissions) {
        Subject subject = getSubject();
        boolean ret = true && subject != null && permissions != null && permissions.length > 0;
        if (!ret) {
            return false;
        }
        for (String permission : permissions) {
            if (subject.isPermitted(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 与hasPermission标签逻辑相反，当前用户没有制定权限时，验证通过。
     *
     * @param permission
     *            权限名
     * @return 拥有权限：true，否则false
     */
    public boolean lacksPermission(String permission) {
        return !hasPermission(permission);
    }

    /**
     * 已认证通过的用户。不包含已记住的用户，这是与isUser标签的区别所在。与isNotAuthenticated搭配使用
     *
     * @return 通过身份验证：true，否则false
     */
    public boolean isAuthenticated() {
        return getSubject() != null && getSubject().isAuthenticated();
    }

    /**
     * 未认证通过用户，与isAuthenticated标签相对应。与isGuest标签的区别是，该标签包含已记住用户。。
     *
     * @return 没有通过身份验证：true，否则false
     */
    public boolean notAuthenticated() {
        return !isAuthenticated();
    }

    /**
     * 认证通过或已记住的用户。与isGuest搭配使用。
     *
     * @return 用户：true，否则 false
     */
    public boolean isUser() {
        return getSubject() != null && getSubject().getPrincipal() != null;
    }

    /**
     * 验证当前用户是否为“访客”，即未认证（包含未记住）的用户。用isUser搭配使用
     *
     * @return 访客：true，否则false
     */
    public boolean isGuest() {
        return !isUser();
    }

    /**
     * 输出当前用户信息，通常为登录帐号信息。
     * 
     * @return 当前用户信息
     */
    public String principal() {
        if (getSubject() != null) {
            // Get the principal to print out
            Object principal = getSubject().getPrincipal();
            return principal.toString();
        }
        return "Guest";
    }
}
