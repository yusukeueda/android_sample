package jp.co.se.android.recipe.chapter09;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Ch0903 extends Activity implements OnClickListener {
    private GraphUser mGraphUser;
    private StatusCallback mStatusCallback = new SessionStatusCallback();
    private Button mBtnLogin;
    private Button mBtnPost;
    private EditText mEtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0903_main);

        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnLogin.setOnClickListener(this);
        mBtnPost = (Button) findViewById(R.id.post);
        mBtnPost.setOnClickListener(this);
        mEtInput = (EditText) findViewById(R.id.input);

        // セッションを取得
        Session session = Session.getActiveSession();
        if (session == null) {
            // セッションを保存している場合は復帰
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, mStatusCallback,
                        savedInstanceState);
            }
            // セッションがない場合は新たに生成
            if (session == null) {
                session = new Session(this);
            }
            // セッションの状態をセット
            Session.setActiveSession(session);
            // トークンが既に存在してる場合はセッションステータスを要求
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this)
                        .setCallback(mStatusCallback));
            }
        } else {
            // セッションがある場合はプロフィールを取得
            getMyProfile(session);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // セッションステータスコールバックを登録
        Session.getActiveSession().addCallback(mStatusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        // セッションステータスコールバックを破棄
        Session.getActiveSession().removeCallback(mStatusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ログイン結果をFacebookSDKに渡す
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // セッション情報を保存
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    /**
     * セッションステータスコールバック.
     */
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(final Session session, SessionState state,
                Exception exception) {
            // 取得したセッションを元にプロフィールを取得
            getMyProfile(session);
        }
    }

    /**
     * プロフィールを取得.
     * 
     * @param session
     */
    private void getMyProfile(final Session session) {
        if (session != null && session.isOpened()) {
            // 自分のユーザー情報を取得
            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user,
                                Response response) {
                            if (session == Session.getActiveSession()) {
                                if (user != null) {
                                    mGraphUser = user;
                                    updateView();
                                }
                            }
                        }
                    });
            Request.executeBatchAsync(request);
        }
    }

    /**
     * 画面を更新.
     */
    private void updateView() {
        Session session = Session.getActiveSession();
        if (mGraphUser != null && session.isOpened()) {
            mBtnLogin.setText("ログアウト");
            mBtnPost.setEnabled(true);
            mEtInput.setEnabled(true);
        } else {
            mBtnLogin.setText("ログイン");
            mBtnPost.setEnabled(false);
            mEtInput.setEnabled(false);
            mGraphUser = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login) {
            Session session = Session.getActiveSession();
            if (session.isOpened()) {
                // ログアウト処理
                onClickLogout();
            } else {
                // ログイン処理
                onClickLogin();
            }
        } else if (id == R.id.post) {
            String message = mEtInput.getText().toString();
            postWall(message);
        }
    }

    /**
     * Facebookにログイン.
     */
    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setCallback(mStatusCallback));
        } else {
            Session.openActiveSession(this, true, mStatusCallback);
        }
    }

    /**
     * Facebookからログアウト.
     */
    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
            updateView();
        }
    }

    /**
     * 近況をアップデート.
     * 
     * @param message
     */
    private void postWall(String message) {
        Request.newStatusUpdateRequest(Session.getActiveSession(), message,
                null, null, new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        mEtInput.setText("");
                        Toast.makeText(Ch0903.this, "近況をアップデートしました",
                                Toast.LENGTH_SHORT).show();
                    }
                }).executeAsync();
    }
}
