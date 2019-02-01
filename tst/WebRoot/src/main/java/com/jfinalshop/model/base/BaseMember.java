package com.jfinalshop.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseMember<M extends BaseMember<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
	}

	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public void setModifyDate(java.util.Date modifyDate) {
		set("modify_date", modifyDate);
	}

	public java.util.Date getModifyDate() {
		return get("modify_date");
	}

	public void setVersion(java.lang.Long version) {
		set("version", version);
	}

	public java.lang.Long getVersion() {
		return get("version");
	}

	public void setAddress(java.lang.String address) {
		set("address", address);
	}

	public java.lang.String getAddress() {
		return get("address");
	}

	public void setAmount(java.math.BigDecimal amount) {
		set("amount", amount);
	}

	public java.math.BigDecimal getAmount() {
		return get("amount");
	}

	public void setAttributeValue0(java.lang.String attributeValue0) {
		set("attribute_value0", attributeValue0);
	}

	public java.lang.String getAttributeValue0() {
		return get("attribute_value0");
	}

	public void setAttributeValue1(java.lang.String attributeValue1) {
		set("attribute_value1", attributeValue1);
	}

	public java.lang.String getAttributeValue1() {
		return get("attribute_value1");
	}

	public void setAttributeValue2(java.lang.String attributeValue2) {
		set("attribute_value2", attributeValue2);
	}

	public java.lang.String getAttributeValue2() {
		return get("attribute_value2");
	}

	public void setAttributeValue3(java.lang.String attributeValue3) {
		set("attribute_value3", attributeValue3);
	}

	public java.lang.String getAttributeValue3() {
		return get("attribute_value3");
	}

	public void setAttributeValue4(java.lang.String attributeValue4) {
		set("attribute_value4", attributeValue4);
	}

	public java.lang.String getAttributeValue4() {
		return get("attribute_value4");
	}

	public void setAttributeValue5(java.lang.String attributeValue5) {
		set("attribute_value5", attributeValue5);
	}

	public java.lang.String getAttributeValue5() {
		return get("attribute_value5");
	}

	public void setAttributeValue6(java.lang.String attributeValue6) {
		set("attribute_value6", attributeValue6);
	}

	public java.lang.String getAttributeValue6() {
		return get("attribute_value6");
	}

	public void setAttributeValue7(java.lang.String attributeValue7) {
		set("attribute_value7", attributeValue7);
	}

	public java.lang.String getAttributeValue7() {
		return get("attribute_value7");
	}

	public void setAttributeValue8(java.lang.String attributeValue8) {
		set("attribute_value8", attributeValue8);
	}

	public java.lang.String getAttributeValue8() {
		return get("attribute_value8");
	}

	public void setAttributeValue9(java.lang.String attributeValue9) {
		set("attribute_value9", attributeValue9);
	}

	public java.lang.String getAttributeValue9() {
		return get("attribute_value9");
	}

	public void setBalance(java.math.BigDecimal balance) {
		set("balance", balance);
	}

	public java.math.BigDecimal getBalance() {
		return get("balance");
	}

	public void setBirth(java.util.Date birth) {
		set("birth", birth);
	}

	public java.util.Date getBirth() {
		return get("birth");
	}

	public void setEmail(java.lang.String email) {
		set("email", email);
	}

	public java.lang.String getEmail() {
		return get("email");
	}

	public void setGender(java.lang.Integer gender) {
		set("gender", gender);
	}

	public java.lang.Integer getGender() {
		return get("gender");
	}

	public void setIsEnabled(java.lang.Boolean isEnabled) {
		set("is_enabled", isEnabled);
	}

	public java.lang.Boolean getIsEnabled() {
		return get("is_enabled");
	}

	public void setIsLocked(java.lang.Boolean isLocked) {
		set("is_locked", isLocked);
	}

	public java.lang.Boolean getIsLocked() {
		return get("is_locked");
	}

	public void setLockKey(java.lang.String lockKey) {
		set("lock_key", lockKey);
	}

	public java.lang.String getLockKey() {
		return get("lock_key");
	}

	public void setLockedDate(java.util.Date lockedDate) {
		set("locked_date", lockedDate);
	}

	public java.util.Date getLockedDate() {
		return get("locked_date");
	}

	public void setLoginDate(java.util.Date loginDate) {
		set("login_date", loginDate);
	}

	public java.util.Date getLoginDate() {
		return get("login_date");
	}

	public void setLoginFailureCount(java.lang.Integer loginFailureCount) {
		set("login_failure_count", loginFailureCount);
	}

	public java.lang.Integer getLoginFailureCount() {
		return get("login_failure_count");
	}

	public void setLoginIp(java.lang.String loginIp) {
		set("login_ip", loginIp);
	}

	public java.lang.String getLoginIp() {
		return get("login_ip");
	}

	public void setLoginPluginId(java.lang.String loginPluginId) {
		set("login_plugin_id", loginPluginId);
	}

	public java.lang.String getLoginPluginId() {
		return get("login_plugin_id");
	}

	public void setMobile(java.lang.String mobile) {
		set("mobile", mobile);
	}

	public java.lang.String getMobile() {
		return get("mobile");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setNickname(java.lang.String nickname) {
		set("nickname", nickname);
	}

	public java.lang.String getNickname() {
		return get("nickname");
	}

	public void setOpenId(java.lang.String openId) {
		set("open_id", openId);
	}

	public java.lang.String getOpenId() {
		return get("open_id");
	}

	public void setPassword(java.lang.String password) {
		set("password", password);
	}

	public java.lang.String getPassword() {
		return get("password");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}

	public java.lang.String getPhone() {
		return get("phone");
	}

	public void setPoint(java.lang.Long point) {
		set("point", point);
	}

	public java.lang.Long getPoint() {
		return get("point");
	}

	public void setRegisterIp(java.lang.String registerIp) {
		set("register_ip", registerIp);
	}

	public java.lang.String getRegisterIp() {
		return get("register_ip");
	}

	public void setSafeKeyExpire(java.util.Date safeKeyExpire) {
		set("safe_key_expire", safeKeyExpire);
	}

	public java.util.Date getSafeKeyExpire() {
		return get("safe_key_expire");
	}

	public void setSafeKeyValue(java.lang.String safeKeyValue) {
		set("safe_key_value", safeKeyValue);
	}

	public java.lang.String getSafeKeyValue() {
		return get("safe_key_value");
	}

	public void setAvatar(java.lang.String avatar) {
		set("avatar", avatar);
	}

	public java.lang.String getAvatar() {
		return get("avatar");
	}

	public void setUsername(java.lang.String username) {
		set("username", username);
	}

	public java.lang.String getUsername() {
		return get("username");
	}

	public void setZipCode(java.lang.String zipCode) {
		set("zip_code", zipCode);
	}

	public java.lang.String getZipCode() {
		return get("zip_code");
	}

	public void setAreaId(java.lang.Long areaId) {
		set("area_id", areaId);
	}

	public java.lang.Long getAreaId() {
		return get("area_id");
	}

	public void setMemberRankId(java.lang.Long memberRankId) {
		set("member_rank_id", memberRankId);
	}

	public java.lang.Long getMemberRankId() {
		return get("member_rank_id");
	}

}
