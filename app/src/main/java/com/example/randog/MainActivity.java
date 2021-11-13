package com.example.randog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.randog.models.Perro;
import com.example.randog.resources.Adaptador;
import com.example.randog.resources.DownLoadImageTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private Button btnConsultar;
    private RequestQueue requestQueue;

    private ListView lvItems;
    private Adaptador adaptador;
    private ArrayList<Perro> arrayPerros = new ArrayList<>();

    private String pesoImg="";
    private String urlImg =".mp4";
    private String uuid ="";

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    static final String URL = "https://random.dog/woof.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        initCompnents();

        fixSsl();
        iniciarFirebase();
        llenarItems();
        setActions();

        initThread();

    }

    private void initCompnents(){
        lvItems = (ListView) findViewById(R.id.lvItems);

        btnConsultar = (Button)  findViewById(R.id.btnConsultar);
    }

    private void iniciarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Perros");

    }

    private void setActions(){

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stringRequest();

            }
        });


    }

    private void llenarItems(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayPerros.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {

                    adItemFromFriebase( objSnapshot.getValue(Perro.class));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stringRequest();
                }

            }
        }).start();
    }

    private void adItemFromFriebase(Perro perro){
        arrayPerros.add(perro);
        adaptador = new Adaptador(this, arrayPerros);
        lvItems.setAdapter(adaptador);
    }

    private void addItem(){
        Perro nuevoPerro = new Perro(pesoImg, urlImg, uuid);
        arrayPerros.add(nuevoPerro);
        databaseReference.child(uuid).setValue(nuevoPerro);

        adaptador = new Adaptador(this, arrayPerros);
        lvItems.setAdapter(adaptador);
    }


    private int stringRequest(){

        if(!urlImg.endsWith(".mp4") && !urlImg.endsWith(".gif") && !urlImg.endsWith(".webm")){
            return 1;
        }
        else{
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //txtPrb.setText(response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                pesoImg = jsonObject.getString("fileSizeBytes");
                                urlImg = jsonObject.getString("url");
                                uuid = arrayPerros.size()+"";

                                if(urlImg.endsWith(".mp4") || urlImg.endsWith(".gif") || urlImg.endsWith(".webm")){
                                    stringRequest();
                                }
                                else{
                                    addItem();
                                    urlImg =".mp4";
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            requestQueue.add(request);
            return 0;
        }

    }
/*
    private void setDataToComponents(){
        txtPrb.setText(urlImg);
        new DownLoadImageTask(imgDog).execute(urlImg);

    }
*/
    private void fixSsl(){
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}