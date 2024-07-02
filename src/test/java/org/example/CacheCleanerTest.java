package org.example;

import org.apache.commons.lang3.ThreadUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CacheCleanerTest {

    @Test
    @DisplayName("Check expire cache lifeTime")
    void testExpireCacheTime() throws InterruptedException {
        FractionTst testFraction = new FractionTst(1,4);
        FractionableTst testFractionable = Utils.cache(testFraction);

        double resCache1 = testFractionable.doubleValue(); //Первый вызов ++
        Thread.sleep(1200);
        double resCache3 = testFractionable.doubleValue(); //кэш сбросился, т.к. 1200 > 1000
        Assertions.assertEquals(resCache1,resCache3);
        Assertions.assertEquals(2, testFraction.dValCount);

    }

    @Test
    @DisplayName("Prolongation cache lifeTime")
    void testCacheTimeProlongation() throws InterruptedException {
        FractionTst testFraction = new FractionTst(1,4);
        FractionableTst testFractionable = Utils.cache(testFraction);
        double resCache1 = testFractionable.doubleValue(); //Первый вызов ++
        Thread.sleep(500);
        double resCache2 = testFractionable.doubleValue(); //второй - из кеша и продляет его
        Assertions.assertEquals(1, testFraction.dValCount);
        Thread.sleep(1200);
        double resCache3 = testFractionable.doubleValue(); //3й - кэш сбросился, т.к. 1200 > 1000
        Assertions.assertEquals(resCache1,resCache2);
        Assertions.assertEquals(resCache1,resCache3);
        Assertions.assertEquals(2, testFraction.dValCount);
    }
    @Test
    void testThread() throws InterruptedException {
        //App.main(); не буду дергать там много вывода в консоль

        Fraction fr= new Fraction(2,3);
        Fractionable num =Utils.cache(fr);
        num.setNum(2);
        num.setDenum(3);
        num.toString();

        Fractionable num2 = Utils.cache(new Fraction(3,4));

        num2.toString();
        Thread.sleep(2000);
        num2.toString();
        num.doubleValue();// sout сработал
        num2.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num2.toString();
        num2.doubleValue();// sout молчит

        num.setNum(5);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        num.setNum(2);
        num.setDenum(3);
        num.doubleValue();// sout молчит
        num.setDenum(8);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит
        Thread.sleep(1100);

        num.doubleValue();// sout сработал
        num.toString();
        num2.doubleValue();// sout молчит
        num2.toString();

        Thread.sleep(4000);
                Fractionable num3 = Utils.cache(new Fraction(1,1));
        num3.setNum(7);
        num3.setDenum(16);
        num3.doubleValue();// sout сработал
        num3.toString();
        Thread.sleep(800);
        num3.toString();
        num3.doubleValue();// sout сработал
        num3.toString();

        Collection<Thread> cleaner = ThreadUtils.findThreadsByName("Cache cleaner");
        Assertions.assertEquals(1,cleaner.size());

    }

}




class FractionTst implements FractionableTst{
    private int num;
    private int denum;

    int dValCount = 0; //счетчик обращений к методам. Если ожидаем что результат в кеше счетчик не должен измениться

    public FractionTst(int num, int denum) {
        this.num=num;
        this.denum = denum;
    }

    @Override
    public void setNumDirect(int num) {
        this.num = num;
    }
    @Override
    public void setDenumDirect(int denum) {
        this.denum = denum;
    }

    @Override
    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Override
    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(1000)
    public double doubleValue() {
        //System.out.println("doubleValue ++ ");
        dValCount++;
        return (double) num/denum;
    }

    @Override
    public double doubleValueDirect() {
        //System.out.println("doubleValueDirect ++ ");
        dValCount++;
        return (double) num/denum;
    }
}


interface FractionableTst extends Fractionable{
    double doubleValueDirect();
}
