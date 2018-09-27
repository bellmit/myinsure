package com.myinsure.admin.website.article;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.myinsure.admin.base.SysKeyIdService;
import com.myinsure.utils.DateUtil;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;

public class ArticleService
{
    @SuppressWarnings("unused")
    private static final Log log = Log.getLog(ArticleService.class);

    private static final ArticleService articleService = new ArticleService();

    public static ArticleService me()
    {
	return articleService;
    }

    /**
     * 保存文章信息
     * 
     * @param map
     * @return
     */
    public String saveAtrticle(Map<String, String> map) throws Exception
    {
	Article article = new Article();
	article.set("title", map.get("title"));
	article.set("content", map.get("content"));
	article.set("identification", map.get("identification"));
	article.set("issue", map.get("issue"));
	article.set("comment", map.get("comment"));
	article.set("keywords", map.get("keywords"));
	article.set("tags", map.get("tags"));
	article.set("description", map.get("description"));
	article.set("longitude", map.get("longitude"));
	article.set("latitude", map.get("latitude"));
	article.set("remarks", map.get("remarks"));
	article.set("metadata1", map.get("metadata1"));
	article.set("metadata2", map.get("metadata2"));
	article.set("recycle", map.get("recycle"));
	article.set("template", map.get("template"));
	article.set("recommend", map.get("recommend"));
	article.set("imgurl", map.get("newsPic"));
	article.set("updatetime", DateUtil.getTodaySec());
	article.set("addtime", DateUtil.getTodaySec());

	String articleKey = String.valueOf(SysKeyIdService.me().generateKeyMysql("web_article"));// 作为文章表除id外的唯一标识
	article.set("article_key", articleKey);
	
	String links = DateUtil.getFullYear() + "/" + DateUtil.getMonth() + "/" + DateUtil.getDay() + "/" + articleKey + ".html";
	article.set("links", links);//新闻链接
	boolean b = article.save();
	if (b)
	{
	    return articleKey;
	}
	return null;
    }

    /**
     * 分页查询所有文章
     * @return
     */
    public Page<Article> getArticles(Integer pageNum,Map<String, String> params) throws Exception
    {
		StringBuilder where = new StringBuilder(" from web_article wa ");
		if(params != null){
			if(params.get("title") != null && !"".equalsIgnoreCase(params.get("title"))){//如果标题不为null，则按照标题查询
				where.append(" where wa.title like '%" + params.get("title") + "%'");
			}else{//否则根据栏目查询
				if(params.get("secondClass") != null && !"".equalsIgnoreCase(params.get("secondClass"))){//如果二级栏目不为null，则按照二级栏目查询
					where.append(" left join web_mapping wm on wm.article_key = wa.article_key ");
					where.append(" where wa.recycle = 2 and wm.column_id = " + params.get("secondClass"));
				}else{//否则按照一级栏目查询
					if(params.get("firstClass") != null && !"".equalsIgnoreCase(params.get("firstClass"))){
						where.append(" left join web_mapping wm on wm.article_key = wa.article_key ");
						where.append(" where wm.column_id = " + params.get("firstClass"));
					}
				}
			}
		}
		where.append(" order by wa.id desc");
		Page<Article> page = Article.dao.paginate(pageNum, 10, "select * ", where.toString());
		return page;
    }

    /**
     * 文章详情查询
     * @return
     */
    public Article getArticle(String articleKey) throws Exception
    {
	String sql = "select * from web_article where article_key = ?";
	Article article = Article.dao.findFirst(sql, articleKey);
	return article;
    }

    /**
     * 更新文章信息
     * @param map
     * @return
     */
    public boolean updateArticle(Map<String, String> map) throws Exception
    {
	Article article = ArticleService.me().getArticle(map.get("articleKey"));
	article.set("title", map.get("title"));
	article.set("content", map.get("content"));
	article.set("identification", map.get("identification"));
	article.set("issue", map.get("issue"));
	article.set("comment", map.get("comment"));
	article.set("keywords", map.get("keywords"));
	article.set("tags", map.get("tags"));
	article.set("description", map.get("description"));
	article.set("longitude", map.get("longitude"));
	article.set("latitude", map.get("latitude"));
	article.set("remarks", map.get("remarks"));
	article.set("metadata1", map.get("metadata1"));
	article.set("metadata2", map.get("metadata2"));
	article.set("recycle", map.get("recycle"));
	article.set("template", map.get("template"));
	article.set("recommend", map.get("recommend"));
	article.set("imgurl", map.get("newsPic"));
	article.set("updatetime", DateUtil.getTodaySec());
	boolean b = article.update();
	return b;
    }

    /**
     * 根据id删除文章
     * 
     * @param id
     * @return
     */
    public boolean delArticle(String id) throws Exception
    {
	boolean b = Article.dao.deleteById(id);
	return b;
    }

    /**
     * 查询所有文章
     */
    public List<Article> getArticles()
    {
	String sql = "select id,title,ctitle,keywords,description,content,updatetime,article_key,links from web_article order by id desc";
	List<Article> list = Article.dao.find(sql);
	return list;
    }
}














