package org.example;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

class CacheCleaner implements Runnable {
    private static final Long minSleepTime = 10L;
    private static Long sleepTime=minSleepTime; //периодичность запуска клинера
    static List<Map<org.example.State, Map<Method, Result>>> list = new ArrayList<>(); //список всех кэшей

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread th = new Thread(r);
            th.setDaemon(true);
            return th;
        }
    });

    private CacheCleaner() {}

    @SneakyThrows
    public static void setSleepTime(long mils){
        if(mils <= minSleepTime )
            throw new IllegalArgumentException("Cache time must be over 1");
        //ХЗ как это подбирать. Пусть будет минимальное значение кэша/2
        //но этого мало, в идеале, надо каждый раз перебирать актуальные кэши и брать mils от туда
        if (((mils/2) < sleepTime) || sleepTime.equals(minSleepTime)) {
            sleepTime = mils/2;
            var t = new Thread(() -> setNewSchedulerPeriod(sleepTime)); //Иначе это задержит основной поток ожидая awaitTermination
            t.setDaemon(true);
            t.start();
        }
    }

    @SneakyThrows
    private static void setNewSchedulerPeriod(long mils) {
        scheduler.awaitTermination(1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(CacheCleanerHolder.cleaner::run, 1, mils, TimeUnit.MILLISECONDS);
    }

    public static CacheCleaner getInstance(){
        scheduler.scheduleAtFixedRate(CacheCleanerHolder.cleaner::run, 1, minSleepTime, TimeUnit.MILLISECONDS);
        return CacheCleanerHolder.cleaner;
    };

    @Override
    @SneakyThrows
    public void run() {
            for (Map<org.example.State, Map<Method, Result>> states : list) {
               for(Map<Method, Result> map : states.values()){
                    for (Method met : map.keySet()) {
                        Result result = map.get(met);
                        if (result.expireTime == 0) continue;
                        if (!result.isAlive()) {
                            map.remove(met);
                        }
                    }
            }//keyset
            }
    }

    private static class CacheCleanerHolder { //такая реализация singletone
        public static final CacheCleaner cleaner = new CacheCleaner();

    }
}
