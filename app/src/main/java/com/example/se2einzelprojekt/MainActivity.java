package com.example.se2einzelprojekt;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

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

    public void sendenAnServer(View view) throws ExecutionException, InterruptedException {
        matrikelnummer = editTextMatrikelnummer.getText().toString();
        Netzwerkübertragung netzwerkübertragung = new Netzwerkübertragung("se2-isys.aau.at", 53212, matrikelnummer);//se2-isys.aau.at
        netzwerkübertragung.execute();
        netzwerkübertragung.get();
        textViewAntwortVonServer.setText(netzwerkübertragung.getAntwortVonServer());

    }

    public void berechnenVonMatrikelnummer(View view) {
        matrikelnummer = editTextMatrikelnummer.getText().toString();
        char[] charArray = matrikelnummer.toCharArray();
        int[] intArray = new int[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            intArray[i] = Character.getNumericValue(charArray[i]);
        }

        ArrayList<Integer> gerade = new ArrayList<>();
        ArrayList<Integer> ungerade = new ArrayList<>();
        for (int i = 0; i < intArray.length; ++i) {
            if (intArray[i] % 2 == 0) {
                gerade.add(intArray[i]);
            } else {
                ungerade.add(intArray[i]);
            }
        }

        Collections.sort(gerade);
        Collections.sort(ungerade);

        for (int i = 0; i < gerade.size(); i++) {
            intArray[i] = gerade.get(i);
        }
        for (int i = 0; i < ungerade.size(); i++) {
            intArray[i + gerade.size()] = ungerade.get(i);
        }

        matrikelnummer = "";
        for (int i = 0; i < intArray.length; i++) {
            matrikelnummer = matrikelnummer + intArray[i];
        }
        editTextMatrikelnummer.setText(matrikelnummer);
    }


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
            BufferedReader inputStreamFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream.writeBytes(matrikelnummer + "\n");
            antwortVonServer = inputStreamFromServer.readLine();
            socket.close();
            Log.e("Funktioniert", "Hurra!");
        } catch (Exception e) {
            Log.e("Fehler", "Serververbindungsfehler");
        }

        return null;
    }

    public String getAntwortVonServer() {
        return antwortVonServer;
    }
}

