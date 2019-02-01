package com.jfinalshop.service.ses;

import java.util.List;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.dao.SCardRecommendDao;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.SRecommend;
import com.jfinalshop.service.BaseService;

/**
 * Service - 会员推荐记录
 * 
 * 
 */
public class RecommendService extends BaseService<SRecommend> {
	
	/**
	 * 构造方法
	 */
	public RecommendService() {
		super(SRecommend.class);
	}
	
	
	private SCardRecommendDao recommendDao = Enhancer.enhance(SCardRecommendDao.class);;
	
	public SRecommend save(SRecommend ad) {
		return super.save(ad);
	}

	public SRecommend update(SRecommend ad) {
		return super.update(ad);
	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(SRecommend ad) {
		super.delete(ad);
	}
	
	
	/**
	 * 根据注册用户ID查找推荐人
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 推荐人，若不存在则返回null
	 */
	public SRecommend findByTomemberid(Long to_member_id) {
		return recommendDao.findByTomemberid(to_member_id);
	}
	
	/**
	 * 根据注册用户ID查找“第二”推荐人
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 推荐人，若不存在则返回null
	 */
	public SRecommend findByTomemberidTwo(Long to_member_id) {
		return recommendDao.findByTomemberidTwo(to_member_id);
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
	public Page<SRecommend> findPage(String isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
		return recommendDao.findPage(isEnabled, isExchange, hasExpired, pageable);
	}

	
	
/**
 * 根据推荐人手机号 ，查询推荐记录
 * @param mobile
 * @param pageable
 * @return
 */
	public Page<SRecommend> findPagememberId(String mobile, Pageable pageable) {
		return recommendDao.findPagememberId(mobile, pageable);
	}

	
/**
 * 查找会员推荐记录分页
 * @param memberId
 * @return
 */
	public List<SRecommend> findListSRecommend(java.lang.Long memberId) {
		return recommendDao.findListSRecommend(memberId);
	}
	
	
	
}