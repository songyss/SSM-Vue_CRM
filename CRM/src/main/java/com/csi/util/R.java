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

    public static R error(){
        R r = new R();
        r.put("code",500);
        r.put("message","fail");
        r.put("data",null);
        return r;
    }
}
