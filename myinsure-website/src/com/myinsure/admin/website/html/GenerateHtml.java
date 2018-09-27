package com.myinsure.admin.website.html;

import java.util.Iterator;
import java.util.List;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.myinsure.admin.website.article.Article;
import com.myinsure.admin.website.article.ArticleService;
import com.myinsure.admin.website.web.WebColumn;
import com.myinsure.admin.website.web.WebColumnService;
import com.myinsure.admin.website.web.WebConfig;
import com.myinsure.admin.website.web.WebConfigService;
import com.myinsure.front.web.WebController;
import com.myinsure.front.web.WebSitService;
import com.myinsure.front.web.column.WebFrontColumnService;

public class GenerateHtml extends Controller
{
	private final String address="http://myadmin.inssm.com/myinsure-website";
	Log log = Log.getLog(WebController.class);
	/**
	 * 跳转到静态页管理页面
	 */
	public void toGenerate(){
		WebConfig webConfig = WebConfigService.me().getWebConfigByName("web_domain");
		setAttr("domain", webConfig.getStr("value"));
		render("generate_list.html");
	}
    /**
     * 生成网站的一级导航页
     */
    public void generateHtml(){
    	String columnId = this.getPara("columnId");
    	WebColumn webColumn = WebColumnService.me().getById(columnId);
    	String sourceHtml = webColumn.getStr("foldername");
    	WebConfig webConfig = WebConfigService.me().getWebConfigByName("web_domain");
    	String sysdomain = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" 
			    + getRequest().getServerPort() + getRequest().getContextPath();
    	String str = "";
    	boolean b = false;
    	try
		{
    		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
    		Configuration cfg = Configuration.defaultConfiguration();
    		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
    		Template t = WebSitService.me().generateHtml(gt,sourceHtml);
    		t.binding("cxt", getRequest().getContextPath());
    		t.binding("domain", webConfig.getStr("value"));
    		t.binding("sysdomain", sysdomain);
    		str = t.render();
    		String filePath = PathKit.getWebRootPath() + "/front/" + webColumn.getStr("out_url");
    		
    		b = WebSitService.me().outHtml(str, filePath);
		} catch (Exception e)
		{
			e.printStackTrace();
			renderJson("msg", "失败");
		}
    	if(b){
    		renderJson("msg", "成功");
    	}else{
    		renderJson("msg", "失败");
    	}
    }
    
    /**
     * 生成所有新闻页
     */
    public void generateNewsHtml(){
    	List<Article> articles = ArticleService.me().getArticles();
    	Iterator<Article> iter = articles.iterator();
    	while(iter.hasNext()){
    		Article article = iter.next();
    		newsHtml(article);
    	}
    	renderJson("msg", "成功");
    }
    /**
     * 生成指定的新闻页
     */
    public void generateOneNewsHtml(){
    	String articleKey = this.getPara("articleKey");
    	Article article = null;
    	JSONObject json=new JSONObject();
    	try
		{
			article = ArticleService.me().getArticle(articleKey);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
    	String b = "";
    	if(article != null){
    		b = newsHtml(article);
    	}
    	if(b!=null&&!b.equals("")){
    		json.put("url", b);
    		json.put("msg", "成功");
    		renderJson(json);
    	}else{
    		renderJson("msg", "失败");
    	}
    }
    /**
     * 生成新闻页共用方法
     * @param article
     * @return
     */
    public String newsHtml(Article article){
    	String filePath = "";
    	String fileUrl = "";
    	List<WebColumn> columns = WebColumnService.me().getByArticleKey(article.getStr("article_key"));
    	String sourceHtml = "news_detail";
    	WebConfig webConfig = WebConfigService.me().getWebConfigByName("web_domain");
    	WebConfig webConfig2 = WebConfigService.me().getWebConfigByName("sys_domain");
    	String str = "";
    	try
		{
    		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
    		Configuration cfg = Configuration.defaultConfiguration();
    		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
    		Template t = WebSitService.me().generateHtml(gt,sourceHtml);
    		t.binding("cxt", getRequest().getContextPath());
    		t.binding("domain", webConfig.getStr("value"));
    		t.binding("sysdomain", webConfig2.getStr("value"));
    		t.binding("article", article);
    		Iterator<WebColumn> iter = columns.iterator();
    		while(iter.hasNext()){
    			WebColumn webColumn = iter.next();
    			//查找当前导航所属的上级导航，及上级导航下的所有下级导航
    			WebColumn column = null;
    			if(webColumn.getStr("wap_nav") != null && !"".equalsIgnoreCase(webColumn.getStr("wap_nav"))){
    				column = WebFrontColumnService.me().findSonColumnByColumnId(webColumn.getStr("wap_nav"));
    			}
    			t.binding("sonColumn", webColumn);
    			t.binding("firstColumn", column);
        		str = t.render();
        		
    			if("文章模版".equalsIgnoreCase(webColumn.getStr("faclass"))){
    				filePath = PathKit.getWebRootPath() + "/front/" + webColumn.getStr("foldername") + ".html";
    				fileUrl=address+"/front/" + webColumn.getStr("foldername") + ".html";
    			}else{
    				filePath = PathKit.getWebRootPath() + "/front/static/" + webColumn.getStr("foldername") + "/" + article.getStr("links");
    				fileUrl=address+"/front/static/" + webColumn.getStr("foldername") + "/" + article.getStr("links");
    			}
    			WebSitService.me().outHtml(str, filePath);
    		}
		} catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
    	return fileUrl;
    }
}
