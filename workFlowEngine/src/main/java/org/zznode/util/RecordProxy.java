package org.zznode.util;

import org.apache.commons.lang.StringUtils;
import org.zznode.common.LogCompar;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RecordProxy {
    /**
     * @param oldObj 原数据对象
     * @param newObj 修改后数据对象
     */
    public static Map<String,String> addRecord(Object oldObj, Object newObj){
        try {
            // 得到类对象
            Class<? extends Object> class1 = oldObj.getClass();
            Class<? extends Object> class2 = newObj.getClass();
            if(!class1.equals(class2)){
                throw new RuntimeException("请传入两个相同的实体类对象");
            }
            // 得到属性集合
            Field[] fields1 = class1.getDeclaredFields();
            Field[] fields2 = class2.getDeclaredFields();
            Map<String,String> resutlMap = new HashMap<>();
            StringBuffer key = new StringBuffer();
            StringBuffer value = new StringBuffer();
            Long id = null;
            for (Field field1 : fields1) {
                field1.setAccessible(true);    // 设置属性是可以访问的(私有的也可以)
                if(id == null && field1.getName().equals("id")){
                    id = (Long)field1.get(oldObj);
                }
                for (Field field2 : fields2) {
                    field2.setAccessible(true);    // 设置属性是可以访问的(私有的也可以)
                    if(field1.equals(field2)){    // 比较属性名是否一样
                        if(field2.get(newObj) == null || StringUtils.isEmpty(field2.get(newObj) + "")){
                            break;    // 属性名称一样就退出二级循环
                        }
                        if(!field1.get(oldObj).equals(field2.get(newObj))){    // 比较属性值是否一样
                            // 得到注解
                            LogCompar pn = field1.getAnnotation(LogCompar.class);
                            if(pn != null){
                                key.append(field1.getName()+",");
                                value.append(pn.value() + ":\"" +  field1.get(oldObj) + "\" 改成 \"" + field2.get(newObj) + "\",");
                            }
                        }
                        break;    // 属性名称一样就退出二级循环
                    }
                }
            }
            if(value.length() != 0){
                resutlMap.put("name",key.toString());
                resutlMap.put("value",value.toString());
                return resutlMap;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
