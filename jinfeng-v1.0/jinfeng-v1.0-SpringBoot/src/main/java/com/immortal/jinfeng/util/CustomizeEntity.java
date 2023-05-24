package com.immortal.jinfeng.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

//

/**
 * @Author 刘洌铒
 * @Date 2023/4/24
 * @Date 2023/3/19
 * @function 定制返回的实体，将实体的字段和值载入到 map 中，依赖于 map 对字段进行增删，
 *           用于对 spring MVC 返回的 JSON内容操作进行简化。方便去除不要的字段，和新增字段
 **/
@Component
public class CustomizeEntity<T> {

    public static int errorHint = 0;//开启错误提示,设置1为开启，默认0为关闭

    public final static int DEFFAULT = 0;//此参数表示驼峰命名法模式
    public final static int UPDERLINE_NOMENCLATURE = 1;//此参数表示下划线命名法模式

    public Map<String,Object> changeMap(T entity){
        Map<String, Object> result = new HashMap<>();
        return changeMapKeyUseNomenclature(entity, result, CustomizeEntity.DEFFAULT);
    }

    /**
     * @Author 刘洌铒
     * @Date 2023/4/24
     * @function 新装载一个实体（该实体中所有需装载的字段都必须实现 get 方法）
     * @param T entity 被装载的实体，所有字段作为 key，字段值作为 value 载入到名为 result 的 Map 中。并且转为下划线的命名法
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> changeMapKeyUseUnderline(T entity){
        Map<String, Object> result = new HashMap<>();
        return changeMapKeyUseNomenclature(entity, result, CustomizeEntity.UPDERLINE_NOMENCLATURE);
    }

    /**
     * @Author 刘洌铒
     * @Date 2023/4/24
     * @function 追加装载一个实体（该实体中所有需装载的字段都必须实现 get 方法）
     * @param T entity 被装载的实体，所有字段作为 key，字段值作为 value 载入到名为 result 的 Map 中。并且转为下划线的命名法
     * @Param Map result 被追加装载的 Map，将在原有的内容上 put 新装载实体的字段和值。
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @PS：特别注意！！！如果装载的多个实体中有出现字段相同的，则后面追加装载的字段值会覆盖前面载入的字段值。
     **/
    public Map<String, Object> changeMapKeyUseNomenclature(T entity, Map<String, Object> result,int rule) {

        Class entityClass = entity.getClass();

        //构建实体方法的map（我们只要get方法）
        Map<String, Method> methodMap = new HashMap<>();
        for (Method m : entityClass.getDeclaredMethods()) {
            String mName = m.getName();
            if (mName.startsWith("get")) {
                methodMap.put(mName, m);
            }
        }

        //将实体的所有字段和对应的值封装到结果result中。（这个过程要对小驼峰的字段转为下横线命名法）
        Field[] fields = entityClass.getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                String fName = fields[i].getName();
                String getFunctionName = "get" + (char)(fName.charAt(0) - 32) + fName.substring(1);

                String key = "";

                Method method = methodMap.get(getFunctionName);
                if(method == null){
                    //不存在get方法
                    if(CustomizeEntity.errorHint == 1){
                        System.out.println("不存在 " + fName + " 的get方法！");
                    }
                    continue;
                }
                if(rule == CustomizeEntity.UPDERLINE_NOMENCLATURE ){
                    //转化为下划线命名法
                    key = CustomizeEntity.humpTransformDownLine(fName);
                }else if(rule == CustomizeEntity.DEFFAULT){
                    //不进行转化
                    key = fName;
                }
                result.put(key, method.invoke(entity));
            }
        }catch (NullPointerException e) {
            System.out.println("未检测到某个get方法！请检查实体类中字段的get方法是否严格按照小驼峰命名，且与字段相对应！");
            throw e;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @Author 刘洌铒
     * @Date 2023/3/19
     * @function 驼峰命名法的字符串转为下横线命名法
     * @param String humpFieldName 驼峰命名法的字段名称
     * @return java.lang.String 将返回一个下横线命名法的字段名称
     **/
    private static String humpTransformDownLine(String humpFieldName){
        StringBuilder downLineFieldName = new StringBuilder();
        for (char item : humpFieldName.toCharArray()){
            if(item < 97){
                item += 32;
                downLineFieldName.append('_');
            }
            downLineFieldName.append(item);
        }
        return downLineFieldName.toString();
    }

}
