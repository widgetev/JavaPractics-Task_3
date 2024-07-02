package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ObjInvHandler<T> implements InvocationHandler {
    private final T curObj;
    private final Map<State, Map<Method, Result>> states = new ConcurrentHashMap<>();

    //cacheObjMap
    private Map<Method, Result> curStateResults = new HashMap<>();
    private  State curState = new State();
    private static CacheCleaner cleaner = CacheCleaner.getInstance();

    public ObjInvHandler(T obj) {
        this.curObj = obj;
        states.put(curState,curStateResults);
        cleaner.list.add(states); //каждый кэш добавить в список кешей для отчистки
        //System.out.println(cleaner.list);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //method из аргументов - это метод интерфеса. Надо найти метод которые его реализует в классе(обекте)
        Method curMeth = curObj.getClass().getMethod(method.getName(), method.getParameterTypes());

        //Если это кэшируемый метод
        if(curMeth.isAnnotationPresent(Cache.class)){
            long time = curMeth.getAnnotation(Cache.class).value(); //срок годности указанный в аннотации конкретного метода
            Result res = curStateResults.get(curMeth); //результат в текущем состоянии (если есть)


            if (res!=null) { //Это если результат для текущего состояния уже вычислялся. Счиать не надо, но срок жизни надо сбросить
                res.expireTime = (time == 0)?0L:System.currentTimeMillis() + time; //Установить новый срок действия для тек.результата
                curStateResults.put(curMeth,res); //обновим обновленный текущий результат в кеше
                //System.out.println(" from cache = " + res.value);
            } else {
                //Если не попвл в пред. IF значит резултата еще = нет новый кэщ
                CacheCleaner.setSleepTime(time); //периодичность запуска чистки как минимальное время кэша/2
                res = new Result(((time == 0) ? 0L : System.currentTimeMillis() + time), method.invoke(curObj, args)); //Получу его из метода и соберу новый объект для записи в кэш
                //System.out.println(" set new cache = " + res.value);
                curStateResults.put(curMeth, res); // и запишу его туда
            }
            return res.value;
        }

        //Если этот метод должен сбромить кэш
        if(curMeth.isAnnotationPresent(Mutator.class)) {

            curState = new State(curState, curMeth, args);
            //если такое состояние уже было  - достану его в текущее
            if (states.containsKey(curState)) { //В каких случаях это сработает? Мы же его только-что создали?! или он будет такой же по equals и hashCode
                curStateResults=states.get(curState);
            } else { //иначе создать новый элемент в мапе состояний
                curStateResults = new ConcurrentHashMap<>();
                states.put(curState, curStateResults);
            }
            //вызвать целевой метод (предполагается это сеттер какой-то)
            return method.invoke(curObj, args);
        }
        //Если до сих пор ни чего не вернули.
        // Значит просто вернем то что метод вычислит без кэща (просто вызов метода без всей этой магии)
        return method.invoke(curObj, args);
    }
}