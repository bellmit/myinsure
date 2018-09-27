package com.myinsure.admin.ueditor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;

public class UeditorController extends Controller
{
	/**
	 * ueditor统一请求路径
	 */
	public void ueditor(){
		String configStr = reader("ueditor.json");
		JSONObject config = JSONObject.parseObject(configStr);
		config.put("state", "SUCCESS");
		renderJson(config);
	}
	/**
	 * 读取后台配置文件
	 * @param filePath
	 * @return
	 */
	public String reader(String filePath) {
        try {
        	InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
        	InputStreamReader reader = new InputStreamReader(resourceAsStream,"utf-8");
        	BufferedReader bufferedReader = new BufferedReader(reader);
        	
            StringBuilder txt = new StringBuilder();
            String lineTxt;
        	while ((lineTxt = bufferedReader.readLine()) != null) {
        		txt.append(lineTxt);
            }
        	return txt.toString();
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
