package com.jfinalshop.controller.XhxManagementSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.nlpcn.commons.lang.util.MD5;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Message;
import com.jfinalshop.controller.admin.BaseController;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.OrderItem;
import com.xiaoleilu.hutool.http.HttpRequest;

@ControllerBind(controllerKey = "KuaiDi100")
public class KuaiDi100 extends Controller {
	/**
	 * 删除订单项 需要订单项id
	 */
	public void delete() {
		String id = getPara("id");
		if(TextUtils.isEmpty(id)) {
			this.renderJson("error");
			return;
		}
		try {
		OrderItem.dao.findById(id).delete();
		renderJson("success");
		} catch (Exception e) {
		renderJson(Message.error("admin.common.delete"));
		}
		
	}
	
	public void ExpressData() {
		String code = getPara("code");
		String order = getPara("order");
		if(TextUtils.isEmpty(code)||TextUtils.isEmpty(order)) {
			this.renderJson("error");
			return;
		}
		try {
		String	result	= getURLkuaidi100(code,order);
		System.out.println("这是快递1002:"+getURLkuaidi1002(code, order));
		this.renderJson(result);
		} catch (Exception e) {
		this.renderJson("出现未知异常");	
		}
	}
	
	public void view() {
		System.out.println("来了吗");
		Map<String,Integer> date = new HashMap<>();
		date.put("近七天",7);
		date.put("一个月",30);
		date.put("三个月",90);
		date.put("近半年",180);
		date.put("一整年",365);
		System.out.println("打野了吗");
		
		setAttr("member", Member.dao.findById(18));
		setAttr("date", date);
		render("/admin/xxh_test/barsimple.html");
	}
	
	public void query() {
			
		/**
		 * 身份验证id :6B96DC1E800E1E4B1B7323F3FD9D8B01
		 * 快递编号com：yuantongkuaiyun
		 * 运单号 nu：802939949345599680
		 * 返回类型show： 
			0：返回json字符串， 
			1：返回xml对象， 
			2：返回html对象， 
			3：返回text文本。 
		   返回条数muti:
			1:返回多行完整的信息， 
			0:只返回一行信息。
		 排序order:排序： 
			desc：按时间由新到旧排列， 
			asc：按时间由旧到新排列。 
		 */
			try
			{
				URL url= new URL("http://api.kuaidi100.com/api?id=6B96DC1E800E1E4B1B7323F3FD9D8B01&key=egyMlgMa6923&com=yuantong&num=802939949345599680&show=0&muti=1&order=desc");
				URLConnection con=url.openConnection();
				System.out.println(con);
				
				 con.setAllowUserInteraction(false);
				 
				   InputStream urlStream = url.openStream();
				   System.out.println(urlStream);
				   String type = con.guessContentTypeFromStream(urlStream);
				   System.out.println(type);
				   
				   String charSet=null;
				   
				   if (type == null) {
				    type = con.getContentType();
				    System.out.println(type);
				   }
				   if (type == null || type.trim().length() == 0 ) {
					   System.out.println("走这里了吗》》》》	");
					   //|| type.trim().indexOf("text/html") < 0
				    return ;
				   }
				   System.out.println("结束了吗？？？");
				   if(type.indexOf("charset=") > 0) {
				    charSet = type.substring(type.indexOf("charset=") + 8);
				   }
				   byte b[] = new byte[10000];
				   int numRead = urlStream.read(b);
				  String content = new String(b, 0, numRead);
				   while (numRead != -1) {
				    numRead = urlStream.read(b);
				    if (numRead != -1) {
				     //String newContent = new String(b, 0, numRead);
				     String newContent = new String(b, 0, numRead, charSet);
				     content += newContent;
				    }
				   }
				   this.renderJson(content);
				  System.out.println("content:" + content);
				   urlStream.close();
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
	}
	
	public void kdi100() {
		/**
		 * 身份验证id :6B96DC1E800E1E4B1B7323F3FD9D8B01
		 * 快递编号com：yuantongkuaiyun
		 * 运单号 nu：802939949345599680
		 * key:egyMlgMa6923
		 * 返回类型show： 
			0：返回json字符串， 
			1：返回xml对象， 
			2：返回html对象， 
			3：返回text文本。 
		   返回条数muti:
			1:返回多行完整的信息， 
			0:只返回一行信息。
		 排序order:排序： 
			desc：按时间由新到旧排列， 
			asc：按时间由旧到新排列。 
		 */
		String param ="{\"com\":\"yuantongkuaiyun\",\"num\":\"802939949345599680\"}";
		String customer ="6B96DC1E800E1E4B1B7323F3FD9D8B01";
		String key = "egyMlgMa6923";
		String sign = MD5.code(param+key+customer);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("param",param);
		params.put("sign",sign);
		params.put("customer",customer);
		
		System.out.println(params);
		/*String resp;
		try {*/
			
		HttpRequest	cc = new HttpRequest("https://m.kuaidi100.com/index_all.html?type=YTO&postid=802939949345599680");
			System.out.println(cc);
		/*	resp = new HttpRequest().postData("http://poll.kuaidi100.com/poll/query.do", params, "utf-8").toString();
			System.out.println(resp);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public static String getURLkuaidi100(String code,String order) throws Exception {
		String strURL = "http://www.kuaidi100.com/query?type="+code+"&postid="+order+"";
        URL url = new URL(strURL);
        HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.connect();
            
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"utf-8"));
        String line;
        
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
        	buffer.append(line);
        }
        reader.close();
        httpConn.disconnect();
         
       return buffer.toString();
	}
	//https://poll.kuaidi100.com/pollquery/testapi.do?code=251465516240&company=shunfenghk&schema=json&resultv2=0&method=normal
	public static String getURLkuaidi1002(String code,String order) throws Exception {
		String strURL = "https://poll.kuaidi100.com/pollquery/testapi.do?code=251465516240&company=shunfenghk&schema=json&resultv2=0&method=normal";
        URL url = new URL(strURL);
        HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.connect();
            
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"utf-8"));
        String line;
        
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
        	buffer.append(line);
        }
        reader.close();
        httpConn.disconnect();
         
       return buffer.toString();
	}

	
	/**
	 * 程序中访问http数据接口
	 */
	public static String getURLContent(String urlStr) {
		/** 网络的url地址 */
		URL url = null;
		/** http连接 */
		HttpURLConnection httpConn = null;
		/**//** 输入流 */
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(urlStr);
			in = new BufferedReader(new InputStreamReader(url.openStream(), "GBK"));
			String str = null;
			while ((str = in.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception ex) {
 
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		String result = sb.toString();
		System.out.println(result);
		return result;
	}

}
