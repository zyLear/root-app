package com.zylear.root.rootapp.bean;

public class PassCheckRequest {

    public PassCheckRequest(String account, String password, String deviceId, String codeKey) {
        this.account = account;
        this.password = password;
        this.deviceId = deviceId;
        this.codeKey = codeKey;
    }

    private String account;
    private String password;
    private String codeKey;
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }
}
