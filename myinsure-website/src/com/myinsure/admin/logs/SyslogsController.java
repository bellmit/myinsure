package com.myinsure.admin.logs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myinsure.interceptor.AdminInterceptor;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
/**
 * 保险公司管理
 * @author 卢飞
 */
@Before({ AdminInterceptor.class })
public class SyslogsController extends Controller
{
	/**
	 * 跳转到日志列表页面
	 */
	public void getLogs(){
		String realName = this.getPara("real_name");
		String beginTime = this.getPara("begin_time");
		String endTime = this.getPara("end_time");
		Map<String, String> params = new HashMap<String, String>();
		params.put("realName", realName);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		
		Integer pageNumber = this.getPara("page") == null?1:this.getParaToInt("page");
		Integer pageSize = this.getPara("limit") == null?20:this.getParaToInt("limit");
		Page<Syslogs> page = SyslogsService.me().getLogs(pageNumber,pageSize,params);
		List<Syslogs> list = page.getList();
    	
		this.setAttr("logList", list);
		this.setAttr("totalRow", page.getTotalRow());
		this.setAttr("totalPage", page.getTotalPage());
    	render("logs_list.html");
	}
	
	public void getLogs1(){
		String realName = this.getPara("real_name");
		String beginTime = this.getPara("begin_time");
		String endTime = this.getPara("end_time");
		Map<String, String> params = new HashMap<String, String>();
		params.put("realName", realName);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		
		int draw = this.getParaToInt("draw");
    	int start = this.getParaToInt("start");
    	int length = this.getParaToInt("length");
    	String csrfmiddlewaretoken = this.getPara("csrfmiddlewaretoken");
    	
    	int pageNumber = start/length + 1;
		Integer pageSize = this.getPara("limit") == null?20:this.getParaToInt("limit");
		Page<Syslogs> page = SyslogsService.me().getLogs(pageNumber,pageSize,params);
		List<Syslogs> list = page.getList();
		
		this.setAttr("list", list);
		setAttr("draw", draw);
    	setAttr("pageNumber", pageNumber);
    	setAttr("pageSize", page.getPageSize());
		this.setAttr("totalRow", page.getTotalRow());
		this.setAttr("totalPage", page.getTotalPage());
		renderJson();
	}
	/**
	 * 分页查询日志列表
	 */
	public void getLogsJson(){
		String realName = this.getPara("real_name");
		String beginTime = this.getPara("begin_time");
		String endTime = this.getPara("end_time");
		Map<String, String> params = new HashMap<String, String>();
		params.put("realName", realName);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		
		Integer pageNumber = this.getPara("page") == null?1:this.getParaToInt("page");
		Integer pageSize = this.getPara("limit") == null?20:this.getParaToInt("limit");
		Page<Syslogs> page = SyslogsService.me().getLogs(pageNumber,pageSize,params);
		List<Syslogs> list = page.getList();
    	
    	setAttr("data", list);
    	setAttr("count",Integer.valueOf(page.getTotalRow()));
		setAttr("code", 0);
    	renderJson();
	}
	/**
	 * 单个删除
	 */
	public void delLog(){
		String logId = this.getPara("logId");
		boolean b = SyslogsService.me().delLog(logId);
		if(b){
			renderJson("msg","删除成功！");
		}else{
			renderJson("msg","删除失败！");
		}
	}
	/**
	 * 批量删除
	 */
	public void delBatch(){
		String[] logIds = this.getParaValues("id");
		boolean b = false;
		for (String logId : logIds)
		{
			b = SyslogsService.me().delLog(logId);
		}
		if(b){
			renderJson("success",1);
		}else{
			renderJson("success",0);
		}	
	}
}








