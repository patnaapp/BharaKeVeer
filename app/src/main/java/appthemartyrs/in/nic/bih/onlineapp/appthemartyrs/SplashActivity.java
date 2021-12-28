package appthemartyrs.in.nic.bih.onlineapp.appthemartyrs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import appthemartyrs.in.nic.bih.onlineapp.appthemartyrs.util.Utiilties;
import appthemartyrs.in.nic.bih.onlineapp.appthemartyrs.util.Versioninfo;

public class SplashActivity extends Activity {
    TextView tv;

    //final MyCounter timer = new MyCounter(600000, 1000);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //disableSSLCertificateChecking();
        String path = Environment.getExternalStorageDirectory() + "/bkv.pfx".toString();

        Log.e("Path",path);

        if (getIntent().getBooleanExtra("EXIT", true)) {
           // finish();
        }

        if (!Utiilties.isOnline(SplashActivity.this) == false) {
            //new CheckUpdate().execute();
            start();

        } else {

            AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
            ab.setCancelable(false);
            ab.setMessage(Html
                    .fromHtml("<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection</font>"));
            ab.setPositiveButton("Turn On Network Connection",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            //GlobalVariables.isOffline = false;
                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });

            ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
            ab.show();

//            start();
        }
    }


    private void start() {
        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    // Thread will sleep for 2 seconds
                    sleep(2 * 1000);
                    // After 5 seconds redirect to another intent
                    Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    SplashActivity.this.finish();

                } catch (Exception e) {

                }
            }
        };
        background.start();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }




}