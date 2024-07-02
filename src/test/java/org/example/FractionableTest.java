package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Этот тест взять из предыдущего задания и не менялся должен отрабатывать без ошибок
class FractionableTest {

    @ParameterizedTest(name = "Set Num: \"{0}\"")
    @MethodSource("org.example.TestDataSource#num")
    void setNum(int x) {
        Fractionable f =Utils.cache(new Fraction(1,1));
        Double num = f.doubleValue();// сработал
        f.setNum(x);
        Assertions.assertEquals(x,f.doubleValue()); //если setNum отработал то результатом вычисления будет он же
    }

    @ParameterizedTest(name = "Set Deum: \"{0}\"")
    @MethodSource("org.example.TestDataSource#denum")
    void setDenum(int x) {
        Fractionable f =Utils.cache(new Fraction(1,1));
        Double num = f.doubleValue();// сработал
        f.setDenum(x);
        Assertions.assertEquals((double) 1/x, f.doubleValue()); //если setDeum отработал то результатом вычисления будет 1/Denum
    }

    @Test
    void doubleValue() {
        Fractionable f =Utils.cache(new Fraction(1,1));
        Double num = f.doubleValue();// сработал
        f.setNum(2);
        f.setDenum(3);
        Assertions.assertEquals((double) 2/3, f.doubleValue()); //если setDeum отработал то результатом вычисления будет 1/Denum
        f.setNumDirect(1);
        f.setDenumDirect(8);
        Assertions.assertEquals((double) 2/3, f.doubleValue()); //кэшированное значение не изменилось
        Assertions.assertNotEquals((double) 1/8, f.doubleValue()); //кэшированное значение отличается от того что установили
        f.setNum(1);
        Assertions.assertEquals((double) 1/8, f.doubleValue()); //использование любого Mutable метода сброисит кэш
        Assertions.assertNotEquals((double) 2/3, f.doubleValue()); //использование любого Mutable метода сброисит кэш
        f.setNum(2);
        f.setDenum(3);
        Assertions.assertEquals((double) 2/3, f.doubleValue()); //такое значение уже должно быть в кеше

    }
}

class TestDataSource {
    public static List<Integer> num() {
        return List.of(2,5, 10);
    }

    public static List<Integer> denum() {
        return List.of(1,8, 15);
    }


}

