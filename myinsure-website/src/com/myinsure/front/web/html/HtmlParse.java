package com.myinsure.front.web.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.beetl.core.Template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myinsure.admin.website.article.Article;
import com.myinsure.admin.website.carousel.WebImage;
import com.myinsure.admin.website.web.WebColumn;
import com.myinsure.front.web.article.WebArticleService;
import com.myinsure.front.web.carousel.WebImageService;
import com.myinsure.front.web.column.WebFrontColumnService;

public class HtmlParse
{
	/**
	 * 将HTML转为字符串
	 * @param filePath html文件路径
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String htmlToString(String filePath){
         File file = new File(filePath);  
         InputStream input = null;
         try {
        	 input = new FileInputStream(file);
         } catch (FileNotFoundException e) {
        	 e.printStackTrace();
         }  
         StringBuffer buffer = new StringBuffer();  
         byte[] bytes = new byte[1024];
         try {
             for(int n ; (n = input.read(bytes))!=-1 ; ){  
                 buffer.append(new String(bytes,0,n,"UTF-8"));  
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
         return buffer.toString();
    }
	/**
	 * 获取匹配到的字符串,并将字符串转为json对象
	 * @return
	 */
	public static List<JSONObject> getTags(String html,String rgex){
		List<JSONObject> list = new ArrayList<JSONObject>();    
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(html);    
        while (m.find()) { 
        	JSONObject tag = JSON.parseObject(m.group(1));
            list.add(tag);
        }
        html = html.replaceAll(rgex,"");//将获取到的字符串替换为空串
        return list;
	}
	
}











