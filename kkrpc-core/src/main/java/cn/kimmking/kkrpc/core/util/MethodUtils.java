package cn.kimmking.kkrpc.core.util;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/10 21:49
 */
public class MethodUtils {

    public static boolean checkLocalMethod(final String methodName) {
        //本地方法不代理
        if ("toString".equals(methodName) ||
                "hashCode".equals(methodName) ||
                "notifyAll".equals(methodName) ||
                "equals".equals(methodName) ||
                "wait".equals(methodName) ||
                "getClass".equals(methodName) ||
                "notify".equals(methodName)) {
            return true;
        }
        return false;
    }


    // 等价上面的
    public static boolean checkLocalMethod(final Method method) {
        return method.getDeclaringClass().equals(Object.class);
    }

    public static String methodSign(Method method) {
        if (method != null) {
            StringBuilder builder = new StringBuilder();//("method:");
            String name = method.getName();
            builder.append(name);
            builder.append("@");
            int count = method.getParameterCount();
            builder.append(count);
            builder.append("_");
            if (count > 0) {
                Class<?>[] classes = method.getParameterTypes();
                Arrays.stream(classes).forEach(c -> builder.append(c.getName() + ","));
            }
            String sign = builder.toString();
            System.out.println(sign);
            return sign;
//            String string = builder.toString();
//            return DigestUtils.md5DigestAsHex(string.getBytes());
        }
        return "";
    }


}
