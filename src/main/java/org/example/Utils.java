package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {
   public static <T> T cache(T obj){
       Class cls = obj.getClass();
       return (T) Proxy.newProxyInstance(
               cls.getClassLoader()
               ,cls.getInterfaces()
               , new ObjInvHandler(obj)
       );
   }
}
