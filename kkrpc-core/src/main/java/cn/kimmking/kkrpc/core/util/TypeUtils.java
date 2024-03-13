package cn.kimmking.kkrpc.core.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/12 18:52
 */
public class TypeUtils {

    public static Object cast(Object origin, Class<?> type) {
        Class<?> aClass = origin.getClass();
        if (origin == null || type.isAssignableFrom(aClass)) {
            return origin;
        }
        if (origin instanceof HashMap map) {
            JSONObject jsonObject = new JSONObject(map);
            return jsonObject.toJavaObject(type);
        }
        if (type.equals(String.class)) {
            return origin.toString();
        }
//        if(type.isPrimitive()) {
        if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
            return Integer.valueOf(origin.toString());
        } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
            return Long.valueOf(origin.toString());
        } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
            return Long.valueOf(origin.toString());
        } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
            return Long.valueOf(origin.toString());
        } else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
            return Byte.valueOf(origin.toString());
        } else if (type.equals(Short.class) || type.equals(Short.TYPE)) {
            return Short.valueOf(origin.toString());
        } else if (type.equals(Character.class) || type.equals(Character.TYPE)) {
            return Character.valueOf(origin.toString().charAt(0));
        }
//        }
        return null;
    }

}
