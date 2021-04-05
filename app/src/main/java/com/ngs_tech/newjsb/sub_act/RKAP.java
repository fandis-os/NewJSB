package com.ngs_tech.newjsb.sub_act;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ngs_tech.newjsb.R;

public class RKAP extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> mUploadMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rkap);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        webView.setWebViewClient(new MyWebViewClient());

        // establecemos el cliente chrome para seleccionar archivos
        webView.setWebChromeClient(new MyWebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setVisibility(View.VISIBLE);
                if (newProgress==100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // configuracion del webview
        webView.getSettings().setJavaScriptEnabled(true);

        // cargamos la pagina
        webView.loadUrl("http://114.7.131.86/rkap/");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {

            if (null == mUploadMessage || intent == null || resultCode != RESULT_OK) {
                return;
            }

            Uri[] result = null;
            String dataString = intent.getDataString();

            if (dataString != null) {
                result = new Uri[]{ Uri.parse(dataString) };
            }

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    private class MyWebViewClient extends WebViewClient {

        // permite la navegacion dentro del webview
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        // maneja la accion de seleccionar archivos
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

            // asegurar que no existan callbacks
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }

            mUploadMessage = filePathCallback;

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("file/*"); // set MIME type to filter

            RKAP.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), RKAP.FILECHOOSER_RESULTCODE );

            return true;
        }
    }
}
