package org.example;

import lombok.SneakyThrows;

/**
 * Hello world!
 *
 */
public class App 
{
    @SneakyThrows
    public static void main(String... args) {
        Fraction fr= new Fraction(2,3);
        System.out.println("new num");
        Fractionable num =Utils.cache(fr);
        num.setNum(2);
        num.setDenum(3);
        System.out.println( " num to String = " + num);
        System.out.println("------------------------");

        System.out.println("new num2 ");
        Fractionable num2 = Utils.cache(new Fraction(3,4));
        System.out.println("-------------------------\n");

        System.out.println( " num2 to String = " + num2);
        Thread.sleep(2000);
        System.out.println( " num2 to String = " + num2);
        System.out.println("First  call cacheble");
        System.out.println("num for 2/3 =" + num.doubleValue());// sout сработал
        System.out.println("--------------------");
        System.out.println("num2 for 3/4 =" + num2.doubleValue());// sout сработал
        System.out.println("--------------------\n");
        System.out.println("Second  call cacheble");
        System.out.println("num for 2/3 =" + num.doubleValue());// sout молчит
        System.out.println("--------------------");
        System.out.println( " num2 to String = " + num2);
        System.out.println("num2 for 3/4 =" + num2.doubleValue());// sout молчит
        System.out.println("--------------------\n");

        System.out.println("Call mutator");
        num.setNum(5);
        System.out.println("num for 5/3 =" + num.doubleValue());// sout сработал
        System.out.println("num for 5/3 =" + num.doubleValue());// sout молчит
        System.out.println("num for 5/3= " + num.doubleValue());// sout молчит
        System.out.println("\nCall mutator");
        num.setDenum(8);
        System.out.println("\nCall cacheble");
        System.out.println("num for 5/8 =" + num.doubleValue());// sout сработал
        System.out.println("num for 5/8 =" + num.doubleValue());// sout молчит

        System.out.println("\n Pause");
        Thread.sleep(1000);

        System.out.println("num for 5/8 =" + num.doubleValue() );// sout сработал
        System.out.println( " num to String = " + num);
        System.out.println("num2 for 3/4 =" + num2.doubleValue());// sout молчит
        System.out.println( " num2 to String = " + num2);


        Thread.sleep(4000);
        System.out.println("new Fraction");
        Fractionable num3 = Utils.cache(new Fraction(1,1));
        num3.setNum(7);
        num3.setDenum(16);
        System.out.println("num3 for 7/16 =" + num3.doubleValue());// sout сработал
        System.out.println( " num3 to String = " + num3);
        Thread.sleep(800);
        System.out.println( " num3 to String = " + num3);
        System.out.println("num3 for 7/16 =" + num3.doubleValue());// sout сработал
        System.out.println( " num3 to String = " + num3);


        //CacheCleaner.stop=true;


    }

}
