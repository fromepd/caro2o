package com.ruoyi.common.core.service;

/**
 * @author Dengyangbo
 * @date 2022/8/31 09:16
 * @email 13005100647@163.com
 * @description SmsService
 */
public interface SmsService {

    /**
     * 发送验证码
     *
     * @param phone   手机号码
     * @param useType 用途
     * @param timeout 过期时间 秒
     * @return
     */
    void sendCaptcha(String phone, String useType, int timeout);

    /**
     * 检查验证码
     *
     * @param phone      手机号码
     * @param useType    用途
     * @param verifyCode 验证码
     * @return
     */
    boolean checkCaptcha(String phone, String useType, String verifyCode);
}
