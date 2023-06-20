package com.ruoyi.common.core.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Dengyangbo
 * @date 2022/8/31 09:21
 * @email 13005100647@163.com
 * @description LocationSmsServiceImpl
 */
@Component("locationSmsService")
public class LocationSmsServiceImpl implements SmsService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public void sendCaptcha(String phone, String useType, int timeout) {
        Assert.hasLength(phone, "请输入手机号!");
        Assert.hasLength(useType, "请输入用途!");

        String key = "SMS_CODE:".concat(useType).concat(phone);
        redisCache.setCacheObject(key, "666666", timeout, TimeUnit.SECONDS);
    }

    @Override
    public boolean checkCaptcha(String phone, String useType, String verifyCode) {
        Assert.hasLength(phone, "请输入手机号!");
        Assert.hasLength(useType, "请输入用途!");
        Assert.hasLength(useType, "请输入验证码!");
        String key = "SMS_CODE:".concat(useType).concat(phone);
        String cacheCode = redisCache.getCacheObject(key);

        if (StringUtils.hasLength(cacheCode)) {
            return cacheCode.equals(verifyCode);
        }
        return false;
    }
}
