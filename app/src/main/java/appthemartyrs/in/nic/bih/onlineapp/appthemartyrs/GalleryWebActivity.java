package appthemartyrs.in.nic.bih.onlineapp.appthemartyrs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.TrustManagerFactory;

import appthemartyrs.in.nic.bih.onlineapp.appthemartyrs.util.Utiilties;

public class GalleryWebActivity extends Activity {
    private WebView mWebView;
    ProgressBar progressBar;
    String _searchPageURL;
    String REFERER_URL="https://www.bharatkeveer.gov.in/";
    int countload=0;
    TrustManagerFactory tmf = null;
    String TAG="WEBVIEW";

    final Map<String, String> extraHeaders = new HashMap<String, String>();

    private X509Certificate[] mCertificates;
    private PrivateKey mPrivateKey;

    TextView LoadingText;

    boolean isHandlerProceed=false;


    Certificate certificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_web);


        mWebView = (WebView) findViewById(R.id.activity_main_webview);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        LoadingText=findViewById(R.id.LoadingText);

        WebSettings wbset=mWebView.getSettings();
        wbset.setJavaScriptEnabled(true);
        wbset.setDomStorageEnabled(true);
        wbset.setSupportZoom(true);
        wbset.setPluginState(WebSettings.PluginState.ON);

        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        //String MyUA2 = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.151 Safari/535.19";

        mWebView.getSettings().setUserAgentString("Android WebView");
        mWebView.setVisibility(View.INVISIBLE);

        mWebView.clearCache(true);
        mWebView.clearHistory();


        extraHeaders.put("Referer", REFERER_URL);


        try {
            //initTrustStore();

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //InputStream caInput = getResources().openRawResource(R.raw.rootca); // stored at \app\src\main\res\raw
            InputStream caInput = getResources().openRawResource(R.raw.mkvdst); // stored at \app\src\main\res\raw
            certificate = cf.generateCertificate(caInput);
            caInput.close();
        }
        catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
            Toast.makeText(GalleryWebActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }

        if (Utiilties.isOnline(GalleryWebActivity.this) == false) {
            LoadingText.setText("We cannot detect any internet connectivity. Please check your internet connection and try again.");
            AlertDialog.Builder ab = new AlertDialog.Builder(GalleryWebActivity.this);
            ab.setIcon(R.drawable.nointernet);
            ab.setTitle("No Internet Connection");
            ab.setMessage("We cannot detect any internet connectivity. Please check your internet connection and try again.");
            ab.setPositiveButton("[ Turn On ]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(I);
                }
            });
            ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
            ab.show();
        } else {
            LoadingText.setText("Connecting to bharatkeveer.gov.in...");
            mWebView.loadUrl("https://www.bharatkeveer.gov.in/viewMartyrsGalleryPage");
            mWebView.setWebChromeClient(new WebChromeClient(){

                public void onProgressChanged(WebView view, int newProgress){
                    // Update the progress bar with page loading progress
                    progressBar.setProgress(newProgress);
                    if(newProgress == 100){
                        // Hide the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
            mWebView.setWebViewClient(new BKVWebViewClient());
        }

    }
    private void initTrustStore() throws
            java.security.cert.CertificateException, FileNotFoundException,
            IOException, KeyStoreException, NoSuchAlgorithmException {

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore trustedKeyStore = KeyStore.getInstance(keyStoreType);
        trustedKeyStore.load(null, null);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        InputStream caInput = new BufferedInputStream(
                getResources().getAssets().open("mkvdst.crt"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            Log.d("WEBVIEW", "ca-root DN=" + ((X509Certificate) ca).getSubjectDN());
        }
        finally {
            caInput.close();
        }
        trustedKeyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(trustedKeyStore);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (Utiilties.isOnline(GalleryWebActivity.this) == false)
        {
            LoadingText.setText("We cannot detect any internet connectivity. Please check your internet connection and try again.");
            AlertDialog.Builder ab = new AlertDialog.Builder(GalleryWebActivity.this);
            ab.setIcon(R.drawable.nointernet);
            ab.setTitle("No Internet Connection");
            ab.setMessage("We cannot detect any internet connectivity. Please check your internet connection and try again.");
            ab.setPositiveButton("[ Turn On ]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent I = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(I);
                }
            });
            ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
            ab.show();
        }
        else
            {
//            LoadingText.setText("Connecting to bharatkeveer.gov.in...");
//            mWebView.loadUrl("https://www.bharatkeveer.gov.in/viewMartyrsGalleryPage");
//            mWebView.setWebChromeClient(new WebChromeClient());
//            mWebView.setWebViewClient(new BKVWebViewClient());
        }
    }

    public void showAlert(String title,String msg)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(GalleryWebActivity.this);
        ab.setIcon(R.drawable.nointernet);
        ab.setTitle(title);
        ab.setMessage(msg);
        ab.setPositiveButton("[ OK ]", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.dismiss();

            }
        });
        ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
        ab.show();
    }

    private class BKVWebViewClient extends WebViewClient
    {
        boolean timeout = true;
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            // TODO Auto-generated method stub
//            Log.e("onPageStarted",url);
//            super.onPageStarted(view, url, favicon);

            progressBar.setVisibility(View.VISIBLE);

            Runnable run = new Runnable()
            {
                public void run()
                {
                    if(timeout)
                    {
                        // do what you want
                        LoadingText.setText("Connection Timed out. Please try again later.");
                        showAlert("Connection Timed out", "Whoops! Something went wrong. Please try again later.");

                    }
                }
            };
            Handler myHandler = new Handler(Looper.myLooper());
            myHandler.postDelayed(run, 5000);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String url)
        {
            Log.e("shouldOverrideUrl",url);
            webView.loadUrl(url,extraHeaders);
            return true;
        }

        public void onPageFinished(WebView view, String url)
        {
            // TODO Auto-generated method stub
            timeout = false;
//----------------BELOW CODE MOVED TO "onLoadResource"======================
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            LoadingText.setText("Connected to bharatkeveer.gov.in...");
            Log.e("onPageFinished",url);
           super.onPageFinished(view, url);

        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {

            progressBar.setVisibility(View.GONE);
            countload++;
            if(countload>=3)
            {
                view.loadUrl("file:///android_asset/myerrorpage.htm");
            }
            else
            {
                view.loadUrl("https://www.bharatkeveer.gov.in/viewMartyrsGalleryPage");
            }
        }

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request)
        {
            super.onReceivedClientCertRequest(view, request);
            Log.e("ClientCertRequest",request.toString());

            //-----------------added NEW
            if (mCertificates == null || mPrivateKey == null)
            {
                loadCertificateAndPrivateKey();
            }
            request.proceed(mPrivateKey, mCertificates);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)
        {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
            Log.e("dHttpAuthRequest",realm.toString());

        }


        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

            Log.d(TAG, "onReceivedSslError");

//            String message;
//            switch (error.getPrimaryError()) {
//                case SslError.SSL_DATE_INVALID:
//                    message = "SSL date invalid";    // R.string.notification_error_ssl_date_invalid;
//                    break;
//                case SslError.SSL_EXPIRED:
//                    message = "SSL expired";       //R.string.notification_error_ssl_expired;
//                    break;
//                case SslError.SSL_IDMISMATCH:
//                    message = "SSL id mismatch";       //R.string.notification_error_ssl_idmismatch;
//                    break;
//                case SslError.SSL_INVALID:
//                    message = "SSL invalid";       // R.string.notification_error_ssl_invalid;
//                    break;
//                case SslError.SSL_NOTYETVALID:
//                    message = "SSL not yet valid";    // R.string.notification_error_ssl_not_yet_valid;
//                    break;
//                case SslError.SSL_UNTRUSTED:
//                    message = "SSL untrusted";   //R.string.notification_error_ssl_untrusted;
//                    break;
//                default:
//                    message = "SSL certificate invalid";       // R.string.notification_error_ssl_cert_invalid;
//            }
//
//                AlertDialog.Builder ab = new AlertDialog.Builder(GalleryWebActivity.this);
//                ab.setIcon(R.drawable.sslerror);
//                ab.setTitle("SSL Certificate");  //PLAY STORE NOT ALLOWING THIS MSG
//                ab.setTitle(message);
//
//                ab.setMessage("Please Accept SSL Certificate.");
//                ab.setPositiveButton("Accept & Continue", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        isHandlerProceed=true;
//                        dialog.dismiss();
//                        handler.proceed();
//                    }
//                });
//
//                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        isHandlerProceed=true;
//                        LoadingText.setText("Sorry! You have canceled the process. You cannot make donation until you “Accept & Continue” SSL Certificate. Press back button to close this app.");
//                        dialog.dismiss();
//                        handler.cancel();
//
//                    }
//                });
//                ab.create().getWindow().getAttributes().windowAnimations = R.style.alert_animation;
//                ab.show();




            SslCertificate sslCertificateServer = error.getCertificate();
            Certificate pinnedCert = getCertificateForRawResource(R.raw.mkvdst, GalleryWebActivity.this);
            Certificate serverCert = convertSSLCertificateToCertificate(sslCertificateServer);

            if(pinnedCert.equals(serverCert))
            {
                Log.e("pinnedCert","IF-pinnedCert");

                handler.proceed();
            }
            else
                {
                super.onReceivedSslError(view, handler, error);
                Log.e("pinnedCert","ELSE-pinnedCert");
            }

        }
        // credits to @Heath Borders at http://stackoverflow.com/questions/20228800/how-do-i-validate-an-android-net-http-sslcertificate-with-an-x509trustmanager
        private Certificate getX509Certificate(SslCertificate sslCertificate)
        {
            Bundle bundle = SslCertificate.saveState(sslCertificate);
            byte[] bytes = bundle.getByteArray("x509-certificate");
            if (bytes == null)
            {
                return null;
            }
            else
                {
                try
                {
                    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                    return certFactory.generateCertificate(new ByteArrayInputStream(bytes));
                }
                catch (CertificateException e)
                {
                    return null;
                }
            }
        }

        //--------ADDED NEW METHODS ON 18 Feb 2019-----------------

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail)
        {
            return super.onRenderProcessGone(view, detail);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend)
        {
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void onLoadResource(WebView view, String url)
        {
            Log.e("onLoadResource",url);
            LoadingText.setText("Loading bharatkeveer.gov.in...");
            if(url.contains("https://bharatkeveer.gov.in/images/favicon-circle/"))
            {
//                view.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                LoadingText.setText("Welcome to bharatkeveer.gov.in");
            }
            if(url.contains("https://bharatkeveer.gov.in"))
            {
                Toast.makeText(GalleryWebActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
            }
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url)
        {
            Log.e("onPageCommitVisible",url);
            super.onPageCommitVisible(view, url);
//            view.setVisibility(View.VISIBLE);
////            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
        {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse)
        {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args)
        {
            super.onReceivedLoginRequest(view, realm, account, args);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale)
        {
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event)
        {
            super.onUnhandledKeyEvent(view, event);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException
        {
            return super.clone();
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request)
        {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg)
        {
            super.onTooManyRedirects(view, cancelMsg, continueMsg);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url)
        {
            return super.shouldInterceptRequest(view, url);
        }

    }


    private void loadCertificateAndPrivateKey()
    {
        try
        {
            InputStream certificateFileStream = getClass().getResourceAsStream("/assets/bkv.pfx");

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            String password = "password";
            keyStore.load(certificateFileStream, password != null ? password.toCharArray() : null);

            Enumeration<String> aliases = keyStore.aliases();
            String alias = aliases.nextElement();

            Key key = keyStore.getKey(alias, password.toCharArray());
            if (key instanceof PrivateKey) {
                mPrivateKey = (PrivateKey)key;
                Certificate cert = keyStore.getCertificate(alias);
                mCertificates = new X509Certificate[1];
                mCertificates[0] = (X509Certificate)cert;
            }

            certificateFileStream.close();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
//https://stackoverflow.com/questions/35135225/android-webview-handle-onreceivedclientcertrequest
//----------THIS CODE WILL BE USED ABOVE : Function already defined-----------
//        @Override
//        public void onReceivedClientCertRequest(WebView view, final ClientCertRequest request) {
//            if (mCertificates == null || mPrivateKey == null) {
//                loadCertificateAndPrivateKey();
//            }
//            request.proceed(mPrivateKey, mCertificates);
//        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    { //if back key is pressed
        if((keyCode == KeyEvent.KEYCODE_BACK)&& mWebView.canGoBack())
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GalleryWebActivity.this);
            // set title
            alertDialogBuilder.setTitle("Exit");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Back Press is not allowed. Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            GalleryWebActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
            //  }
            //  else {
            //      mWebView.loadUrl(_searchPageURL);
            //  }
            return true;

        }

        return super.onKeyDown(keyCode, event);

    }


    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GalleryWebActivity.this);
        // set title
        alertDialogBuilder.setTitle("Exit");

        // set dialog message
        alertDialogBuilder
                .setMessage("Back Press is not allowed. Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        GalleryWebActivity.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public static Certificate getCertificateForRawResource(int resourceId, Context context) {
        CertificateFactory cf = null;
        Certificate ca = null;
        Resources resources = context.getResources();
        InputStream caInput = resources.openRawResource(resourceId);

        try {
            cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(caInput);
        } catch (CertificateException e) {
            Log.e("getCertificateForRawRes", "exception", e);
        } finally {
            try {
                caInput.close();
            } catch (IOException e) {
                Log.e("getCertificateForRawRes", "exception", e);
            }
        }

        return ca;
    }

    public static Certificate convertSSLCertificateToCertificate(SslCertificate sslCertificate) {
        CertificateFactory cf = null;
        Certificate certificate = null;
        Bundle bundle = sslCertificate.saveState(sslCertificate);
        byte[] bytes = bundle.getByteArray("x509-certificate");

        if (bytes != null) {
            try {
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                Certificate cert = certFactory.generateCertificate(new ByteArrayInputStream(bytes));
                certificate = cert;
            } catch (CertificateException e) {
                Log.e("conSSLCertificateToCert", "exception", e);
            }
        }

        return certificate;
    }
}
