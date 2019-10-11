package com.example.simdsp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.support.v4.app.Fragment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Process process = null;
    Thread thread;
    DataOutputStream os=null ;
    DataInputStream  is=null;
    public EditText text;
    Button button1;
    Button button2;
    int i=0,aux=1;
    double f=100;
    double[] t = new double[(int)f*4+1];
    double[] y= new double[t.length];
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text =(EditText)findViewById(R.id.editText);
        button1=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);

        button2.setEnabled(false);

        text.setEnabled(false);
        text.setTypeface(Typeface.MONOSPACE);
        text.setTextColor(Color.WHITE);
        //text.setText("hola chan\n");
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        thread = new Thread() {
            public void run() {

                BufferedReader br;


                while (true) {


                    if (is != null) {

                        try {

                            br = new BufferedReader(new InputStreamReader(is));
                            final String data = br.readLine();

                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            text.setText( text.getText().append(data+"\n"));
                                                                                   }
                                    }

                            );

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                }
            }

        };

        thread.start();
    }

    public void Signal(View view){
        Functions task=new Functions();
        double A=1;
        double T=0;
        double fs=f*4;
        if(aux==1) {
            for (int j = 0; T <= 1;j++) {
                t[j] = T;
                T = (1 / fs) + T;
            }
            /*Sen s1 = new Sen(t, f, A);
            y = s1.Calculate();*/
            Square s2 = new Square(t,f,A);
            y = s2.Calculate();
            aux=0;
        }
        task.execute("");

    }

    private class Functions extends AsyncTask<String, Void, String> {
     @Override
     protected String doInBackground(String... params) {

             runOnUiThread(
                 new Runnable() {
                     @Override
                     public void run() {
                         try{

                             //Sen s1 = new Sen(t,f,A);
                             //y=s1.Calculate();
                             //for(int i=0;i<t.length;i++) {
                                 text.append((y[i] + "\n"));
                                 i++;
                             //}
                         }catch (NullPointerException e){
                             text.setText("hola chan exep\n");
                             e.printStackTrace();
                             //text.setText("hola chan"+"\n");
                         }
                     }
                 }
         );
         return "";
     }
     @Override
     protected void onPostExecute(String result) {
        //TextView txt = (TextView) findViewById(R.id.output);
        //txt.setText("Executed"); // txt.setText(result);
        //might want to change "executed" for the returned string passed
             // into onPostExecute() but that is upto you
         }

         @Override
         protected void onPreExecute() {}

         @Override
         protected void onProgressUpdate(Void... values) {}
     }

    public void startDSP(View view){

        String myExec;




        try {
            myExec=  "cp /data/local/tmp/simdsp /data/data/com.example.simdsp/simdsp";
            process = Runtime.getRuntime().exec(myExec);
            process.waitFor();
            myExec=  "chmod 777 /data/data/com.example.simdsp/simdsp";
            process = Runtime.getRuntime().exec(myExec);
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }


        myExec = "/data/data/com.example.simdsp/simdsp";
        String myEnv[] = {"LD_LIBRARY_PATH=/data/data/com.example.simdsp/lib"};

        try {
            process = Runtime.getRuntime().exec(myExec,myEnv);
            if(process==null) {
                Toast.makeText(getApplicationContext(),"Couldn't start SimDSP sketch",Toast.LENGTH_SHORT).show();

            }
            else{

                Toast.makeText(getApplicationContext(),"Starting SimDSP sketch",Toast.LENGTH_SHORT).show();
                os = new DataOutputStream(process.getOutputStream());
                is = new DataInputStream(process.getInputStream());
                button2.setEnabled(true);
                button1.setEnabled(false);

            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Couldn't start SimDSP sketch",Toast.LENGTH_SHORT).show();

        }


    }

    public void stopDSP(View view){

        if(process!=null){
            process.destroy();
            process=null;
            Toast.makeText(getApplicationContext(),"Stopping SimDSP sketch",Toast.LENGTH_SHORT).show();
            button1.setEnabled(true);
            button2.setEnabled(false);
        }



    }

    public void clearConsole(View view){
        text.getEditableText().clear();
    }

}

