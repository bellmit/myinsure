package com.myinsure.admin.website.carousel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import com.myinsure.interceptor.AdminInterceptor;
import com.myinsure.utils.DateUtil;
/**
 * 轮播图及个页面图片管理
 * @author 卢飞
 */
@Before({ AdminInterceptor.class })
public class WebImageController extends Controller
{
	/**
	 * 分页查询所有图片
	 */
	public void getCarousels(){
		Integer pageNumber = this.getPara("pageNumber") == null?1:this.getParaToInt("pageNumber");
		Page<WebImage> page = WebImageService.me().getCarousels(pageNumber);
		List<WebImage> list = page.getList();
		
		setAttr("carousels", list);
		setAttr("pageNumber", pageNumber);
		setAttr("pageSize", page.getPageSize());
    	setAttr("totalPage", page.getTotalPage());
    	setAttr("totalRow", page.getTotalRow());
    	render("carousel_list.html");
	}
	/**
	 * 分页查询所有图片
	 */
	public void getCarousels1(){
		int draw = this.getParaToInt("draw");
    	int start = this.getParaToInt("start");
    	int length = this.getParaToInt("length");
    	String csrfmiddlewaretoken = this.getPara("csrfmiddlewaretoken");
    	
    	int pageNumber = start/length + 1;
		Page<WebImage> page = WebImageService.me().getCarousels(pageNumber);
		List<WebImage> list = page.getList();
		
		setAttr("draw", draw);
		setAttr("list", list);
		setAttr("pageNumber", pageNumber);
		setAttr("pageSize", page.getPageSize());
		setAttr("totalPage", page.getTotalPage());
		setAttr("totalRow", page.getTotalRow());
		renderJson();
	}
	/**
	 * 跳转到图片添加页面
	 */
	public void toAddCarousel(){
		render("carousel_add.html");
	}
	/**
	 * 编辑图片
	 */
	public void carouselEdit(){
		String carouselId = this.getPara("carouselId");
		WebImage carousel = WebImageService.me().getCarouselById(carouselId);
		if(carousel.getStr("carousel_url") != null){
//			int i = carousel.getStr("carousel_url").indexOf(getRequest().getContextPath());
//			String sub = carousel.getStr("carousel_url").substring(i+getRequest().getContextPath().length());
			carousel.put("pic",carousel.getStr("carousel_url"));
		}
		setAttr("carousel", carousel);
		render("carousel_add.html");
	}
	/**
	 * 上传图片
	 */
	public void uploadCarouselPic(){
		getFile("imgFile", PathKit.getWebRootPath() + "/temp", 20 * 1024 * 1024, "utf-8"); // 最大上传20M的图片
		// 异步上传时，无法通过uploadFile.getFileName()获取文件名
		String fileName = getPara("fileName");
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1); // 去掉路径
		String extension = fileName.substring(fileName.lastIndexOf("."));

		String src = "carousel";
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
	 * 保存图片
	 */
	public void saveCarousel(){
		String carouselId = this.getPara("carouselId");
		String carouselName = this.getPara("carousel_name");
		String carouselUrl = this.getPara("carousel_url");
		String productManageId = this.getPara("product_manage_id");
//		carouselUrl = getRequest().getContextPath() + carouselUrl;
		Map<String, String> params = new HashMap<String, String>();
		params.put("carouselId", carouselId);
		params.put("carouselName", carouselName);
		params.put("carouselUrl", carouselUrl);
		params.put("productManageId", productManageId);
		boolean b = WebImageService.me().saveCarousel(params);
		if(b){
			renderJson("error",0);
		}else{
			renderJson("error",1);
		}
	}
	/**
	 * 批量删除图片
	 */
	public void delBatch(){
		String[] carouselIds = this.getParaValues("carouselId");
		for (String carouselId : carouselIds)
		{
			WebImageService.me().delCarousel(carouselId);
		}
		renderJson("error",0);
	}
	/**
	 * 使用非轮播图图片
	 */
	public void imageUpper(){
		String carouselId = this.getPara("carouselId");
		boolean b = WebImageService.me().imageUpper(carouselId);
		if(b){
			setAttr("error", 0);
			setAttr("msg", "启用成功！");
		}else{
			setAttr("error", 1);
			setAttr("msg", "启用失败！");
		}
		renderJson();
	}
	/**
	 * 将图片用作轮播图
	 */
	public void carouselUpper(){
		String carouselId = this.getPara("carouselId");
		List<WebImage> list = WebImageService.me().getShowCarousel();
		if(list != null){
			int size = list.size();
			if(size >= 4){
				setAttr("error", 1);
				setAttr("msg", "轮播图不能多于是4张！");
				renderJson();
				return;
			}
		}
		Integer isUsed = 1;
		boolean b = WebImageService.me().updateCarousel(carouselId, isUsed);
		if(b){
			setAttr("error", 0);
			setAttr("msg", "启用成功！");
		}else{
			setAttr("error", 1);
			setAttr("msg", "启用失败！");
		}
		renderJson();
	}
	/**
	 * 将图片轮播图弃用
	 */
	public void carouselUnder(){
		String carouselId = this.getPara("carouselId");
		Integer isUsed = 0;
		boolean b = WebImageService.me().updateCarousel(carouselId, isUsed);
		if(b){
			setAttr("error", 0);
			setAttr("msg", "弃用成功！");
		}else{
			setAttr("error", 1);
			setAttr("msg", "弃用失败！");
		}
		renderJson();
	}
}








