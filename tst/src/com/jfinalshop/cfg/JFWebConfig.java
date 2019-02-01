package com.jfinalshop.cfg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.FakeStaticHandler;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.tx.TxByActionKeyRegex;
import com.jfinal.plugin.activerecord.tx.TxByActionKeys;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinalshop.FreeMarkerExceptionHandler;

import com.jfinalshop.controller.wap.IndexController;
import com.jfinalshop.model._MappingKit;
import com.jfinalshop.security.MyJdbcAuthzService;
import com.jfinalshop.shiro.core.ShiroInterceptor;
import com.jfinalshop.shiro.core.ShiroPlugin;
import com.jfinalshop.shiro.core.SubjectKit;
import com.jfinalshop.shiro.freemarker.ShiroTags;
import com.jfinalshop.template.directive.AdPositionDirective;
import com.jfinalshop.template.directive.ArticleCategoryChildrenListDirective;
import com.jfinalshop.template.directive.ArticleCategoryParentListDirective;
import com.jfinalshop.template.directive.ArticleCategoryRootListDirective;
import com.jfinalshop.template.directive.ArticleListDirective;
import com.jfinalshop.template.directive.AttributeListDirective;
import com.jfinalshop.template.directive.BrandListDirective;
import com.jfinalshop.template.directive.ConsultationListDirective;
import com.jfinalshop.template.directive.CurrentMemberDirective;
import com.jfinalshop.template.directive.FlashMessageDirective;
import com.jfinalshop.template.directive.FriendLinkListDirective;
import com.jfinalshop.template.directive.GoodsListDirective;
import com.jfinalshop.template.directive.MemberAttributeListDirective;
import com.jfinalshop.template.directive.NavigationListDirective;
import com.jfinalshop.template.directive.PaginationDirective;
import com.jfinalshop.template.directive.ProductCategoryChildrenListDirective;
import com.jfinalshop.template.directive.ProductCategoryParentListDirective;
import com.jfinalshop.template.directive.ProductCategoryRootListDirective;
import com.jfinalshop.template.directive.PromotionListDirective;
import com.jfinalshop.template.directive.ReviewListDirective;
import com.jfinalshop.template.directive.SeoDirective;
import com.jfinalshop.template.directive.TagListDirective;
import com.jfinalshop.template.method.AbbreviateMethod;
import com.jfinalshop.template.method.CurrencyMethod;
import com.jfinalshop.template.method.MessageMethod;
import com.jfinalshop.util.SystemUtils;
import com.jfinalshop.validator.ValidationPlugin;
import com.jfinalshop.validator.ValidatorInterceptor;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.StandardCompress;

public class JFWebConfig extends JFinalConfig {

	/**
	 * 供Shiro插件使用。
	 */
	Routes routes;
	
	@Override
	public void configConstant(Constants constants) {
		loadPropertyFile("jfinalshop.properties");
		constants.setDevMode(getPropertyToBoolean("devMode", true));
		//constants.setEncoding("UTF-8");
		constants.setI18nDefaultBaseName("i18n");
		constants.setViewType(ViewType.FREE_MARKER);
		constants.setViewExtension(".ftl");
		constants.setI18nDefaultLocale(getProperty("locale"));
		constants.setMaxPostSize(1024*1024*100);
		constants.setError401View("/admin/common/error.ftl");
		constants.setError403View("/admin/common/unauthorized.ftl");
		constants.setError404View("/admin/common/error.ftl");
		constants.setError500View("/admin/common/error.ftl");
		constants.setMaxPostSize(104857600);
	}

	@Override
	public void configRoute(Routes routes) {
		new EhCachePlugin().start();//提前初始化
		this.routes = routes;
		
		//routes.add("/", IndexController.class);
		AutoBindRoutes abr = new AutoBindRoutes();
		// api 模块
		abr.addJars("jfinalshop-4.0-api-1.0-SNAPSHOT.jar");
		// 忽略不自动扫描的Controller
		List<Class<? extends Controller>> clazzes = new ArrayList<Class<? extends Controller>>();
		clazzes.add(com.jfinalshop.controller.shop.BaseController.class);
		clazzes.add(com.jfinalshop.controller.admin.BaseController.class);
		clazzes.add(com.jfinalshop.controller.wap.BaseController.class);
		abr.addExcludeClasses(clazzes);
		routes.add(abr);
		
	}

	@Override
	public void configPlugin(Plugins plugins) {
		//String publicKey = getProperty("jdbc.publicKey");
		//String password = getProperty("jdbc.password");
		
		//配置druid连接池
		DruidPlugin druidDefault = new DruidPlugin(
		getProperty("jdbc.url"), 
		getProperty("jdbc.username"),
		//EncriptionKit.passwordDecrypt(publicKey, password),
		getProperty("jdbc.password"),
		getProperty("jdbc.driver"));
		// StatFilter提供JDBC层的统计信息
		druidDefault.addFilter(new StatFilter());

		// WallFilter的功能是防御SQL注入攻击
		WallFilter wallDefault = new WallFilter();

		wallDefault.setDbType("mysql");
		druidDefault.addFilter(wallDefault);
		
		druidDefault.setInitialSize(getPropertyToInt("db.default.poolInitialSize"));
		druidDefault.setMaxPoolPreparedStatementPerConnectionSize(getPropertyToInt("db.default.poolMaxSize"));
		druidDefault.setTimeBetweenConnectErrorMillis(getPropertyToInt("db.default.connectionTimeoutMillis"));
		plugins.add(druidDefault);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidDefault);
		plugins.add(arp);
		
		// 配置属性名(字段名)大小写不敏感容器工厂
		arp.setContainerFactory(new CaseInsensitiveContainerFactory());
		
		// 显示SQL
		//arp.setShowSql(true);
		
		// 所有配置在 MappingKit
		_MappingKit.mapping(arp);
		
		//Ehcache缓存
		plugins.add(new EhCachePlugin());
		
		//shiro权限框架，添加到plugin
		plugins.add(new ShiroPlugin(routes, new MyJdbcAuthzService()));
		
		// 参数校验插件
		plugins.add(new ValidationPlugin("validation.properties"));
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		// 添加shiro的过滤器到interceptor
		interceptors.add(new ShiroInterceptor());
		// 参数校全局拦截
		interceptors.add(new ValidatorInterceptor());
		//开启事物
		interceptors.add(new TxByActionKeyRegex("/Redeem.*"));
	}

	@Override
	public void configHandler(Handlers handlers) {
		handlers.add(new FakeStaticHandler(".jhtml"));
		DruidStatViewHandler dvh = new DruidStatViewHandler("/druid", new IDruidStatViewAuth() {
			public boolean isPermitted(HttpServletRequest request) {
				if (SubjectKit.hasRoleAdmin()) {
					return true;
				} else {
					return false;
				}
			}
		});
		handlers.add(dvh);
	}

	@Override
	public void afterJFinalStart() {
		try {
			Configuration cfg = FreeMarkerRender.getConfiguration();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("base", JFinal.me().getContextPath());
			map.put("showPowered", getProperty("show_powered"));
			map.put("setting", SystemUtils.getSetting());
			map.put("message", new MessageMethod());
			
			map.put("abbreviate", new AbbreviateMethod());
			map.put("currency", new CurrencyMethod());
			map.put("flash_message", new FlashMessageDirective());
			map.put("current_member", new CurrentMemberDirective());
			map.put("pagination", new PaginationDirective());
			map.put("seo", new SeoDirective());
			map.put("ad_position", new AdPositionDirective());
			map.put("member_attribute_list", new MemberAttributeListDirective());
			map.put("navigation_list", new NavigationListDirective());
			map.put("tag_list", new TagListDirective());
			map.put("friend_link_list", new FriendLinkListDirective());
			map.put("brand_list", new BrandListDirective());
			map.put("attribute_list", new AttributeListDirective());
			map.put("article_list", new ArticleListDirective());
			map.put("article_category_root_list", new ArticleCategoryRootListDirective());
			map.put("article_category_parent_list", new ArticleCategoryParentListDirective());
			map.put("article_category_children_list", new ArticleCategoryChildrenListDirective());
			map.put("goods_list", new GoodsListDirective());
			map.put("product_category_root_list", new ProductCategoryRootListDirective());
			map.put("product_category_parent_list", new ProductCategoryParentListDirective());
			map.put("product_category_children_list", new ProductCategoryChildrenListDirective());
			map.put("review_list", new ReviewListDirective());
			map.put("consultation_list", new ConsultationListDirective());
			map.put("promotion_list", new PromotionListDirective());
			map.put("compress", StandardCompress.INSTANCE);
			cfg.setSharedVaribles(map);
			
			cfg.setDefaultEncoding(getProperty("template.encoding"));
			cfg.setURLEscapingCharset(getProperty("url_escaping_charset"));
			cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
			cfg.setWhitespaceStripping(true);
			cfg.setClassicCompatible(true);
			cfg.setNumberFormat(getProperty("template.number_format"));
			cfg.setBooleanFormat(getProperty("template.boolean_format"));
			cfg.setDateTimeFormat(getProperty("template.datetime_format"));
			cfg.setDateFormat(getProperty("template.date_format"));
			cfg.setTimeFormat(getProperty("template.time_format"));
			cfg.setObjectWrapper(new BeansWrapper(Configuration.VERSION_2_3_23));
			cfg.setTemplateUpdateDelayMilliseconds(getPropertyToLong("template.update_delay"));
			cfg.setTemplateExceptionHandler(new FreeMarkerExceptionHandler());
			cfg.setSharedVariable("shiro", new ShiroTags());
			
			cfg.setServletContextForTemplateLoading(JFinal.me().getServletContext(), getProperty("template.loader_path"));
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
		super.afterJFinalStart();
	}

	@Override
	public void configEngine(Engine me) {
		
	}
}
