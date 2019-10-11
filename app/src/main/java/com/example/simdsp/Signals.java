package com.example.simdsp;

abstract public class Signals {
    protected double[] t;
    protected double f,a;

    public Signals(double[] ts,double fs,double As){
        t=ts;
        f=fs;
        a=As;
        }
    public abstract double[] Calculate();
}
