package com.myinsure.admin.website.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.myinsure.admin.base.SysKeyIdService;
import com.myinsure.interceptor.AdminInterceptor;
import com.myinsure.utils.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
/**
 * 角色管理
 * 
 * @author sxf
 * 
 */
@Before(
{ AdminInterceptor.class })
public class WebConfigController extends Controller
{
	Log log = Log.getLog(WebConfigController.class);

	/**
	 * load WebCongig
	 */
	public void configload()
	{
		List<WebConfig> webConfig = WebConfigService.me().getWebConfig();
		for (WebConfig webConfigs : webConfig)
		{
			this.setAttr(webConfigs.getStr("name"), webConfigs.getStr("value"));
		}
		this.render("/sysadmin/website/web/webconfig.html");
	}

	/**
	 * saveorupdate WebConfig
	 */
	public void configsave()
	{
		try
		{
			String web_name = this.getPara("web_name");
			String web_domain = this.getPara("web_domain");
			String web_icp_number = this.getPara("web_icp_number");
			String web_title = this.getPara("web_title");
			String web_copyright = this.getPara("web_copyright");
			String web_administrator_phone = this.getPara("web_administrator_phone");
			String web_administrator_email = this.getPara("web_administrator_email");
			String web_describe = this.getPara("web_describe");
			
			String contact_hotline = this.getPara("contact_hotline");
			String company_address = this.getPara("company_address");
			String company_name = this.getPara("company_name");
			String technical_support = this.getPara("technical_support");
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("web_name", web_name);
			params.put("web_domain", web_domain);
			params.put("web_icp_number", web_icp_number);
			params.put("web_title", web_title);
			params.put("web_copyright", web_copyright);
			params.put("web_administrator_phone", web_administrator_phone);
			params.put("web_administrator_email", web_administrator_email);
			params.put("web_describe", web_describe);
			
			params.put("contact_hotline", contact_hotline);
			params.put("company_address", company_address);
			params.put("company_name", company_name);
			params.put("technical_support", technical_support);

			// Map.entry<Integer,String> 映射项（键-值对） 有几个方法：用上面的名字entry
			// entry.getKey() ;entry.getValue(); entry.setValue();
			// map.entrySet() 返回此映射中包含的映射关系的 Set视图。

			for (Map.Entry<String, String> entry : params.entrySet())
			{
				boolean configsave = WebConfigService.me().configsave(entry.getKey(), entry.getValue());
				if (!configsave)
				{
					this.renderText("新增角色失败");
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			this.renderText("服务器异常");
		}
		forwardAction("/sysadmin/website/web/configload");
	}

	// 查看菜单列表
	public void columnList()
	{
		List<WebColumn> columnList = WebColumnService.me().getWebColumns();

		this.setAttr("columnList", columnList);
		this.render("/sysadmin/website/web/columnconfig.html");
	}

	// 新增或更新菜单列表
	public void saveorupdateColumn()
	{
		List<WebColumn> columnList = WebColumnService.me().getWebColumn();
		for (WebColumn webcolumn : columnList)
		{
			String id = webcolumn.getStr("id");
			String name = this.getPara("name_" + id);
			String orders = this.getPara("orders_" + id);
			String nav = this.getPara("nav_" + id);
			String faclass = this.getPara("faclass_" + id);
			String foldername = this.getPara("foldername_" + id);
			String outurl = this.getPara("out_url_" + id);
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", id);
			params.put("name", name);
			params.put("orders", orders);
			params.put("nav", nav);
			params.put("faclass", faclass);
			params.put("foldername", foldername);
			params.put("outurl", outurl);
			boolean flag = WebColumnService.me().updateColumn(params);
			if (!flag)
			{
				this.renderText("error", "修改出错");
			}
		}
		if (this.getPara("count") != null && !this.getPara("count").equalsIgnoreCase(""))
		{
			Integer count = Integer.valueOf(this.getPara("count"));
			for (; count > 0; count--)
			{
				int id = SysKeyIdService.me().generateKeyMysql("web_column");
				String name = this.getPara("name_" + count);
				String orders = this.getPara("orders_" + count);
				String nav = this.getPara("nav_" + count);
				String faclass = this.getPara("faclass_" + count);
				String foldername = this.getPara("foldername_" + count);
				String wapNav = this.getPara("wap_nav_" + count);
				String outurl = this.getPara("out_url_" + count);
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id", String.valueOf(id));
				maps.put("name", name);
				maps.put("orders", orders);
				maps.put("nav", nav);
				maps.put("faclass", faclass);
				maps.put("foldername", foldername);
				maps.put("wapNav", wapNav);
				maps.put("outurl", outurl);
				boolean flag2 = WebColumnService.me().saveColumn(maps);
				if (!flag2)
				{
					this.renderText("保存出错");
				}
			}
		}
		List<WebColumn> newcolumnList = WebColumnService.me().getWebColumns();
		this.setAttr("columnList", newcolumnList);
		this.render("/sysadmin/website/web/columnconfig.html");
	}
	
	/**
	 * 上传图片
	 */
	public void uploadImg(){
		getFile("imgFile", PathKit.getWebRootPath() + "/temp", 20 * 1024 * 1024, "utf-8"); // 最大上传20M的图片
		// 异步上传时，无法通过uploadFile.getFileName()获取文件名
		String fileName = this.getPara("fileName");
		String columnId = this.getPara("columnId");//导航id
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1); // 去掉路径
		String extension = fileName.substring(fileName.lastIndexOf("."));

		String src = "/front/upload/column";
		String savePath = PathKit.getWebRootPath() + src;
		JSONObject json = new JSONObject();

		if (".png".equalsIgnoreCase(extension) || ".jpg".equalsIgnoreCase(extension)
				|| ".gif".equalsIgnoreCase(extension) || ".jpeg".equalsIgnoreCase(extension) 
				|| ".bmp".equalsIgnoreCase(extension))
		{
			File source = new File(PathKit.getWebRootPath() + "/temp/" + fileName); // 获取临时文件对象
			fileName = DateUtil.getTodaySecNum() + extension;
			try
			{
				FileInputStream fis = new FileInputStream(source);

				File targetDir = new File(savePath);
				if (!targetDir.exists())
				{
					targetDir.mkdirs();
				}

				File target = new File(targetDir, fileName);
				if (!target.exists())
				{
					target.createNewFile();
				}

				FileOutputStream fos = new FileOutputStream(target);
				byte[] bts = new byte[1024 * 20];
				while (fis.read(bts, 0, 1024 * 20) != -1)
				{
					fos.write(bts, 0, 1024 * 20);
				}

				fos.close();
				fis.close();
				source.delete();
				src = getRequest().getContextPath() + src;
				boolean b = WebColumnService.me().saveColumnImg(columnId,"/upload/column/" + fileName);
				if(b){
					json.put("error", "0");
				}else{
					json.put("error", "1");
				}
			} catch (FileNotFoundException e)
			{
				json.put("error", "1");
				e.printStackTrace();
				renderJson(json.toString());
				return;
			} catch (IOException e)
			{
				json.put("error", "1");
				e.printStackTrace();
				renderJson(json.toString());
				return;
			}
		} else
		{
			json.put("error", "1");
		}
		renderJson(json.toString());
	}

	// 删除菜单列表
	public void columnDel()
	{
		String id = this.getPara("id");
		boolean flag = WebColumnService.me().columnDel(id);
		if (flag)
		{
			this.renderText("删除成功");
		} else
		{
			this.renderText("删除失败");
		}
	}

	// 移动至其他菜单
	public void moveToOther()
	{
		String parentid = this.getPara("parentid");
		String childid = this.getPara("childid");
		boolean b = WebColumnService.me().updateColumn(parentid, childid);
		if (b)
		{
			renderJson("msg", "更改成功！");
		} else
		{
			renderJson("msg", "更改失败！");
		}
	}
}
