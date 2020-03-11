package com.example.se2einzelprojekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText editTextMatrikelnummer;
    Button buttonSendToServer;
    TextView textViewAntwortVonServer;
    String matrikelnummer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMatrikelnummer = findViewById(R.id.editTextMatrikelnummer);
        buttonSendToServer = findViewById(R.id.buttonSendToServer);
        textViewAntwortVonServer = findViewById(R.id.textViewAntwortVonServer);
    }

    public void sendenAnServer(View view){
        matrikelnummer = editTextMatrikelnummer.getText().toString();
        Netzwerkübertragung netzwerkübertragung = new Netzwerkübertragung("se2-isys.aau.at", 53212, matrikelnummer);
        netzwerkübertragung.execute();
        textViewAntwortVonServer.setText(netzwerkübertragung.antwortVonServer);
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
