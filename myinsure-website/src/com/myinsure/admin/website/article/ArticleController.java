package com.myinsure.admin.website.article;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.myinsure.admin.user.SysUser;
import com.myinsure.admin.user.SysUserService;
import com.myinsure.admin.website.web.WebColumn;
import com.myinsure.admin.website.web.WebColumnService;
import com.myinsure.admin.website.webmap.WebMaping;
import com.myinsure.admin.website.webmap.WebMapingService;
import com.myinsure.interceptor.AdminInterceptor;
import com.myinsure.utils.CookieUtils;
import com.myinsure.utils.DateUtil;

import org.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

/**
 * 文章管理
 * 
 * @author Administrator
 * 
 */
@Before({ AdminInterceptor.class })
public class ArticleController extends Controller
{
    Log log = Log.getLog(ArticleController.class);
    private final String domain_name="http://myadmin.inssm.com/myinsure-website/front/";//域名
    // 跳转至文章编辑页面
    public void article() throws Exception
    {
	List<WebColumn> webColumns = WebColumnService.me().getWebColumns();
	String userId = CookieUtils.get(this, "user_id");
	SysUser user = SysUserService.me().getById(userId);
	setAttr("user", user);
	setAttr("webColumns", webColumns);
	render("/sysadmin/website/article/articleedit.html");
    }

    // 保存文章信息
    public void saveArticle() throws Exception
    {
	String title = this.getPara("title");
	String content = this.getPara("content");
	String identification = this.getPara("identification");
	String issue = this.getPara("issue");
	String comment = this.getPara("comment");
	String keywords = this.getPara("keywords");
	String tags = this.getPara("tags");
	String description = this.getPara("description");
	String longitude = this.getPara("longitude");
	String latitude = this.getPara("latitude");
	String remarks = this.getPara("remarks");
	String metadata1 = this.getPara("metadata1");
	String metadata2 = this.getPara("metadata2");
	String recycle = this.getPara("recycle");
	String template = this.getPara("template");
	String recommend = this.getPara("recommend");
	String newsPic = this.getPara("news_pic");
//	newsPic = getRequest().getScheme() + "://" + getRequest().getServerName() + ":"
//		    + getRequest().getServerPort() + getRequest().getContextPath() + newsPic;

	Map<String, String> map = new HashMap<String, String>();
	map.put("title", title);
	map.put("content", content);
	map.put("identification", identification);
	map.put("issue", issue);
	map.put("comment", comment);
	map.put("keywords", keywords);
	map.put("tags", tags);
	map.put("description", description);
	map.put("longitude", longitude);
	map.put("latitude", latitude);
	map.put("remarks", remarks);
	map.put("metadata1", metadata1);
	map.put("metadata2", metadata2);
	map.put("recycle", recycle);
	map.put("template", template);
	map.put("recommend", recommend);
	map.put("newsPic", newsPic);
	String articleKey = ArticleService.me().saveAtrticle(map);

	String [] class1s = this.getParaValues("class1");
	String [] class2s = this.getParaValues("class2");
	String [] class3s = this.getParaValues("class3");
	String [] specialIds = this.getParaValues("special_id");

	if (articleKey != null)
	{// 文章保存成功
	    if (class1s != null && class1s.length > 0)
	    {
		for (String class1 : class1s)
		{
		    WebMapingService.me().saveMappingColumn(articleKey, class1);
		}
	    }
	    if (class2s != null && class2s.length > 0)
	    {
		for (String class2 : class2s)
		{
		    WebMapingService.me().saveMappingColumn(articleKey, class2);
		}
	    }
	    if (class3s != null && class3s.length > 0)
	    {
		for (String class3 : class3s)
		{
		    WebMapingService.me().saveMappingColumn(articleKey, class3);
		}
	    }
	    if (specialIds != null && specialIds.length > 0)
	    {
		for (String specialId : specialIds)
		{
		    WebMapingService.me().saveMappingSpecial(articleKey, specialId);
		}
	    }
	    renderJson("msg", "保存成功！");
	} else
	{
	    renderJson("msg", "保存失败！");
	}
    }

    // 图片上传
    public void uploadImg() throws Exception
    {
	UploadFile file = getFile("imgFile", PathKit.getWebRootPath() + "/upload/");
	// 异步上传时，无法通过uploadFile.getFileName()获取文件名
	String fileName = file.getFileName();

	// 异步上传时，无法通过File source = uploadFile.getFile();获取文件
	File source = new File(PathKit.getWebRootPath() + "/upload/" + fileName); // 获取临时文件对象

	String extension = fileName.substring(fileName.lastIndexOf("."));
	
	String timeStr = DateUtil.getTodayStr();
	String savePath = "/front/upload/" + timeStr;

	JSONObject json = new JSONObject();

	if (".png".equals(extension) || ".jpg".equals(extension) || ".gif".equals(extension) || "jpeg".equals(extension) || ".bmp".equals(extension))
	{
	    fileName = DateUtil.getTodaySecNum() + extension;

	    try
	    {
		FileInputStream fis = new FileInputStream(source);

		File targetDir = new File(PathKit.getWebRootPath() + savePath);
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
		byte [] bts = new byte[1024 * 20];
		while (fis.read(bts, 0, 1024 * 20) != -1)
		{
		    fos.write(bts, 0, 1024 * 20);
		}

		fos.close();
		fis.close();
		source.delete();
		json.put("error", 0);
		json.put("state", "SUCCESS");
		json.put("title", fileName);
		json.put("original", fileName);
		json.put("url", domain_name+"upload/" + timeStr + "/" + fileName); // 相对地址，显示图片用

	    } catch (FileNotFoundException e)
	    {
		json.put("success", "FAIL");
	    } catch (IOException e)
	    {
		json.put("success", "FAIL");
	    }
	}
	renderJson(json.toString());
    }

    /**
	 * 上传新闻封面
	 */
	public void uploadNewsPic(){
		getFile("imgFile", PathKit.getWebRootPath() + "/temp", 20 * 1024 * 1024, "utf-8"); // 最大上传20M的图片
		// 异步上传时，无法通过uploadFile.getFileName()获取文件名
		String fileName = getPara("fileName");
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1); // 去掉路径
		String extension = fileName.substring(fileName.lastIndexOf("."));

		String src = "web/article/pic";
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
    
    // 删除图片
    public void delImg() throws Exception
    {
//	Map<String, String []> paraMap = getParaMap();
	renderJson("success", true);
    }

    // 查询文章列表
    public void articleList() throws Exception
    {
		String pageNum = this.getPara("pageNum");
		if (pageNum == null)
		{
		    pageNum = "1";
		}
		List<WebColumn> columns = WebColumnService.me().getFirstWebColumns();
		Page<Article> page = ArticleService.me().getArticles(Integer.valueOf(pageNum),null);
		List<Article> articles = page.getList();
		int pageNumber = page.getPageNumber();
		int pageSize = page.getPageSize();
		int totalPage = page.getTotalPage();
		int totalRow = page.getTotalRow();
	
		setAttr("columns", columns);
		setAttr("articles", articles);
		setAttr("pageNumber", pageNumber);
		setAttr("pageSize", pageSize);
		setAttr("totalPage", totalPage);
		setAttr("totalRow", totalRow);
		render("/sysadmin/website/article/articlelist.html");
    }
    /**
     * 根据一级栏目查询二级栏目
     */
    public void getSecondColumn(){
    	String columnId = this.getPara("columnId");
    	List<WebColumn> list = WebColumnService.me().getWebColumnsByParent(columnId);
    	if(list.size() > 0){
    		setAttr("error", 0);
    		setAttr("list", list);
    		renderJson();
    	}else{
    		setAttr("error", 1);
    		setAttr("msg", "二级栏目为空");
    		renderJson();
    	}
    }
    
    // 查询文章列表
    public void articleList1() throws Exception
    {
    	int draw = this.getParaToInt("draw");
    	int start = this.getParaToInt("start");
    	int length = this.getParaToInt("length");
    	String csrfmiddlewaretoken = this.getPara("csrfmiddlewaretoken");
    	String firstClass = this.getPara("first_class");
    	String secondClass = this.getPara("second_class");
    	String title = this.getPara("title");
    	int pageNum = start/length + 1;
    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("firstClass", firstClass);
    	params.put("secondClass", secondClass);
    	params.put("title", title);
    	
    	Page<Article> page = ArticleService.me().getArticles(Integer.valueOf(pageNum),params);
    	List<Article> articles = page.getList();
    	int pageNumber = page.getPageNumber();
    	int pageSize = page.getPageSize();
    	int totalPage = page.getTotalPage();
    	int totalRow = page.getTotalRow();
    	
    	setAttr("list", articles);
    	setAttr("draw", draw);
    	setAttr("pageNumber", pageNumber);
    	setAttr("pageSize", pageSize);
    	setAttr("totalPage", totalPage);
    	setAttr("totalRow", totalRow);
    	renderJson();
    }

    // 重新编辑文章
    public void editArticle() throws Exception
    {
	String articleKey = this.getPara("key");
	Article article = ArticleService.me().getArticle(articleKey); // 文章对象
	List<WebMaping> mappings = WebMapingService.me().getMappings(articleKey);// 与当前文章对应的所有栏目和专题
	                                                                         // 所有的专题列表（还没有此项）
	List<WebColumn> webColumns = WebColumnService.me().getWebColumns(); // 所有的栏目列表

	List<String> columns = new ArrayList<>();
	Iterator<WebMaping> iter = mappings.iterator();
	while (iter.hasNext())
	{
	    WebMaping mapping = iter.next();
	    columns.add(mapping.getStr("column_id"));
	}
	if(article.getStr("imgurl") != null){
//		int i = article.getStr("imgurl").indexOf(getRequest().getContextPath());
//		String sub = article.getStr("imgurl").substring(i+getRequest().getContextPath().length());
		article.set("imgurl",article.getStr("imgurl"));
	}
	setAttr("article", article);
	setAttr("mappings", mappings);
	setAttr("columns", columns);
	setAttr("webColumns", webColumns);
	// setAttr("", value);所有的专题列表（还没有此项）
	render("/sysadmin/website/article/articleedit1.html");
    }

    /**
     * 修改文章信息
     */
    public void updateArticle() throws Exception
    {
	try
	{
	    String articleKey = this.getPara("article_key");
	    String id = this.getPara("id");
	    String title = this.getPara("title");
	    String content = this.getPara("content");
	    String identification = this.getPara("identification");
	    String issue = this.getPara("issue");
	    String comment = this.getPara("comment");
	    String keywords = this.getPara("keywords");
	    String tags = this.getPara("tags");
	    String description = this.getPara("description");
	    String longitude = this.getPara("longitude");
	    String latitude = this.getPara("latitude");
	    String remarks = this.getPara("remarks");
	    String metadata1 = this.getPara("metadata1");
	    String metadata2 = this.getPara("metadata2");
	    String recycle = this.getPara("recycle");
	    String template = this.getPara("template");
	    String recommend = this.getPara("recommend");
	    String newsPic = this.getPara("news_pic");
//		newsPic = getRequest().getContextPath() + newsPic;

	    Map<String, String> map = new HashMap<String, String>();
	    map.put("id", id);
	    map.put("articleKey", articleKey);
	    map.put("title", title);
	    map.put("content", content);
	    map.put("identification", identification);
	    map.put("issue", issue);
	    map.put("comment", comment);
	    map.put("keywords", keywords);
	    map.put("tags", tags);
	    map.put("description", description);
	    map.put("longitude", longitude);
	    map.put("latitude", latitude);
	    map.put("remarks", remarks);
	    map.put("metadata1", metadata1);
	    map.put("metadata2", metadata2);
	    map.put("recycle", recycle);
	    map.put("template", template);
	    map.put("recommend", recommend);
	    map.put("newsPic", newsPic);
	    boolean updateArticle = ArticleService.me().updateArticle(map);// 更新文章信息

	    List<WebMaping> mappings = WebMapingService.me().getMappings(articleKey);
	    boolean b = WebMapingService.me().delMapping(mappings);// 删除文章-栏目-专题对应关系

	    String [] class1s = this.getParaValues("class1");// 以下为建立文章-栏目-专题新的关系
	    String [] class2s = this.getParaValues("class2");
	    String [] class3s = this.getParaValues("class3");
	    String [] specialIds = this.getParaValues("special_id");
	    boolean bolclass1 = true;
	    boolean bolclass2 = true;
	    boolean bolclass3 = true;
	    if (class1s != null && class1s.length > 0)
	    {
		for (String class1 : class1s)
		{
		    bolclass1 = WebMapingService.me().saveMappingColumn(articleKey, class1);
		}
	    }
	    if (class2s != null && class2s.length > 0)
	    {
		for (String class2 : class2s)
		{
		    WebMapingService.me().saveMappingColumn(articleKey, class2);
		}
	    }
	    if (class3s != null && class3s.length > 0)
	    {
		for (String class3 : class3s)
		{
		    bolclass2 = WebMapingService.me().saveMappingColumn(articleKey, class3);
		}
	    }
	    if (specialIds != null && specialIds.length > 0)
	    {
		for (String specialId : specialIds)
		{
		    bolclass3 = WebMapingService.me().saveMappingSpecial(articleKey, specialId);
		}
	    }
	    if (updateArticle != false && b != false && bolclass1 != false && bolclass2 != false && bolclass3 != false)
	    {
		renderJson("msg", "修改成功！");
	    } else
	    {
		renderJson("msg", "修改失败！");
	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	    renderJson("msg", "修改失败！");
	}
    }

    /**
     * 单个删除文章
     */
    public void delArticle() throws Exception
    {
	String id = this.getPara("id");
	boolean b = ArticleService.me().delArticle(id);
	if (b)
	{
	    renderJson("msg", "删除成功！");
	} else
	{
	    renderJson("msg", "删除失败！");
	}
    }

    /**
     * 批量删除文章
     */
    public void delBatch() throws Exception
    {
	String [] ids = this.getParaValues("id");
	boolean delArticle;
	try
	{
	    for (String id : ids)
	    {
		delArticle = ArticleService.me().delArticle(id);
		if (!delArticle)
		{
		    renderJson("msg", "删除失败！");
		    return;
		}
	    }
	    renderJson("msg", "删除成功！");
	} catch (Exception e)
	{
	    e.printStackTrace();
	    renderJson("msg", "删除失败！");
	}
    }
}
