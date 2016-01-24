package jp.co.se.android.recipe.chapter08;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Ch0804 extends Activity {

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // タイトルバーに進捗インジケータを表示する準備
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.ch0804_main);

        mWebView = (WebView) findViewById(R.id.web);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                // 進捗状況を取得
                mProgressBar.setProgress(newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                // プログレスバーを表示する
                mProgressBar.setVisibility(View.VISIBLE);
                // 進捗インジケータを表示する
                setProgressBarIndeterminateVisibility(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // プログレスバーを表示する
                mProgressBar.setVisibility(View.GONE);
                // 進捗インジケータを表示する
                setProgressBarIndeterminateVisibility(false);
            }
        });
        mWebView.loadUrl("http://android-recipe.herokuapp.com/samples/ch08/history");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mWebView.stopLoading();
        ViewGroup webParent = (ViewGroup) mWebView.getParent();
        if (webParent != null) {
            webParent.removeView(mWebView);
        }
        mWebView.destroy();
    }
}
