package com.myinsure.common;

import java.io.File;

import com.myinsure.admin.base.SysKeyId;
import com.myinsure.admin.fun.SysFun;
import com.myinsure.admin.fun.SysRoleFun;
import com.myinsure.admin.logs.Syslogs;
import com.myinsure.admin.module.SysModule;
import com.myinsure.admin.role.SysRole;
import com.myinsure.admin.role.SysUserRole;
import com.myinsure.admin.shiro.jfinl_shiro.ShiroInterceptor;
import com.myinsure.admin.shiro.jfinl_shiro.ShiroPlugin;
import com.myinsure.admin.site.SysSite;
import com.myinsure.admin.sys_project.SysProject;
import com.myinsure.admin.user.SysUser;
import com.myinsure.admin.website.article.Article;
import com.myinsure.admin.website.carousel.WebImage;
import com.myinsure.admin.website.company.WebCompany;
import com.myinsure.admin.website.institution.Institution;
import com.myinsure.admin.website.web.WebColumn;
import com.myinsure.admin.website.web.WebConfig;
import com.myinsure.admin.website.webmap.WebMaping;
import com.myinsure.handler.JHandler;
import com.myinsure.interceptor.AdminInterceptor;
import com.myinsure.utils.ReadPropertity;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.DiskStoreConfiguration;
import org.beetl.ext.jfinal3.JFinal3BeetlRenderFactory;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;

/**
 * 系统全局配置
 * 
 * 入口配置
 */
public class CommonConfig extends JFinalConfig
{
	Routes routes;
	Log log = Log.getLog(CommonConfig.class);

	public void loadProp(String pro, String dev)
	{
		try
		{
			PropKit.use(pro);
		} catch (Exception e)
		{
			PropKit.use(dev);
		}
	}

	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me)
	{

		me.setBaseUploadPath("/");
		/** 设置开发模式 */
		boolean devMode = Boolean.parseBoolean(ReadPropertity.getProperty("devMode"));
		me.setDevMode(devMode);

		/** 设置页面显示类型 */
		// me.setViewType(ViewType.JSP);

		// 设置beetl模板
		JFinal3BeetlRenderFactory rf = new JFinal3BeetlRenderFactory();
		rf.config();
		me.setRenderFactory(rf);

		// GroupTemplate gt = rf.groupTemplate;

		/** 微信接口开发 ApiConfigKit 设为开发模式可以在开发阶段输出请求交互的 xml 与 json 数据 */
		// ApiConfigKit.setDevMode(me.getDevMode());

		/** 设置字符集 */
		me.setEncoding(Consts.CHARTSET_UTF8);

		/** 视图error page设置 */
		me.setError404View("/common/404.html");
		me.setError500View("/common/500.html");

		/** 设置post大小 */
		me.setMaxPostSize(209715200);
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me)
	{
		this.routes = me;
		me.add(new AdminRoute());
		me.add(new FrontRoute());
	}

	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me)
	{
		/**
		 * shiro配置
		 */
		ShiroPlugin shiroPlugin = new ShiroPlugin(this.routes);
		shiroPlugin.setLoginUrl("/page/index.jsp");// 登陆url：未验证成功跳转
		shiroPlugin.setSuccessUrl("/page/index.jsp");// 登陆成功url：验证成功自动跳转
		shiroPlugin.setUnauthorizedUrl("/login/needPermission");// 授权url：未授权成功自动跳转
		me.add(shiroPlugin);

		Prop dbProp = PropKit.use("config.properties");

		/** 配置Druid数据库连接池插件 **/
		DruidPlugin dp = new DruidPlugin(dbProp.get("jdbcUrl"), dbProp.get("user"), dbProp.get("password"));
		dp.addFilter(new StatFilter());
		dp.setInitialSize(5);
		dp.setMaxActive(150);
		WallFilter wall = new WallFilter();
		wall.setDbType(ReadPropertity.getProperty("dbType"));
		dp.addFilter(wall);
		me.add(dp);

		/** 配置ActiveRecord插件,加载数据表对象 **/
		ActiveRecordPlugin arp = new ActiveRecordPlugin("dp1", dp);

		// arp.setTransactionLevel(4);//事务隔离级别
		arp.setDevMode(true); // 设置开发模式
		arp.setShowSql(true); // 是否显示SQL
		// arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));//
		// 大小写不敏感

		// arp.setDialect(new MysqlDialect());// 使用 mysql数据库方言

		/** 配置ActiveRecord插件,加载数据表对象 **/
		arp.addMapping("sys_user", "user_id", SysUser.class);
		arp.addMapping("sys_role", "role_id", SysRole.class);
		arp.addMapping("sys_keyid", "id", SysKeyId.class);
		arp.addMapping("sys_module", "module_id", SysModule.class);
		arp.addMapping("sys_fun", "fun_id", SysFun.class);
		arp.addMapping("sys_role_fun", "role_fun_id", SysRoleFun.class);
		arp.addMapping("sys_user_role", "user_role_id", SysUserRole.class);
		arp.addMapping("sys_site_set", "site_id", SysSite.class);
		arp.addMapping("web_config", "id", WebConfig.class);
		arp.addMapping("web_column", "id", WebColumn.class);
		arp.addMapping("web_article", "id", Article.class);
		arp.addMapping("web_mapping", "id", WebMaping.class);
		arp.addMapping("sys_logs", "id", Syslogs.class);
		arp.addMapping("rns_company", "company_id", WebCompany.class);
		arp.addMapping("rns_carousel", "carousel_id", WebImage.class);
		//arp.addMapping("sys_project", "id",SysProject.class);
		arp.addMapping("web_institution", "id",Institution.class);
		me.add(arp);

		// DruidPlugin dp2 = new DruidPlugin(dbProp.get("jdbcUrl2"),
		// dbProp.get("user2"), dbProp.get("password2"));
		// dp.addFilter(new StatFilter());
		// dp.setInitialSize(5);
		// dp.setMaxActive(150);
		// WallFilter wall2 = new WallFilter();
		// wall.setDbType(ReadPropertity.getProperty("dbType2"));
		// dp.addFilter(wall);
		// me.add(dp);

		/** 配置ActiveRecord插件,加载数据表对象 **/
		// ActiveRecordPlugin arp2 = new ActiveRecordPlugin("dp2", dp);
		//
		// // arp.setTransactionLevel(4);//事务隔离级别
		// arp2.setDevMode(true); // 设置开发模式
		// arp2.setShowSql(true); // 是否显示SQL
		/**
		 * 设置第二数据源 DruidPlugin dp2 = new
		 * DruidPlugin(ReadPropertity.getProperty("jdbcUrl2"),
		 * ReadPropertity.getProperty("user2"),
		 * ReadPropertity.getProperty("password2"
		 * ),ReadPropertity.getProperty("driver")); dp2.addFilter(new
		 * StatFilter()); dp2.setMaxActive(150); WallFilter wall2 = new
		 * WallFilter(); wall2.setDbType(ReadPropertity.getProperty("dbType"));
		 * dp2.addFilter(wall2); me.add(dp2);
		 * 
		 * ActiveRecordPlugin arp2 = new
		 * ActiveRecordPlugin("dp2",dp2);//配置多数据源的话，这里一定要加别名，默认只有一个
		 * me.add(arp2);
		 */

		/** 加载定时器 **/
		// QuartzPlugin quartzPlugin = new QuartzPlugin("job.properties");
		// me.add(quartzPlugin);

		/** 加载EhCache插件 **/
		// EhCachePlugin ecp = new EhCachePlugin();
		// me.add(ecp);
		// me.add(createEhCachePlugin());

	}

	/** 创建根目录下的ehcache文件 */
	public EhCachePlugin createEhCachePlugin()
	{
		String ehcacheDiskStorePath = PathKit.getWebRootPath();

		File pathFile = new File(ehcacheDiskStorePath, ".ehcache");

		Configuration cfg = ConfigurationFactory.parseConfiguration();
		cfg.addDiskStore(new DiskStoreConfiguration().path(pathFile.getAbsolutePath()));
		return new EhCachePlugin(cfg);
	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me)
	{
		/** 权限认证拦截器 */
		me.add(new AdminInterceptor());
		/**shiro拦截器，若不采用全局拦截器，在使用shiro注释时需要在类或方法前加此注释**/
		me.add(new ShiroInterceptor());
		/** 投保时前端用户登录拦截器 */
		// me.add(new LoginInterceptor());
		/** 全局异常拦截器 */
		// me.add(new ExceptionInterceptor());
		// me.add(new GlobelInterceptor());
		/** 配置开启事物规则 */
		// me.add(new TxByMethods("save", "update", "delete"));
		// me.add(new TxByMethodRegex("(.*save.*|.*update.*|.*delete.*)"));

	}

	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me)
	{
		me.add(new JHandler());
		log.info("configHandler 全局配置处理器，主要是记录日志和request域值处理");
		// me.add(new GlobalHandler());
	}

	public void afterJFinalStart()
	{
		// 1.5 之后支持redis存储access_token、js_ticket，需要先启动RedisPlugin
		// ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache());
		// 1.6新增的2种初始化
		// ApiConfigKit.setAccessTokenCache(new
		// RedisAccessTokenCache(Redis.use("weixin")));
		// ApiConfigKit.setAccessTokenCache(new
		// RedisAccessTokenCache("weixin"));
		// Prop dbProp = PropKit.use("config.properties");
		// ApiConfig ac = new ApiConfig();
		// // 配置微信 API 相关参数
		// ac.setToken(dbProp.get("token"));
		// ac.setAppId(dbProp.get("appId"));
		// ac.setAppSecret(dbProp.get("appSecret"));

		/**
		 * 是否对消息进行加密，对应于微信平台的消息加解密方式： 1：true进行加密且必须配置 encodingAesKey
		 * 2：false采用明文模式，同时也支持混合模式
		 */
		// ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		// ac.setEncodingAesKey(PropKit.get("encodingAesKey",
		// "setting it in config file"));
		//
		// /**
		// * 多个公众号时，重复调用ApiConfigKit.putApiConfig(ac)依次添加即可，第一个添加的是默认。
		// */
		// ApiConfigKit.putApiConfig(ac);// 第一个公众号

		// ApiConfig ac2 = new ApiConfig();
		// ac2.setToken(dbProp.get("token2"));
		// ac2.setAppId(dbProp.get("appId2"));
		// ac2.setAppSecret(dbProp.get("appSecret2"));
		// ac2.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		// ac2.setEncodingAesKey(PropKit.get("encodingAesKey",
		// "setting it in config file"));
		// ApiConfigKit.putApiConfig(ac2);//第二个公众号

		// 微信 WxSession的配置
		// 启用默认的Session管理器
		// ApiConfigKit.enableDefaultWxSessionManager();
		// 启用redis Session管理器
		// ApiConfigKit.setWxSessionManager(new
		// RedisWxSessionManager("weixin"));

		/**
		 * 1.9 新增LocalTestTokenCache用于本地和线上同时使用一套appId时避免本地将线上AccessToken冲掉
		 * 
		 * @see WeixinApiController#getToken()
		 */
		// boolean isLocal = true;
		// if (isLocal) {
		// String onLineTokenUrl = "http://wei.inssm.com/myinsure/api/getToken";
		// ApiConfigKit.setAccessTokenCache(new
		// LocalTestTokenCache(onLineTokenUrl));
		// }

	}

	@Override
	public void configEngine(Engine me)
	{
		// me.setDevMode(true);

	}
}
