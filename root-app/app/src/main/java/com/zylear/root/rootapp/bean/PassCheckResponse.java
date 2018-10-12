package com.zylear.root.rootapp.bean;

/**
 * Created by xiezongyu on 2018/10/12.
 */
public class PassCheckResponse extends BaseResponse {

    public PassCheckResponse() {
    }

    public PassCheckResponse(String content) {
        this.content = content;
    }

    public PassCheckResponse(Integer errorCode, String errorMessage, String content) {
        super(errorCode, errorMessage);
        this.content = content;
    }

    public PassCheckResponse(BaseResponse baseResponse, String content) {
        super(baseResponse);
        this.content = content;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
