package org.example;

public class Result {
    long expireTime;
    Object value;
    public Result(long expireTime, Object value) {
        this.expireTime = expireTime;
        this.value = value;
    }
    public boolean isAlive() {
        return (expireTime >= System.currentTimeMillis());
    }
    @Override
    public String toString() {
        return "Result{" + value +
                " expireTime in " + (expireTime - System.currentTimeMillis()) +
                '}';
    }
}
