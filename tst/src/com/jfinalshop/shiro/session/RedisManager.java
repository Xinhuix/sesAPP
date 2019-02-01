package com.jfinalshop.shiro.session;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinalshop.CommonAttributes;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {

	private String		url			= null;

	private int			port		= 6379;

	private int			timeout		= 0;

	private String		password	= null;

	private JedisPool	jedisPool	= null;

	public RedisManager() {
		Prop prop = PropKit.use(CommonAttributes.JFINALSHOP_PROPERTIES_PATH);
		url = prop.get("redis.url");
		password = prop.get("redis.password");
		if (StrKit.isBlank(url)) {
			url = StringUtils.defaultIfBlank(url, "127.0.0.1");
		}
		if (StringUtils.isNotBlank(password)) {
			jedisPool = new JedisPool(new JedisPoolConfig(), url, port, timeout, password);
		} else if (timeout != 0) {
			jedisPool = new JedisPool(new JedisPoolConfig(), url, port, timeout);
		} else {
			jedisPool = new JedisPool(new JedisPoolConfig(), url, port);
		}
	}

//	public void init() {
//		url = StringUtils.defaultIfBlank(url, "127.0.0.1");
//		if (StringUtils.isNotBlank(password)) {
//			jedisPool = new JedisPool(new JedisPoolConfig(), url, port, timeout, password);
//		} else if (timeout != 0) {
//			jedisPool = new JedisPool(new JedisPoolConfig(), url, port, timeout);
//		} else {
//			jedisPool = new JedisPool(new JedisPoolConfig(), url, port);
//		}
//	}

	public Jedis getJedis() {
		return jedisPool.getResource();
	}

	public String get(String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			return jedis.get(key);
		}
	}

	public void set(String key, String value) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.set(key, value);
		}
	}

	public void set(String key, String value, int timeToLiveSeconds) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.setex(key, timeToLiveSeconds, value);
		}
	}

	public void del(String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.del(key);
		}
	}

	public Set<String> keys(String pattern) {
		try (Jedis jedis = jedisPool.getResource();) {
			return jedis.keys(pattern);
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}