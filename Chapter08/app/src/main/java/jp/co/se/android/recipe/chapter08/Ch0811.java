package jp.co.se.android.recipe.chapter08;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Ch0811 extends Activity {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(this);
        setContentView(mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        setupGeolocation(mWebView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                    Callback callback) {
                GeolocationPermissionDialog.newInstance(callback, origin).show(
                        getFragmentManager(),
                        GeolocationPermissionDialog.FRG_TAG);
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                FragmentManager fm = getFragmentManager();
                Fragment f = fm
                        .findFragmentByTag(GeolocationPermissionDialog.FRG_TAG);
                if (f != null) {
                    GeolocationPermissionDialog d = (GeolocationPermissionDialog) f;
                    d.dismissAllowingStateLoss();
                }
            }
        });
        mWebView.loadUrl("http://android-recipe.herokuapp.com/samples/ch08/geolocation");
    }

    private void setupGeolocation(WebView webView) {
        WebSettings ws = webView.getSettings();

        // 位置情報の取得を有効にする
        ws.setGeolocationEnabled(true);

        // データベースを保存する場所を指定する
        File databaseDir = getDir("databases", Context.MODE_PRIVATE);
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }
        ws.setGeolocationDatabasePath(databaseDir.getPath());
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

    public static class GeolocationPermissionDialog extends DialogFragment {
        public static final String FRG_TAG = GeolocationPermissionDialog.class
                .getSimpleName();
        public static final String EXTRA_ORIGIN = "extra.ORIGIN";

        private GeolocationPermissions.Callback mCallback;

        public static final GeolocationPermissionDialog newInstance(
                GeolocationPermissions.Callback callback, String origin) {
            GeolocationPermissionDialog f = new GeolocationPermissionDialog();
            f.mCallback = callback;
            Bundle args = new Bundle();
            args.putString(EXTRA_ORIGIN, origin);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("位置情報の要求");
            builder.setMessage(String.format("このページはあなたの位置情報を求めています。\n許可しますか？",
                    getArguments().getString(EXTRA_ORIGIN)));

            builder.setPositiveButton("はい",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mCallback != null) {
                                mCallback.invoke(
                                        getArguments().getString(EXTRA_ORIGIN),
                                        true, false);
                            }
                            mCallback = null;
                        }
                    });
            builder.setNegativeButton("キャンセル", null);

            return builder.create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);

            if (mCallback != null) {
                mCallback.invoke(getArguments().getString(EXTRA_ORIGIN), false,
                        false);
                mCallback = null;
            }
        }
    }
}
