package com.jfinalshop.model;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.util.TextUtils;

public class Page<T> {
			private int currentPage=1;//当前页
			private String url="";//跳转URL
			private int pageCount=0;//总页数
			private int pageSize=20;//每页数据数量
			private int recordCount;//总记录数
			private int startPage=1;//首页
			private int endPage;//尾页
			private int startIndex;//开始索引
			private List<T> data;//携带数据
			private Map<String,Object>  params=new HashMap<>();//参数集合
			private String lastAddr="";
			public Page() {};
			
			public Page(int recordCount,String url,HttpServletRequest request) {
					this.pageCount=recordCount%pageSize==0?recordCount%pageSize:recordCount%pageSize+1;
					this.url=url;
					operation();
			}
			
			public void init(int  recordCount,String url,HttpServletRequest request) throws Exception {
				this.recordCount=recordCount;
				  pageCount=recordCount%pageSize==0?recordCount/pageSize:recordCount/pageSize+1;
				  this.url=url;
				  this.fullMember(request);
			}
			
			private void   operation() {
				     	    this.currentPage=currentPage>pageCount?pageCount:currentPage;
				     	    currentPage=currentPage<1?1:currentPage;
				     	    startIndex=(currentPage-1<0?0:currentPage-1)*20;
				     	    startPage=currentPage-2>0?currentPage-2:1;
				     	    endPage=currentPage+2>pageCount?pageCount:currentPage+2;
			}
			
			//注入成员
			public void fullMember(HttpServletRequest request) throws Exception {
					Method [] ms=Page.class.getMethods();
					String beanNameAndDot=firstToLower(Page.class.getSimpleName())+".";
					for(Method m:ms ) {
						  if(m.getName().startsWith("set")&&m.getName().length()>3) {
							  String paraName=beanNameAndDot+firstToLower(m.getName().substring(3));
							   Class<?>[] types=m.getParameterTypes();
							   if(types.length==1) {
								      if(request.getParameterMap().keySet().contains(paraName)) {
								    	  	String para=request.getParameter(paraName);
								    	  	Object nPara=convertPara(types[0],para);
								    	  	m.invoke(this, nPara);
								      }
							   }
						  }
					}
					operation();
					fullPara(request);
			}
			public Object convertPara(Class<?> t,String para) throws Exception {
				     if(t==Integer.class||t==Integer.TYPE) {
				    	 return Integer.valueOf(para);
				     }else if(t==Long.class||t==Long.TYPE) {
				    	 return Long.valueOf(para);
				     }else if(t==Double.class) {
				    	 return Double.valueOf(para);
				     }else if(java.sql.Date.class==t) {
				    	return new SimpleDateFormat("yyyy-MM-dd").parse(para);
				     }else if(java.util.Date.class==t) {
				    	 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(para);
				     }else if(t==String.class) {
				    	 return para;
				     }else if(t==Float.class) {
				    	 return Float.valueOf(para);
				     }
				    		throw new Exception("can not find correct ConverClass");
				    
			}
			public String firstToLower(String para) {
				  if(TextUtils.isEmpty(para))return null;
				  char a=para.charAt(0);
				  if(a>='A'&&a<='Z') {
					      a+=32;
					      char [] as=para.toCharArray();
						  as[0]=a;
						  return new String(as);
				  }
				  return null;
			}
			
			public void fullPara(HttpServletRequest request) {
							for(String key:request.getParameterMap().keySet()) {
								String start=firstToLower(Page.class.getSimpleName())+".";
								if(!key.startsWith(start)) {
									if(request.getParameter(key)!=null) {
										  this.params.put(key, request.getParameter(key));
									}
								}
							}
						
			}
			
			public String appendParams() {
				if(TextUtils.isEmpty(lastAddr)) {
					    for(Map.Entry<String, Object> entry:params.entrySet()) {
					      if(!entry.getValue().getClass().getSimpleName().equals(HashMap.class.getSimpleName()))
					    	 lastAddr+="&"+entry.getKey()+"="+entry.getValue();
					    }
				}
				    return lastAddr;
			}
			
			public int getCurrentPage() {
				return currentPage;
			}

			public void setCurrentPage(int currrentPage) {
				this.currentPage = currrentPage;
			}
			
			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public int getPageCount() {
				return pageCount;
			}

			public void setPageCount(int pageCount) {
				this.pageCount = pageCount;
			}

			public int getPageSize() {
				return pageSize;
			}

			public void setPageSize(int pageSize) {
				this.pageSize = pageSize;
			}

			public int getRecordCount() {
				return recordCount;
			}

			public void setRecordCount(int recordCount) {
				this.recordCount = recordCount;
			}

			public int getStartPage() {
				return startPage;
			}

			public void setStartPage(int startPage) {
				this.startPage = startPage;
			}

			public int getEndPage() {
				return endPage;
			}

			public void setEndPage(int endPage) {
				this.endPage = endPage;
			}

			public int getStartIndex() {
				return startIndex;
			}

			public void setStartIndex(int startIndex) {
				this.startIndex = startIndex;
			}

			public List<T> getData() {
				return data;
			}

			public void setData(List<T> data) {
				this.data = data;
			}

			public Map<String, Object> getParams() {
				return params;
			}

			public void setParams(Map<String, Object> params) {
				this.params = params;
			}
			
}
