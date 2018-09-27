package com.myinsure.front.web;

import java.util.List;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.myinsure.admin.website.article.Article;
import com.myinsure.admin.website.web.WebColumn;
import com.myinsure.admin.website.web.WebColumnService;
import com.myinsure.admin.website.web.WebConfig;
import com.myinsure.admin.website.web.WebConfigService;
import com.myinsure.front.web.article.WebArticleService;
import com.myinsure.front.web.column.WebFrontColumnService;
/**
 * 网站
 * @author 卢飞
 */
public class WebController extends Controller
{
    Log log = Log.getLog(WebController.class);
    /**
     * 跳转到列表页，二级导航页以列表形式存在，不生成静态页。
     * 列表页分为两种，一种为简单列表页，另一种为带图片的列表页
     */
    public void getNewses(){
    	String columnId = this.getPara("columnId");//二级导航id
    	Integer pageNumber = this.getPara("pageNumber") == null?1:this.getParaToInt("pageNumber");
    	WebColumn webColumn = WebColumnService.me().getById(columnId);
    	String sourceHtml = "";
    	if("简单列表".equalsIgnoreCase(webColumn.getStr("faclass"))){
    		sourceHtml = "news_list1";
    	}else{
    		sourceHtml = "news_list2";
    	}
    	WebConfig sysDomain = WebConfigService.me().getWebConfigByName("sys_domain");
		WebConfig webDomain = WebConfigService.me().getWebConfigByName("web_domain");
    	//查找当前二级导航所属的一级导航，及一级导航下的所有二级导航
    	WebColumn column = WebFrontColumnService.me().findSonColumnByColumnId(webColumn.getStr("wap_nav"));
    	//分页查找当前二级导航下的新闻
    	Page<Article> page = WebArticleService.me().findByColumnId(columnId, pageNumber);
    	List<Article> newsList = page.getList();
    	int totalPage = page.getTotalPage();
    	int totalRow = page.getTotalRow();
    	String str = "";
    	try
		{
    		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
    		Configuration cfg = Configuration.defaultConfiguration();
    		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
    		Template t = WebSitService.me().generateHtml(gt,sourceHtml);
    		t.binding("cxt", getRequest().getContextPath());
    		t.binding("sonColumn", webColumn);
    		t.binding("domain", webDomain.getStr("value"));
    		t.binding("sysdomain", sysDomain.getStr("value"));
    		t.binding("firstColumn", column);
    		t.binding("newsList", newsList);
    		t.binding("pageNumber", pageNumber);
    		t.binding("totalPage", totalPage);
    		t.binding("totalRow", totalRow);
    		str = t.render();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
    	renderHtml(str);
    }
    /**
     * 根据标题搜索新闻列表
     */
    public void searchNews(){
    	String columnId = this.getPara("columnId");//二级导航id
    	Integer pageNumber = this.getPara("pageNumber") == null?1:this.getParaToInt("pageNumber");
    	WebColumn webColumn = WebColumnService.me().getById(columnId);
    	String sourceHtml = "";
    	if("简单列表".equalsIgnoreCase(webColumn.getStr("faclass"))){
    		sourceHtml = "news_list1";
    	}else{
    		sourceHtml = "news_list2";
    	}
    	//查找当前二级导航所属的一级导航，及一级导航下的所有二级导航
    	WebColumn column = WebFrontColumnService.me().findSonColumnByColumnId(webColumn.getStr("wap_nav"));
    	//分页查找当前二级导航下的新闻
    	Page<Article> page = WebArticleService.me().findByColumnId(columnId, pageNumber);
    	List<Article> newsList = page.getList();
    	int totalPage = page.getTotalPage();
    	int totalRow = page.getTotalRow();
    	String str = "";
    	try
		{
    		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
    		Configuration cfg = Configuration.defaultConfiguration();
    		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
    		Template t = WebSitService.me().generateHtml(gt,sourceHtml);
    		t.binding("cxt", getRequest().getContextPath());
    		t.binding("sonColumn", webColumn);
    		t.binding("firstColumn", column);
    		t.binding("newsList", newsList);
    		t.binding("pageNumber", pageNumber);
    		t.binding("totalPage", totalPage);
    		t.binding("totalRow", totalRow);
    		str = t.render();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
    	renderHtml(str);
    }
    
}





