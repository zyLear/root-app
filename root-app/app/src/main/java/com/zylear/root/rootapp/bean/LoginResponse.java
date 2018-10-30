package com.zylear.root.rootapp.bean;

/**
 * Created by xiezongyu on 2018/10/13.
 */
public class LoginResponse extends BaseResponse {

    public LoginResponse() {
    }

    public LoginResponse(String accountInfo, String helper) {
        this.accountInfo = accountInfo;
        this.helper = helper;
    }

    public LoginResponse(Integer errorCode, String errorMessage, String accountInfo, String helper) {
        super(errorCode, errorMessage);
        this.accountInfo = accountInfo;
        this.helper = helper;
    }

    public LoginResponse(BaseResponse baseResponse, String accountInfo, String helper) {
        super(baseResponse);
        this.accountInfo = accountInfo;
        this.helper = helper;
    }

    private String accountInfo;
    private String helper;

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }
}
