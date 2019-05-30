package com.cy.store.exception;

import com.alibaba.fastjson.JSONObject;

public class JsonException extends BaseException {

    private JsonException(ErrorCodes code) {
        super(code);
    }

    public static JsonException newInstance(ErrorCodes code){
        return new JsonException(code);
    }

    public JSONObject toJson(){
        JSONObject res = new JSONObject();
        res.put("retCode", this.getCode());
        res.put("retMsg", this.getMsg());
        return res;
    }

}
