package com.example.demo1;

import java.text.DecimalFormat;

public class ResultEntry {
    private double principal;
    private double rate;
    private double time;
    private double result;

    public ResultEntry(double principal, double rate, double time, double result) {
        this.principal = principal;
        this.rate = rate;
        this.time = time;
        this.result = result;
    }

    // Геттеры и сеттеры для каждого поля, если необходимо получить или установить значения полей.

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
