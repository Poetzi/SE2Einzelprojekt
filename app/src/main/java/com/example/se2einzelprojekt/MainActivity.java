package com.example.se2einzelprojekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    class Netzwerkübertragung extends AsyncTask{
        String serverDomain;
        String portNummer;
        String matrikelnummer;

        public Netzwerkübertragung(String serverDomain, String portNummer, String matrikelnummer) {
            this.serverDomain = serverDomain;
            this.portNummer = portNummer;
            this.matrikelnummer = matrikelnummer;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }
}
