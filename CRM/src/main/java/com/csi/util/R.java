package com.csi.util;

import java.util.HashMap;

public class R extends HashMap<String, Object> {

    public R(){
        put("code",200);
        put("message","success");
    }

    public static R ok(Object data){
        R r = new R();
        r.put("data",data);
        return r;
    }

    public static R loginSuccess(Object data){
        R r = new R();
        r.put("message","登录成功");
        r.put("data",data);
        return r;
    }

    public static R loginFail(){
        R r = new R();
        r.put("code",500);
        r.put("message","登陆失败");
        r.put("data",null);
        return r;
    }

    public static R error(){
        R r = new R();
        r.put("code",500);
        r.put("message","fail");
        r.put("data",null);
        return r;
    }

    // 只返回消息不返回数据
    public static R message(String message) {
        R r = new R();
        r.put("code",500);
        r.put("message", message);
        return r;
    }

    public static R okMessage(String message) {
        R r = new R();
        r.put("code",200);
        r.put("message", message);
        return r;
    }
}
