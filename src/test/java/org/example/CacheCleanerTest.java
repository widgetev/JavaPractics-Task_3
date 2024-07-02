package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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
