/*
 * @(#) VerifyResult.java 2019-04-22
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.netease.is.nc.sdk.entity;

/**
 * @author hzzhuxiafeng
 * @version 2018-10-17
 */

public class VerifyResult {

    public VerifyResult() {
    }

    /**
     * 异常代号
     */
    private int error;
    /**
     * 错误描述信息
     */
    private String msg;
    /**
     * 二次校验结果 true:校验通过 false:校验失败
     */
    private boolean result;

    /**
     * 短信上行发送的手机号码
     * 仅限于短信上行的验证码类型
     */
    private String phone;

    /**
     * 额外字段
     */
    private String extraData;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public static VerifyResult fakeNormalResult(String resp) {
        VerifyResult result = new VerifyResult();
        result.setResult(false);
        result.setError(0);
        result.setMsg(resp);
        result.setPhone("");
        return result;
    }
}
