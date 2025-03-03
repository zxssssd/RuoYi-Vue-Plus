package com.ruoyi.report.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.ruoyi.report.domain.R;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * report 权限拦截
 *
 * @author gssong
 */
@Component
public class JimuReportTokenService implements JmReportTokenServiceI {

    @Value("${checkToken.url}")
    private String url;

    @Override
    public String getUsername(String token) {
        String result = HttpUtil.get(url + token);
        return StringUtils.substringAfterLast((String) JSON.parseObject(result, R.class).getData(), "sys_user:");
    }

    @Override
    public Boolean verifyToken(String token) {
        String result;
        try {
            result = HttpUtil.get(url + token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (result == null || result.contains(HttpStatus.HTTP_INTERNAL_ERROR + StrUtil.EMPTY)) {
            return false;
        }
        return !result.contains("Cannot GET");
    }

    @Override
    public String[] getRoles(String s) {
        return Collections.singletonList("admin").toArray(new String[0]);
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return JmReportTokenServiceI.super.getToken(request);
    }

    @Override
    public String getToken() {
        return JmReportTokenServiceI.super.getToken();
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        return JmReportTokenServiceI.super.getUserInfo(token);
    }

    @Override
    public HttpHeaders customApiHeader() {
        return JmReportTokenServiceI.super.customApiHeader();
    }

    @Override
    public String getTenantId() {
        return JmReportTokenServiceI.super.getTenantId();
    }
}
