package com.myinsure.admin.base;

import java.util.List;
import java.util.UUID;

import com.myinsure.admin.fun.SysFunService;
import com.myinsure.utils.StringUtils;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author sxf 2015-5-29
 * 
 *         主键生成操作 创建主键生成器的想法主要有两个问题 1、UUID生成的序列号太长，无规律，繁杂
 *         2、自增的生成方式在数据库迁移的时候会产生很多麻烦 主键生成操作 创建主键生成器的想法主要有两个问题
 *         1、UUID生成的序列号太长，无规律，繁杂 2、自增的生成方式在数据库迁移的时候会产生很多麻烦
 * 
 *         主流的方式有查询表中id最大值+1的方式实现id的增长，也会出现高并发情况下出现主键冲突问题。
 *         采用线程的方式需要线程停顿一下，导致生成主键效率不高， 国外id的生成方式还有用时间串，机器码位移的方式相加得出唯一值，但逻辑太复杂也不好
 *         主流的方式有查询表中id最大值+1的方式实现id的增长，也会出现高并发情况下出现主键冲突问题。
 *         采用线程的方式需要线程停顿一下，导致生成主键效率不高， 国外id的生成方式还有用时间串，机器码位移的方式相加得出唯一值，但逻辑太复杂也不好
 * 
 *         这里采用别人项目中的一个思路，在查询某表中id的最大值的时候存入另一个表中最大值+1，将生成的id值
 *         作为这个表的id,避免冲突，保证唯一性，而且在前面还加上了项目编号和时间串。
 *         主键生成方式是，通过sys_keyid中间表来记录某表的最大值，当执行新增方法 的时候需要先查询中间表中的最大值，并+2，最为表的新id
 *         这里采用别人项目中的一个思路，在查询某表中id的最大值的时候存入另一个表中最大值+1，将生成的id值
 *         作为这个表的id,避免冲突，保证唯一性，而且在前面还加上了项目编号和时间串。
 *         主键生成方式是，通过sys_keyid中间表来记录某表的最大值，当执行新增方法 的时候需要先查询中间表中的最大值，并+2，最为表的新id
 * 
 */

public class SysKeyIdService
{

    // 每次取编码数量
    private static final int step = 2;
    private static final int max_value_default = 10;

    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(SysKeyIdService.class);// 日志

    // 单例模式
    private static final SysKeyIdService sysKeyIdService = new SysKeyIdService();

    public static SysKeyIdService me()
    {
	return sysKeyIdService;
    }

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
	boolean VcodeExt = StringUtils.isBlank(codeExt);
	boolean VmaxValue = StringUtils.isBlank(maxValue);
	if (VcodeExt || VmaxValue)
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

    // uuid主键生成
    public static String UUID()
    {
	String s = UUID.randomUUID().toString().toUpperCase();
	return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
}
