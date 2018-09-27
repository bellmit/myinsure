package com.myinsure.admin.website.carousel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ibm.db2.jcc.am.p;
import com.jfinal.plugin.activerecord.Page;
import com.myinsure.admin.base.SysKeyIdService;
import com.myinsure.utils.DateUtil;

public class WebImageService
{
	private static final WebImageService rnsCarouselService = new WebImageService();
	public static WebImageService me()
	{
		return rnsCarouselService;
	}
	/**
	 * 分页查询轮播图
	 */
	public Page<WebImage> getCarousels(Integer pageNum){
		String sql = " from rns_carousel where is_del = 0 order by is_used";
		Page<WebImage> page = WebImage.dao.paginate(pageNum, 10, "select * ", sql);
		return page;
	}
	/**
	 * 根据id查询轮播图
	 * @param carouselId
	 * @return
	 */
	public WebImage getCarouselById(String carouselId){
		WebImage carousel = WebImage.dao.findById(carouselId);
		return carousel;
	}
	/**
	 * 保存轮播图信息
	 * @param parmas
	 * @return
	 */
	public boolean saveCarousel(Map<String, String> params){
		WebImage carousel = getCarouselById(params.get("carouselId"));
		boolean flag = false;
		if(carousel == null){
			carousel = new WebImage();
			SysKeyIdService sysKeyIdService = new SysKeyIdService();
			int carouselId = sysKeyIdService.generateKeyMysql("rns_carousel");
			carousel.set("carousel_id", carouselId);
			carousel.set("create_time", DateUtil.getTodaySec());
			flag = true;
		}
		carousel.set("carousel_name", params.get("carouselName"));
		carousel.set("carousel_url", params.get("carouselUrl"));
		carousel.set("product_manage_id", params.get("productManageId"));
		carousel.set("update_time", DateUtil.getTodaySec());
		boolean b = false;
		if(flag){
			b = carousel.save();
		}else{
			b = carousel.update();
		}
		return b;
	}
	/**
	 * 删除轮播图(逻辑删除)
	 * @param carouselId
	 * @return
	 */
	public boolean delCarousel(String carouselId){
		WebImage carousel = getCarouselById(carouselId);
		if(carousel == null){
			return false;
		}
		carousel.set("is_del", 1);
		boolean b = carousel.update();
		return b;
	}
	/**
	 * 修改轮播图在用或未用
	 * @param carouselId
	 * @param isUsed
	 * @return
	 */
	public boolean updateCarousel(String carouselId,Integer isUsed){
		WebImage carousel = getCarouselById(carouselId);
		carousel.set("is_used", isUsed);
		carousel.set("update_time", DateUtil.getTodaySec());
		boolean b = carousel.update();
		return b;
	}
	/**
	 * 查找所有在首页显示的轮播图
	 */
	public List<WebImage> getShowCarousel(){
		String sql = "select product_name,product_url,carousel_url from rns_carousel where is_del = 0 and is_used = 1 and product_manage_id = 0 order by update_time desc";
		List<WebImage> list = WebImage.dao.find(sql);
		return list;
	}
	/**
	 * 根据图片所属管理目录查找
	 * @param productManageId
	 * @return
	 */
	public WebImage getByManageId(Integer productManageId){
		String sql = "select * from rns_carousel where is_del = 0 and is_used = 1 and product_manage_id = " + productManageId;
		WebImage carousel = WebImage.dao.findFirst(sql);
		return carousel;
	}
	public WebImage getByManage(Integer productManageId){
		String sql = "select carousel_url,product_name,product_url from rns_carousel where is_del = 0 and is_used = 1 and product_manage_id = " + productManageId;
		WebImage carousel = WebImage.dao.findFirst(sql);
		return carousel;
	}
	/**
	 * 使用非轮播图图片
	 * @return
	 */
	public boolean imageUpper(String carouselId){
		WebImage carousel = getCarouselById(carouselId);//准备要用的
		WebImage carousel2 = getByManageId(carousel.getInt("product_manage_id"));//正在使用的
		boolean flag = true;
		if(carousel2 != null){
			carousel2.set("is_used", 0);
			carousel2.set("update_time", DateUtil.getTodaySec());
			flag = carousel2.update();
		}
		if(!flag){
			return false;
		}
		carousel.set("is_used", 1);
		carousel.set("update_time", DateUtil.getTodaySec());
		boolean b = carousel.update();
		return b;
	}
}









