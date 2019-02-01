package com.jfinalshop.controller.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Article;
import com.jfinalshop.model.ArticleCategory;
import com.jfinalshop.model.ClientAd;
import com.jfinalshop.model.SComment;
import com.jfinalshop.model.client.CommonSTUC;

@ControllerBind(controllerKey="/client/article")
public class Articles  extends Controller {
		
	
			public   void list() {
				      List<ArticleCategory> categorys=ArticleCategory.dao.find("select id,name  from article_category where grade=0 ");
				      List<ClientAd> ads=ClientAd.dao.find("select id,title,ad_img  from client_ad where status=? and ad_type=?",ClientAd.Status.Use.ordinal(),ClientAd.Type.ArticleBanner.getIndex());
				      List<Record> aritcls=Db.find("select a.id,a.title,a.img,a.goodcount,count(s.id) as count from article a left join s_comment s on a.id=s.article_id  and s.type=? group by  a.id limit 0,15",SComment.Type.Comment.ordinal());
				      try {
				    	  this.setAttr("category", categorys);
						this.setAttr("ad", ads);
						if(this.getPara("mid")!=null) {
							this.getRequest().getSession().setAttribute("mid",this.getPara("mid") );
						}
						this.setAttr("article", aritcls);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				      this.renderJsp("/WEB-INF/template/client/article/articles.jsp");
			}
			  
			public void search() {
				List<Record> articles=null;
				String id=this.getPara("id");
				String clause=this.getPara("clause");
				if(!TextUtils.isEmpty(id)) {
				 articles=Db.find("select a.id,a.title,a.img,count(s.id) as count,a.goodcount,seo_description  from (select id,title,img,goodcount,seo_description from article where article_category_id=?) a left join  s_comment s on a.id=s.article_id and  s.type=? group by a.id limit 0,15 ",id,SComment.Type.Comment.ordinal());
				}else if(!TextUtils.isEmpty(clause)) {
					 articles=Db.find("select a.id,a.title,a.img,count(s.id) as count,a.goodcount,a.seo_description from (select id,title,img,goodcount,seo_description  from article where title"
					 		+ "  like  '%"+clause+"%' or seo_keywords like '%"+clause+"%' ) a left join s_comment s on a.id=s.article_id and s.type=? group by a.id limit 0,15",SComment.Type.Comment.ordinal());
				}
				  this.renderJson("result",articles);
			}
			
			public void article() {
				   String id=this.getPara("id");
				 if(this.getPara("mid")!=null) {
					 this.getRequest().getSession().setAttribute("mid", this.getPara("mid"));
				 }
				   if(TextUtils.isEmpty(id)) {
					     this.setAttr("result","error");
				   }else {
					   Article at=Article.dao.findById(id);
					   SComment comments;
					   if(at==null||at.getTitle()==null) {
						    this.setAttr("result","nomore" );
					   }else {
						   List<Record> recors=Db.find("select   m.nickname,m.username,m.portrait,m.id,s.comment_str as comment,s.id as sid,s.praise,s.c_date as cdate  from  member m inner join s_comment s on"
						   		+ " m.id=s.member_id  and  s.type=?  and s.article_id=? order by s.praise desc",SComment.Type.Comment.ordinal(),id);
						   this.setAttr("comment", recors);
						   at.setHits(at.getHits()+1);
						   at.update();
						   this.setAttr("article", at);
					   }
				   }
				   this.setAttr("cmt", new SComment());
				   this.renderJsp("/WEB-INF/template/client/article/article.jsp");
			}
			/**
			 * 评论
			 */
			public  void comment() {
				Map<String,Object> result=new HashMap<>();
				SComment scomment=new SComment();
				    String mid=this.getPara("mid");
				    String commentstr=this.getPara("commentStr");
				    String aid=this.getPara("articleId");
				    String type=this.getPara("type");
				    String cid=this.getPara("commentId");
				    String appenderId=this.getPara("appenderId");
				    if(type.equals("3")&&!TextUtils.isEmpty(cid)) {
				    	      SComment cmt=SComment.dao.findById(cid);
				    	      if(cmt!=null&&!TextUtils.isEmpty(cmt.getCommentStr())) {
				    	    	  List<SComment> cmts=SComment.dao.find("select  id  from s_comment "
				    	    	  		+ "   where  member_id=? and article_id=? and appender_id=? and append_comment_id=?",mid,aid,appenderId,cid);
				    	    	  if(cmts!=null&&cmts.size()>0) {
				    	    		    result.put("status",CommonSTUC.Status.Exist );
				    	    	  }else {
				    	    		  cmt.setPraise(cmt.getPraise()+1);
				    	    		  SComment praise=new SComment();
					    	    	  praise.setAppendCommentId(Long.valueOf(cid));
					    	    	  praise.setCDate(new Date());
					    	    	  praise.setMemberId(Long.valueOf(mid));
					    	    	  praise.setArticleId(Long.valueOf(aid));
					    	    	  praise.setType(Integer.parseInt(type));
					    	    	  praise.setAppenderId(Long.valueOf(appenderId));
					    	    	  praise.setCommentStr("");
					    	    	  result.put("status", cmt.update()&&praise.save()?1:2);
				    	    	  }
				    	      }else {
				    	    	  result.put("status", 2);
				    	      }
				    }else {
				    	if(TextUtils.isEmpty(mid)||TextUtils.isEmpty(aid)||TextUtils.isEmpty(type)) {
					    	  result.put("status", CommonSTUC.Status.Fail.getIndex());
					    }else {
					    	scomment.setMemberId(Long.parseLong(mid));
					    	scomment.setArticleId(Long.parseLong(aid));
					    	scomment.setType(Integer.parseInt(type));
					    	scomment.setCDate(new Date());
					    	scomment.setAppenderId(type.equals("2")?Long.valueOf(appenderId):0);
					    	scomment.setAppendCommentId(type.equals("2")?Long.valueOf(cid):0);
					    	//scomment.setLevel(TextUtils.isEmpty(level)?false:Boolean.valueOf(level));
					    	scomment.setCommentStr(TextUtils.isEmpty(commentstr)?"":commentstr);
					    	result.put("status", scomment.save()?1:2);
					    }
				    }
				    this.renderJson(result);
			}
}
