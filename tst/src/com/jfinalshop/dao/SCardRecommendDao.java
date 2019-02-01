package com.jfinalshop.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.model.Area;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.SCard;
import com.jfinalshop.model.SRecommend;

/**
 * Dao - 会员推荐记录
 * 
 * 
 */
public class SCardRecommendDao extends BaseDao<SRecommend> {

	/**
	 * 构造方法
	 */
	public SCardRecommendDao() {
		super(SRecommend.class);
	}
	
	
	/**
	 * 根据注册用户ID查找推荐人
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 推荐人，若不存在则返回null
	 */
	public SRecommend findByTomemberid(Long to_member_id) {
		if (to_member_id==null) {
			return null;
		}
		try {
			String sql = "SELECT * FROM s_recommend WHERE  status='0'  and   to_member_id =?";
			return modelManager.findFirst(sql, to_member_id);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 根据注册用户ID查找“第二”推荐人
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 推荐人，若不存在则返回null
	 */
	public SRecommend findByTomemberidTwo(Long to_member_id) {
		if (to_member_id==null) {
			return null;
		}
		try {
			String sql = "SELECT * FROM s_recommend WHERE to_member_id =?";
			return modelManager.findFirst(sql, to_member_id);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	

	/**
	 * 查找会员推荐记录分页
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param isExchange
	 *            是否允许积分兑换
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 优惠券分页
	 */
	public Page<SRecommend> findPage(String isEnabled, Boolean isExchange,
			Boolean hasExpired, Pageable pageable) {
		String sqlExceptSelect = "FROM s_recommend WHERE 1 = 1 ";
		if (isEnabled != null) {
			sqlExceptSelect += " AND status = '" + isEnabled+"'";
		}
		
		return super.findPage(sqlExceptSelect, pageable);
	}
	
	
/***
 * 根据推荐人手机号 ，查询推荐记录
 * @param mobile
 * @param pageable
 * @return
 */
	public Page<SRecommend> findPagememberId(String mobile, Pageable pageable) {
		String sqlExceptSelect = "FROM s_recommend WHERE 1=1";
		if(mobile!=null&&!mobile.equals("")){
			sqlExceptSelect = "FROM s_recommend_username WHERE username like '%"+mobile+"%'";
		}
		
		return super.findPage(sqlExceptSelect, pageable);
	}
	
	/**
	 * 查找推荐人的推荐记录
	 * 
	 * @param count
	 *            数量
	 * @return 顶级地区
	 */
	public List<SRecommend> findListSRecommend(java.lang.Long memberId) {
		String sql = "SELECT * FROM s_recommend WHERE  member_id ="+memberId;
		/*if (count != null) {
			sql += "LIMIT 0, " + count;
		}*/
		return modelManager.find(sql);
	}
	
	//我推荐的
/*	public List<SRecommend> MeRecommend(Long id){
		if(id==null) {
			throw new Exception("id不能为空");
		}
		
		return
	} */
	
}