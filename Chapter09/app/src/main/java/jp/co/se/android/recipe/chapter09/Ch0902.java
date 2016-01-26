package jp.co.se.android.recipe.chapter09;

import jp.co.se.android.recipe.chapter09.R;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ch0902 extends Activity implements OnClickListener {
    private GraphUser mGraphUser;
    private StatusCallback mStatusCallback = new SessionStatusCallback();
    private Button mBtnLogin;
    private ProfilePictureView mPpvImage;
    private TextView mTvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0902_main);

        mPpvImage = (ProfilePictureView) findViewById(R.id.image);
        mTvName = (TextView) findViewById(R.id.name);
        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnLogin.setOnClickListener(this);

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

        // アクセストークンを要求した際のログ出力をONにする
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
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
            mBtnLogin.setText(getString(R.string.label_logout));
            mPpvImage.setProfileId(mGraphUser.getId());
            mTvName.setText(mGraphUser.getName());
        } else {
            mBtnLogin.setText(getString(R.string.label_login));
            mPpvImage.setProfileId(null);
            mTvName.setText("");
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
}
