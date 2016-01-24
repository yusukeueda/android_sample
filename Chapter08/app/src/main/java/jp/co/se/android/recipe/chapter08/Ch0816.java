package jp.co.se.android.recipe.chapter08;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ch0816 extends Activity implements OnClickListener {
    private static final String TAG = Ch0816.class.getSimpleName();
    private TextView mTextView;
    private Button mBtnRun;
    private Button mBtnCancel;
    private AsyncTask<Uri, Void, String> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0817_main);

        mBtnRun = (Button) findViewById(R.id.btnRun);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mTextView = (TextView) findViewById(R.id.text);

        mBtnRun.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    private void sendText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.append(text);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRun) {
            requestGetMethod();
        } else if (id == R.id.btnCancel) {
            if (mTask != null) {
                mTask.cancel(false);
                mTextView.append("\n通信をキャンセルしています…。");
            } else {
                mTextView.append("\n通信していません。または、通信キャンセル中です。");
            }
        }
    }

    private void requestGetMethod() {
        mTextView.setText("");
        sendText("通信準備");

        // URLを、扱いやすいUri型で組む
        Uri baseUri = Uri
                .parse("http://android-recipe.herokuapp.com/samples/ch08/json");

        // パラメータの付与
        Uri uri = baseUri.buildUpon().appendQueryParameter("param1", "hoge")
                .build();

        if (mTask == null) {
            mTask = new AsyncTask<Uri, Void, String>() {
                /** 通信において発生したエラー */
                private Throwable mError = null;

                @Override
                protected String doInBackground(Uri... params) {
                    String result = request(params[0]);

                    if (isCancelled()) {
                        sendText("\n通信はキャンセル済みでした。");
                        return result;
                    }

                    return result;
                }

                private String request(Uri uri) {
                    DefaultHttpClient httpClient = new DefaultHttpClient();

                    // タイムアウトの設定
                    HttpParams httpParams = httpClient.getParams();
                    // 接続確立までのタイムアウト設定 (ミリ秒)
                    HttpConnectionParams.setConnectionTimeout(httpParams,
                            5 * 1000);
                    // 接続後までのタイムアウト設定 (ミリ秒)
                    HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);

                    String result = null;
                    HttpGet request = new HttpGet(uri.toString());
                    try {
                        sendText("\n通信開始");
                        result = httpClient.execute(request,
                                new ResponseHandler<String>() {
                                    @Override
                                    public String handleResponse(
                                            HttpResponse response)
                                            throws ClientProtocolException,
                                            IOException {
                                        int statusCode = response
                                                .getStatusLine()
                                                .getStatusCode();
                                        sendText("\nステータスコード : " + statusCode);
                                        if (statusCode == HttpStatus.SC_OK) {
                                            String result = EntityUtils
                                                    .toString(response
                                                            .getEntity());
                                            return result;
                                        } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                                            throw new RuntimeException(
                                                    "404 NOT FOUND");
                                        } else {
                                            throw new RuntimeException(
                                                    "そのほかの通信エラー");
                                        }
                                    }
                                });
                        sendText("\n通信完了");
                    } catch (RuntimeException e) {
                        mError = e;
                        sendText("\n通信失敗" + e.getClass().getSimpleName());
                        Log.e(TAG, "通信失敗", e);
                    } catch (ClientProtocolException e) {
                        mError = e;
                        sendText("\n通信失敗" + e.getClass().getSimpleName());
                        Log.e(TAG, "通信失敗", e);
                    } catch (IOException e) {
                        mError = e;
                        sendText("\n通信失敗" + e.getClass().getSimpleName());
                        Log.e(TAG, "通信失敗", e);
                    } finally {
                        // リソースを開放する
                        httpClient.getConnectionManager().shutdown();
                    }

                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    sendText("\nonPostExecute(String result)");

                    if (mError == null) {
                        sendText("\n通信成功：");
                        sendText("\n  受信したデータ : " + result);
                    } else {
                        sendText("\n通信失敗：");
                        sendText("\n  エラー : " + mError.getMessage());
                    }

                    mTask = null;
                }

                @Override
                protected void onCancelled() {
                    onCancelled(null);
                }

                @Override
                protected void onCancelled(String result) {
                    sendText("\nonCancelled(String result), result=" + result);

                    mTask = null;
                }
            }.execute(uri);
        } else {
            // 現在通信のタスクが実行中。重複して実行されないように制御。
        }
    }
}
