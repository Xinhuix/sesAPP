package com.jfinalshop.api.interceptor;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinalshop.api.common.bean.BaseResponse;
import com.jfinalshop.api.common.bean.Code;
import com.jfinalshop.api.common.token.TokenManager;
import com.jfinalshop.model.Member;

public class TokenInterceptor implements Interceptor {
    public void intercept(Invocation i) {
        Controller controller = i.getController();
        // 文件上传API需要先getFiles
        if ("/api/fs/upload".equalsIgnoreCase(i.getActionKey()))
        	controller.getFiles();
        
        String token = controller.getPara("token");
        if (StringUtils.isEmpty(token)) {
            controller.renderJson(new BaseResponse(Code.ARGUMENT_ERROR, "Token不能为空！"));
            return;
        }

        Member member = TokenManager.getMe().validate(token);
        if (member == null) {
            controller.renderJson(new BaseResponse(Code.TOKEN_INVALID, "Token失效"));
            return;
        }        
        controller.setAttr("member", member);
        i.invoke();
    }
}
