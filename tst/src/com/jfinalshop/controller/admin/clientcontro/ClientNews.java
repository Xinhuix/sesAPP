package com.jfinalshop.controller.admin.clientcontro;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.model.Article;
import com.jfinalshop.model.ArticleCategory;

@ControllerBind(controllerKey="/admin/clientcontro/news")
public class ClientNews extends Controller {
	
			public  void index() {
							List<ArticleCategory> lac=ArticleCategory.dao.find("select * from article_category");
							this.setAttr("acategorys", lac);
						this.renderJsp("/WEB-INF/template//admin/clientmanager/news/news.jsp");
			}
			
}
