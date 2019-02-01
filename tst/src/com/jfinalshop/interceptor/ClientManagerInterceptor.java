package com.jfinalshop.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.util.TextUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinalshop.controller.admin.MemberController;
import com.jfinalshop.model.Admin;
import com.jfinalshop.service.AdminService;
import com.jfinalshop.shiro.core.SubjectKit;

public class ClientManagerInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation invc) {
		Controller c=invc.getController();
		if(SubjectKit.isAuthed()) {
			Admin a=new AdminService().getCurrent();
			if(!TextUtils.isEmpty(a.getName())&&a.getName().trim().equals("管理员")) {
				invc.invoke();
			}else {
				HttpServletResponse response=c.getResponse();
				response.setCharacterEncoding("UTF-8");
				try {
					response.getWriter().print("<script>"
							+ "window.onload=function(){alert('权限不够，现在跳转至登陆页！');"
							+ "window.location.href='/admin/login.jhtml';}</script>");
					//response.sendRedirect("/admin/login.jhtml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					c.redirect("/admin/login.jhtml");
				}
			}
		}else {
			c.redirect("/admin/login.jhtml");
		}
	}

}
