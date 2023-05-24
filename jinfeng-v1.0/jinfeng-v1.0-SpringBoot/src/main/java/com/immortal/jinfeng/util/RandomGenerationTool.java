package com.immortal.jinfeng.util;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class RandomGenerationTool {
    //验证码的字符库,可通过set方法重新设置
    private static final String verifyCodeCharLibrary = "0123456789" +
            "ABCDEFGHJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz";
    //随机数工具
    private static Random random = new Random();

    /**
     * @Author 刘洌铒
     * @Date 2023/4/23
     * @function 获取一个长度为4的随机验证码
     * @return java.lang.String
    **/
    public static String getVerifyCode(){
        return RandomGenerationTool.getVerifyCode(4);
    }

    /**
     * @Author 刘洌铒
     * @Date 2023/4/23
     * @function 获取一个指定长度的随机验证码
     * @param int verifyCodeLength 指定返回的验证码长度
     * @return java.lang.String
    **/
    public static String getVerifyCode(int verifyCodeLength){
        String code = "";
        for (int i = 0; i < verifyCodeLength; i++) {
            char c = verifyCodeCharLibrary.charAt(random.nextInt(verifyCodeCharLibrary.length()));
            code = code + c;
        }
        return code;
    }

    /**
     * @Author 作者名
     * @Date 2023/4/28
     * @function 生成没有横杠的UUID
     * @param
     * @return java.lang.String
     **/
    public static String getNoWhipptreeUUID(){
        String uuid = UUID.randomUUID().toString(); // 生成UUID，并转化为String
        uuid = uuid.replaceAll("-", ""); // 去掉UUID的-符号
        return uuid;
    }

}
