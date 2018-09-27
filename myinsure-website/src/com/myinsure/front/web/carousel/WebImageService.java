package com.myinsure.front.web.carousel;

import java.util.List;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.myinsure.admin.website.carousel.WebImage;

public class WebImageService
{
	@SuppressWarnings("unused")
	private static final Log log = Log.getLog(WebImageService.class);
	private static final WebImageService rnsCarouselService = new WebImageService();
	public static WebImageService me()
	{
		return rnsCarouselService;
	}
	/**
	 * 查询图片
	 * @param param
	 * @param size
	 * @return
	 */
	public List<WebImage> getImages(String param,Integer size){
		String sql = " from rns_carousel where is_del = 0 and is_used = 1 and product_manage_id = " + param;
		Page<WebImage> page = WebImage.dao.paginate(1, size, "select * ", sql);
		List<WebImage> list = page.getList();
		return list;
	}
}









