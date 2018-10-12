package com.zylear.root.rootapp.bean;

/**
 * Created by xiezongyu on 2018/4/9.
 */
public class BaseResponse {

    private Integer errorCode;
    private String errorMessage;


    public static final BaseResponse SUCCESS_RESPONSE = new BaseResponse(0, "success");
    public static final BaseResponse ERROR_RESPONSE = new BaseResponse(1, "error");



//    public static final BaseResponse UPLOAD_FAIL_RESPONSE = new BaseResponse(2, "upload fail");
//    public static final BaseResponse FILE_EXIST_RESPONSE = new BaseResponse(3, "file exist");
//    public static final BaseResponse ID_EXIST_RESPONSE = new BaseResponse(4, "id exist");
//
//    public static final BaseResponse OVERSPEND_RESPONSE = new BaseResponse(5, "overspend");
//
//    public static final BaseResponse PROJECT_NO_EXIST_RESPONSE = new BaseResponse(6, "project no exist");
//    public static final BaseResponse BIDDING_NO_EXIST_RESPONSE = new BaseResponse(7, "bidding no exist");
//    public static final BaseResponse BID_NO_EXIST_RESPONSE = new BaseResponse(8, "bid no exist");
//    public static final BaseResponse CONTRACT_NO_EXIST_RESPONSE = new BaseResponse(9, "contract no exist");
//    public static final BaseResponse BUDGET_NO_EXIST_RESPONSE = new BaseResponse(10, "budget no exist");
//
//    public static final BaseResponse CAPTCHA_ERROR = new BaseResponse(11, "captcha error");

    public static final Integer DEVICE_EXIST = 1;
    public static final Integer ACCOUNT_EXIST = 2;
    public static final Integer UNAUTHORIZED = 2;


    public BaseResponse() {
    }

    public BaseResponse(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BaseResponse(BaseResponse baseResponse) {
        this.errorCode = baseResponse.errorCode;
        this.errorMessage = baseResponse.errorMessage;
    }

    public static boolean isSuccess(BaseResponse baseResponse) {
        if (baseResponse != null && baseResponse.getErrorCode() == 0) {
            return true;
        }
        return false;
    }


    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
