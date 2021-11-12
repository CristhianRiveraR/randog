package com.example.randog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.randog.resources.DownLoadImageTask;


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
import java.util.ResourceBundle;


public class MainActivity extends AppCompatActivity {

    Button btnConsultar;
    TextView txtPrb;
    RequestQueue requestQueue;
    ImageView imgDog;
    String pesoImg;
    String urlImg =".mp4";
    //Pruebas



    //
    static final String URL = "https://random.dog/woof.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        initCompnents();

        fixSsl();

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stringRequest();
                //setDataToComponents();
            }
        });

    }

    private void initCompnents(){
        btnConsultar = findViewById(R.id.btnConsultar);
        txtPrb = findViewById(R.id.txtPrb);
        imgDog = findViewById(R.id.imgDog);
    }

    private void callStringRequest(){

        do{
            stringRequest();
        }while (urlImg.endsWith(".mp4") || urlImg.endsWith(".gif"));

    }

    private int stringRequest(){

        if(!urlImg.endsWith(".mp4") && !urlImg.endsWith(".gif")){
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

                                if(urlImg.endsWith(".mp4") || urlImg.endsWith(".gif")){
                                    stringRequest();
                                }
                                else{
                                    setDataToComponents();
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
                            int x= 0;
                        }
                    }
            );
            requestQueue.add(request);
            return 0;
        }

    }

    private void setDataToComponents(){
        txtPrb.setText(pesoImg);
        new DownLoadImageTask(imgDog).execute(urlImg);

    }

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
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}