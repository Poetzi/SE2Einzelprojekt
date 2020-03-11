package com.example.se2einzelprojekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    class Netzwerkübertragung extends AsyncTask {
        private String serverDomain;
        private int portNummer;
        private String matrikelnummer;
        private String antwortVonServer;

        public Netzwerkübertragung(String serverDomain, int portNummer, String matrikelnummer) {
            this.serverDomain = serverDomain;
            this.portNummer = portNummer;
            this.matrikelnummer = matrikelnummer;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Socket socket = new Socket(serverDomain, portNummer);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                BufferedReader inputStreamFromServer = new BufferedReader((new InputStreamReader(socket.getInputStream())));
                outputStream.writeBytes(matrikelnummer + '\n');
                antwortVonServer = inputStreamFromServer.readLine();
                socket.close();
            } catch (Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ein fehler bei der Serververbindung ist aufgetreten!\n" +
                        e.getMessage(), Toast.LENGTH_LONG);
                toast.show();
                Log.e("Fehler", "Serververbindungsfehler");


            }

            return antwortVonServer;
        }
    }
}
