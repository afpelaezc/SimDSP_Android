package com.example.simdsp;
//caso nule investigar las obras en la calle 26 Bogotá. Alcance del proyecto: poster. para el martes 15.
//ejemplos: ciclo de demming. martes 22.//preguntas de sondeo, preguntas conceptuales de selección multiple. ejemplos.
public class Square extends Signals {
    double alpha;
    double[] sen=new double[t.length];
    private double[] ssquare= new double[t.length];
    public Square(double[] ts,double fs, double As){super(ts,fs,As);}
    public double[] Calculate(){
        for(int i=0;i<t.length;i++) {
            alpha = 2 * Math.PI * f * t[i];
            sen[i]=Math.round(a*Math.sin(alpha)*100)/100;
            if(sen[i]>0){
                ssquare[i]=a;
            }
            else if (sen[i]<0){
                ssquare[i]=-a;
            }
            else if(sen[i]==0) {
                ssquare[i] = (i == 0) ? a : (sen[i-1]==1)?-a:a;
            }
        }
        return ssquare;

    }
}
