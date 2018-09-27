package com.myinsure.admin.website.company;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.ActionEnter;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.myinsure.interceptor.AdminInterceptor;
import com.myinsure.utils.DateUtil;
/**
 * 保险公司管理
 * @author 卢飞
 */
@Before({ AdminInterceptor.class })
public class WebCompanyController extends Controller
{
	/**
	 * 分页查询保险公司
	 */
	public void getCompanys(){
		Integer pageNumber = this.getPara("pageNumber") == null?1:this.getParaToInt("pageNumber");
		Page<WebCompany> page = WebCompanyService.me().getCompanys(pageNumber);
		List<WebCompany> list = page.getList();
		
		setAttr("companys", list);
		setAttr("pageNumber", pageNumber);
		setAttr("pageSize", page.getPageSize());
    	setAttr("totalPage", page.getTotalPage());
    	setAttr("totalRow", page.getTotalRow());
    	render("company_lis.html");
	}
	/**
	 * 跳转到添加保险公司页面
	 */
	public void toAddCompany(){
		render("company_add.html");
	}
	/**
	 * 跳转到编辑保险公司页面
	 */
	public void toEditCompany(){
		String companyId = this.getPara("companyId");
		WebCompany company = WebCompanyService.me().getCompanyById(companyId);
		if(company.getStr("company_logo") != null){
//			int i = company.getStr("company_logo").indexOf(getRequest().getContextPath());
//			String sub = company.getStr("company_logo").substring(i+getRequest().getContextPath().length());
			company.put("pic",company.getStr("company_logo"));
		}
		if(company.getStr("company_url") != null){
			int i = company.getStr("company_url").indexOf(getRequest().getContextPath());
			int j = company.getStr("company_url").indexOf("?");
			String sub = company.getStr("company_url").substring(i+getRequest().getContextPath().length()+1,j);
			company.set("company_url",sub);
		}
		setAttr("company", company);
		render("company_add.html");
	}
	/**
	 * 上传保险公司logo
	 */
	public void uploadCompanyPhoto(){
		getFile("imgFile", PathKit.getWebRootPath() + "/temp", 20 * 1024 * 1024, "utf-8"); // 最大上传20M的图片
		// 异步上传时，无法通过uploadFile.getFileName()获取文件名
		String fileName = getPara("fileName");
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1); // 去掉路径
		String extension = fileName.substring(fileName.lastIndexOf("."));

		String src = "company";
		String savePath = PathKit.getWebRootPath() + "/front/upload/" + src;
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
				
				json.put("error", "0");
				json.put("src", src + "/" + fileName); // 相对地址，显示图片用

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
	/**
	 * 上传编辑器中的图片
	 */
	public void uploadImg(){
		UploadFile file = getFile("imgFile", PathKit.getWebRootPath() + "/temp", 20 * 1024 * 1024, "utf-8"); // 最大上传20M的图片
		String fileName = file.getFileName();
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1); // 去掉路径
		String extension = fileName.substring(fileName.lastIndexOf("."));

		String src = "company";
		String savePath = PathKit.getWebRootPath() + "/front/upload/" + src;
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
				
				json.put("state", "SUCCESS");
				json.put("title", fileName);
				json.put("original", fileName);
				json.put("url", "/upload/" + src + "/" + fileName); // 相对地址，显示图片用

			} catch (FileNotFoundException e)
			{
				json.put("state", "FAIL");
				e.printStackTrace();
				renderJson(json.toString());
				return;
			} catch (IOException e)
			{
				json.put("state", "FAIL");
				e.printStackTrace();
				renderJson(json.toString());
				return;
			}
		} else
		{
			json.put("state", "FAIL");
		}
		renderJson(json.toString());
	}
	/**
	 * 保存保险公司
	 */
	public void saveCompany(){
		String companyId = this.getPara("companyId");
		String companyName = this.getPara("company_name");
		String claimsPhone = this.getPara("claims_phone");
//		String testUrl = this.getPara("test_url");
		String companyUrl = this.getPara("company_url");
		companyUrl = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" 
			    + getRequest().getServerPort() + getRequest().getContextPath() + "/" + companyUrl;
		String companyLogo = this.getPara("company_logo");
//		companyLogo = getRequest().getContextPath() + companyLogo;
		String companyIntroduce = this.getPara("company_introduce");
		Map<String, String> params = new HashMap<String, String>();
		params.put("companyId", companyId);
		params.put("companyName", companyName);
		params.put("claimsPhone", claimsPhone);
		params.put("companyUrl", companyUrl);
//		params.put("testUrl", testUrl);
		params.put("companyLogo", companyLogo);
		params.put("companyIntroduce", companyIntroduce);
		boolean b = WebCompanyService.me().saveCompany(params);
		if(b){
			renderJson("msg","保存成功！");
		}else{
			renderJson("msg","保存失败！");
		}
	}
	/**
	 * 删除保险公司
	 */
	public void delCompany(){
		String companyId = this.getPara("companyId");
		boolean b = WebCompanyService.me().delCompany(companyId);
		if(b){
			renderJson("msg","删除成功！");
		}else{
			renderJson("msg","删除失败！");
		}
	}
}








