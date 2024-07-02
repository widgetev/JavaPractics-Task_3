package org.example;

import lombok.SneakyThrows;

public class Fraction implements Fractionable{
    private int num;
    private int denum;
    public Fraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    public void setNumDirect(int num) {
        this.num = num;
    }

    public void setDenumDirect(int denum) {
        this.denum = denum;
    }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(1000)
    public double doubleValue() {
        return (double) num/denum;
    }

//
    @Override
    @Cache(1500)
    public String toString() {
        return super.toString() + "{" + num + "/" + denum  + "}";
    }
}
