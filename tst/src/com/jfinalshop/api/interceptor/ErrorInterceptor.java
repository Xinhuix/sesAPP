package com.jfinalshop.api.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinalshop.api.common.bean.BaseResponse;
import com.jfinalshop.api.common.bean.Code;

/**
 * 项目名称：ejianf-api    
 * 类名称：ErrorInterceptor    
 * 类描述：捕获所有api action异常    
 * 创建人：李红元    
 * 创建时间：2015-7-8 上午10:52:15    
 * 修改人：
 * 修改时间：
 * 修改备注：    
 * 版本信息：1.0  
 * Copyright © 2015-2018     
 * 版权所有： 深圳市前海分联科技有限公司  
 *
 */
public class ErrorInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(ErrorInterceptor.class);
    public void intercept(Invocation i) {
        try {
            i.invoke();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            i.getController().renderJson(new BaseResponse(Code.ERROR, "server error"));
        }
    }
}
