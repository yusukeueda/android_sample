package jp.co.se.android.recipe.chapter09;

import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Ch0906 extends Activity implements OnClickListener {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.ch0906_main);

        Button btnRequest = (Button) findViewById(R.id.btnRequest);
        mListView = (ListView) findViewById(android.R.id.list);
        btnRequest.setOnClickListener(this);

        // Ch0905でアクセストークンが取得できているかどうかを取得
        Twitter twitter = Ch0905.getTwitter(this);
        Configuration conf = twitter.getConfiguration();
        String accessToken = conf.getOAuthAccessToken();
        String tokenSecret = conf.getOAuthAccessTokenSecret();
        if (accessToken == null || tokenSecret == null) {
            Toast.makeText(
                    this,
                    "まだ認証が終わっていません。 '" + Ch0905.class.getSimpleName()
                            + ".java'にて認証を行ってください", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRequest) {
            loadTimeline();
        }
    }

    private void loadTimeline() {
        final Twitter twitter = Ch0905.getTwitter(this);

        setProgressBarIndeterminateVisibility(true);

        new AsyncTask<Void, Void, List<twitter4j.Status>>() {

            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    // タイムラインを取得
                    ResponseList<twitter4j.Status> homeTimeline = twitter
                            .getHomeTimeline();
                    return homeTimeline;
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                super.onPostExecute(result);

                setProgressBarIndeterminateVisibility(false);

                // 取得した結果を表示
                mListView.setAdapter(new TwitterAdapter(
                        getApplicationContext(), result));
            }
        }.execute();
    }

    /**
     * リスト表示のアダプタ
     */
    private static class TwitterAdapter extends BaseAdapter {

        private Context mContext;
        private List<Status> mList;
        private LayoutInflater mInflater;

        public TwitterAdapter(Context context, List<twitter4j.Status> result) {
            mContext = context;
            mList = result;

            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public twitter4j.Status getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                View v = mInflater.inflate(R.layout.ch0906_listrow, parent,
                        false);
                holder = new ViewHolder();
                holder.textView1 = (TextView) v.findViewById(R.id.textView1);

                v.setTag(holder);
                convertView = v;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Status status = getItem(position);
            holder.textView1.setText(status.getText());

            return convertView;
        }

        private static class ViewHolder {
            TextView textView1;
        }
    }
}
