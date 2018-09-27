package com.myinsure.admin.website.article;

import com.jfinal.plugin.activerecord.Model;

/**
 * Article model.
 */
@SuppressWarnings("serial")
public class Article extends Model<Article>
{
    public static final Article dao = new Article();
}