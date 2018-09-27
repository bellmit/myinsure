package com.myinsure.admin.website.web;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;

/**
 * Admin model.
 */
@SuppressWarnings("serial")
public class WebColumn extends Model<WebColumn>
{

	public static final WebColumn dao = new WebColumn();

	List<WebColumn> webColumns = null;

	public void setWebColumns(List<WebColumn> webColumns)
	{
		this.webColumns = webColumns;
	}

	public List<WebColumn> getWebColumns()
	{
		if (webColumns == null)
		{
			webColumns = new ArrayList<WebColumn>();
		}
		return webColumns;
	}

}