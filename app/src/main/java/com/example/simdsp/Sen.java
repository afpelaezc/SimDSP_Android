package com.example.simdsp;

public class Sen extends Signals{
    double alpha;
    double[] sen=new double[t.length];
    public Sen(double[] ts, double fs, double As) {
        super(ts, fs, As);
    }

    public double[] Calculate(){

        for(int i=0;i<t.length;i++) {
        alpha = 2 * Math.PI * f * t[i];
        sen[i]=Math.round(a*Math.sin(alpha)*100)/100;
        }
        return sen;

    }
}
