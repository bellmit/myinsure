package com.myinsure.admin.logs;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Page;
/**
 * 保险公司
 * @author 卢飞
 */
public class SyslogsService
{
	private static final SyslogsService syslogsService = new SyslogsService();
	public static SyslogsService me()
	{
		return syslogsService;
	}
	/**
	 * 查询所有日志
	 * @return
	 */
	public List<Syslogs> getAllCompany(){
		String sql = "select * from sys_logs";
		List<Syslogs> list = Syslogs.dao.find(sql);
		return list;
	}
	/**
	 * 分页查询日志信息
	 */
	public Page<Syslogs> getLogs(Integer pageNum,Integer pageSize,Map<String, String> params){
		StringBuilder where = new StringBuilder(" from sys_logs where 1 = 1 ");
		Set<Entry<String, String>> set = params.entrySet();
		Iterator<Entry<String, String>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if("realName".equalsIgnoreCase(key.trim()) && !StringUtils.isEmpty(value)){
				where.append(" and real_name like '%" + value + "%' ");
				continue;
			}
			if("beginTime".equalsIgnoreCase(key.trim()) && !StringUtils.isEmpty(value)){
				where.append(" and establishTime > '" + value + "' ");
				continue;
			}
			if("endTime".equalsIgnoreCase(key.trim()) && !StringUtils.isEmpty(value)){
				where.append(" and establishTime < '" + value + "' ");
				continue;
			}
		}
		where.append(" order by id desc");
		Page<Syslogs> page = Syslogs.dao.paginate(pageNum, pageSize, "select * ", where.toString());
		return page;
	}
	/**
	 * 根据id查询日志信息
	 * @param logId
	 * @return
	 */
	public Syslogs getLogById(String logId){
		Syslogs log = Syslogs.dao.findById(logId);
		return log;
	}
	/**
	 * 删除日志信息
	 * @param logId
	 * @return
	 */
	public boolean delLog(String logId){
		boolean b = Syslogs.dao.deleteById(logId);
		return b;
	}
	
}









