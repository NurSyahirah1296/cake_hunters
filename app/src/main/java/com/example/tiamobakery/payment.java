package com.example.tiamobakery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class payment extends AppCompatActivity {
    String userid,name,phone,total,orderid;
    WebView simpleWebView;
    boolean loadingFinished = true;
    boolean redirect = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userid = bundle.getString("userid");
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        total = bundle.getString("total");
        orderid = bundle.getString("orderid");
        simpleWebView = (WebView) findViewById(R.id.webview);
        simpleWebView.setWebViewClient(new MyWebViewClient());

        String url = "https://tiamobakery.000webhostapp.com/cakehunters/payment.php" +
                "userid="+userid+"&mobile="+phone+"&name="+name+"&amount="+total+"&orderid="+orderid;
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("SYAHIRAH","shouldOverrideUrlLoading");

            if (!loadingFinished) {
                redirect = true;
            }
            loadingFinished = false;
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            Log.e("SYAHIRAH","onPageStarted");
            loadingFinished = false;


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e("SYAHIRAH","onPageFinished");
            if(!redirect){
                loadingFinished = true;
            }

            if(loadingFinished && !redirect){

            } else{
                redirect = false;
                Toast.makeText(payment.this, "Redirecting..", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menupayment, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnu_refresh:
                loadPayment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadPayment() {
        String url = "https://tiamobakery.000webhostapp.com/cakehunters/payment.php" +
                "userid="+userid+"&mobile="+phone+"&name="+name+"&amount="+total+"&orderid="+orderid;
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url);
    }

}
