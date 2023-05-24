package com.immortal.jinfeng.util;


import java.util.HashMap;
import java.util.Map;

public class R extends HashMap{

    private R(){}

    public static R ok(){
        R r = new R();
        r.put("flag",true);
        r.put("code",2000);
        return r;
    }

    public static R code(Integer code){
        R r = new R();
        r.put("flag",false);
        r.put("code",code);
        return r;
    }


    public R message(String message){
        this.put("message",message);
        return this;
    }

    //在最外层追加多个键值对,如果只需要追加一个，请使用put方法以提高性能
    public R additional(Map<String, Object> additionalContent){
        for(String item : additionalContent.keySet()){
            this.put(item,additionalContent.get(item));
        }
        return this;
    }

    public R put(Object key, Object value){
        super.put(key,value);
        return this;
    }

}
