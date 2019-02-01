package com.jfinalshop.service;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.dao.PointLogDao;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.PointLog;

/**
 * Service - 积分记录
 * 
 * 
 */
public class PointLogService extends BaseService<PointLog> {

	/**
	 * 构造方法
	 */
	public PointLogService() {
		super(PointLog.class);
	}
	
	private PointLogDao pointLogDao = Enhancer.enhance(PointLogDao.class);
	
	/**
	 * 查找积分记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	public Page<PointLog> findPage(Member member, Pageable pageable) {
		return pointLogDao.findPage(member, pageable);
	}

}