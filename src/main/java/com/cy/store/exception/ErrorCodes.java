package com.cy.store.exception;

import java.util.Objects;

public enum ErrorCodes {
    //1xxxx:参数相关错误
    //2xxxx:
    //7xxxx:文件相关错误
    //8xxxx:登录、权限相关错误
    //9xxxx:统一错误码

    PARAM_NOT_EMPTY(10001,"参数不能为空"),
    ID_NOT_LEGAL(10002, "ID不合法"),
    CODE_REPEATED(10003, "编码重复"),

    ITEM_NOT_EXIST(20001, "记录不存在"),
    DATA_OP_FAILED(20002, "数据操作失败"),

    FILE_NOT_EXSIT(70001, "文件不存在"),
    FILE_UPLOAD_FAILED(70002, "文件上传失败"),
    FILE_WRITE_FAILED(70003, "文件写失败"),
    PATH_IS_WRONG(70004, "路径正确"),

    VERCODE_NOT_EMPTY(80001, "验证码不能为空"),
    VERCODE_IS_WRONG(80002, "验证码错误"),
    PASSWORD_NOT_EMPTY(80003, "密码不能为空"),
    PASSWORD_IS_WRONG(80004, "密码错误"),
    PASSWORD_NOT_SAME(80005, "两次密码不一致"),
    UN_LOGIN(80006, "用户未登录"),

    SERVER_IS_WRONG(90001, "服务端错误"),
    HTTP_IS_WRONG(90002, "http服务错误");

    private Integer code;
    private String errorInfo;

    ErrorCodes(Integer code, String errorInfo){
        this.code = code ;
        this.errorInfo = errorInfo;
    }

    public int getErrorCode() {
        return code;
    }

    public String getInfo() {
        return toString();
    }

    @Override
    public String toString() {
        return errorInfo;
    }

    public static ErrorCodes fromErrorCode(Integer code){
        for (ErrorCodes error : ErrorCodes.values()) {
            if (Objects.equals(code,error.getErrorCode())) {
                return error;
            }
        }
        return null;
    }

}
