package com.myinsure.utils;

import java.util.List;
import java.util.UUID;

import com.myinsure.admin.base.SysKeyId;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 主键生成操作
 * 
 * @author sxf 2015-5-29
 * 
 *   主键生成方式是，通过sys_keyid中间表来记录某表的最大值，当执行新增方法 的时候需要先查询中间表中的最大值，并+2，最为表的新id
 * 
 */

public class SysKeyIdService
{

    // 每次取编码数量
    private static final int step = 2;
    private static final int max_value_default = 10;

    public SysKeyId getByCodeExt(String tableName, String codeExt)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_keyid where 1=1 ");

	if (StringUtils.isNotEmpty(tableName))
	{
	    sql.append(" and  table_name='" + tableName + "'");
	}
	if (StringUtils.isNotEmpty(codeExt))
	{
	    sql.append(" and  code_ext='" + codeExt + "'");
	}

	SysKeyId sysKeyId = SysKeyId.dao.findFirst(sql.toString());
	return sysKeyId;
    }

    public boolean updateOrSave(String tableName, String codeExt, String maxValue)
    {
	boolean result = false;
	boolean VcodeExt = StringUtils.isNotEmpty(codeExt);
	boolean VmaxValue = StringUtils.isNotEmpty(maxValue);
	if (!VcodeExt || !VmaxValue)
	{
	    return result;
	}
	SysKeyId sysKeyId = getByCodeExt(tableName, codeExt);
	if (null == sysKeyId)
	{
	    sysKeyId = new SysKeyId();
	    sysKeyId.set("table_name", tableName);
	    sysKeyId.set("code_ext", codeExt);
	    sysKeyId.set("max_value", maxValue);
	    result = sysKeyId.save();
	} else
	{
	    sysKeyId.set("max_value", maxValue);
	    result = sysKeyId.update();
	}

	return result;
    }

    /** 获取中间表中的id值。如果id值为空或不存在，则处初始化该id值 */
    public SysKeyId getMAXID(String tableName)
    {
	StringBuffer sql = new StringBuffer();
	sql.append("select * from sys_keyid where 1=1 ");

	if (StringUtils.isNotEmpty(tableName))
	{
	    sql.append(" and  table_name='" + tableName + "'");
	}
	SysKeyId sysKeyId = SysKeyId.dao.findFirst(sql.toString());
	return sysKeyId;
    }

    // oracle中主键的生成方式
    public int generateKeyOracle(String tableName)
    {

	int keyid = 0;
	SysKeyId sysKey = getMAXID(tableName);
	if (null == sysKey)
	{
	    sysKey = new SysKeyId();
	    String sql = "select stepkey.nextval  from dual";// 此处采用oracle的sequence来定义主键表的值
	    List<Record> seq = Db.find(sql);
	    sysKey.set("id", seq.get(0).get("NEXTVAL"));
	    sysKey.set("table_name", tableName);
	    sysKey.set("max_value", max_value_default);// 如果没有此表的最大值记录，则创建此表的最大记录，初始化值为100
	    keyid = max_value_default;
	    sysKey.save();
	} else
	{
	    String maxvalue = sysKey.getStr("max_value");
	    keyid = Integer.valueOf(maxvalue) + step;
	    sysKey.set("max_value", keyid);// 如果有记录则更新最大值记录，最大值记录每次都要+步长，再次存入
	    sysKey.update();
	}
	return keyid;
    }

    // mysql中主键的生成方式，主表主键为自增
    public int generateKeyMysql(String tableName)
    {

	int keyid = 0;
	SysKeyId sysKey = getMAXID(tableName);
	if (null == sysKey)
	{
	    sysKey = new SysKeyId();
	    sysKey.set("table_name", tableName);
	    sysKey.set("max_value", max_value_default);// 如果没有此表的最大值记录，则创建此表的最大记录，初始化值为100
	    keyid = max_value_default;
	    sysKey.save();
	} else
	{
	    String maxvalue = sysKey.getStr("max_value");
	    keyid = Integer.valueOf(maxvalue) + step;
	    sysKey.set("max_value", keyid);// 如果有记录则更新最大值记录，最大值记录每次都要+步长，再次存入
	    sysKey.update();
	}
	return keyid;
    }

    public static String UUID()
    {
	String s = UUID.randomUUID().toString().toUpperCase();
	return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
}
