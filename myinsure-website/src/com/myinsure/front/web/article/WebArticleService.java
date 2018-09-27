package com.myinsure.front.web.article;

import java.util.List;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import com.myinsure.admin.website.article.Article;
import com.myinsure.admin.website.carousel.WebImage;

public class WebArticleService
{
    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(WebArticleService.class);

    private static final WebArticleService articleService = new WebArticleService();

    public static WebArticleService me()
    {
	return articleService;
    }
    /**
     * 根据栏目查找文章
     * @return
     */
    public List<Article> findByColumn(String param,Integer size){
    	String sql = " from web_article wa " +
    			" left join web_mapping wm on wa.article_key = wm.article_key " +
    			" left join web_column wc on wc.id = wm.column_id " +
    			" where wc.name = '" + param + "' order by wa.recommend, wa.article_key desc";
		Page<Article> page = Article.dao.paginate(1, size, "select wa.* ", sql);
		List<Article> list = page.getList();
		return list;
    }
    /**
     * 根据导航的id分页查询其下的新闻
     * @return
     */
    public Page<Article> findByColumnId(String columnId,Integer pageNum){
    	String sql = " from web_article wa " +
    			" left join web_mapping wm on wa.article_key = wm.article_key " +
    			" left join web_column wc on wc.id = wm.column_id " +
    			" where wc.id = '" + columnId + "' order by wa.recommend, wa.article_key desc";
		Page<Article> page = Article.dao.paginate(pageNum, 20, "select wa.* ", sql);
		return page;
    }

    
}
