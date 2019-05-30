package com.cy.store.exception;


public class BaseException extends RuntimeException {

    private String retMsg;
    private int retCode;
    private ErrorCodes code;

    protected BaseException(ErrorCodes code) {
        this(code,code.getRetMsg());
    }

    protected BaseException(ErrorCodes code, String retMsg) {
        super(retMsg);
        this.retCode = code.getRetCode();
        this.retMsg = retMsg;
        this.code = code;
    }

    public ErrorCodes getErrorCode() {
        return code;
    }

    public void setErrorCode(ErrorCodes code) {
        this.retCode = code.getRetCode();
        this.retMsg = code.getRetMsg();
        this.code = code;
    }

    public int getCode(){
        return retCode;
    }
    public String getMsg() {
        return retMsg;
    }

    public void setMsg(String retMsg) {
        this.retMsg = retMsg;
    }

}
