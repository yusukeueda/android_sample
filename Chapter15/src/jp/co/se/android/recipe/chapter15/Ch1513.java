package jp.co.se.android.recipe.chapter15;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

public class Ch1513 extends Activity {
    private static final String TAG = Ch1513.class.getSimpleName();

    public static SimpleDateFormat FORMATTER = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTextView = new TextView(this);
        setContentView(mTextView);

        try {
            // XMLをパース
            FeedChannel channel = parseXml("ch1513.xml");

            // パース結果を画面へ表示
            mTextView.append("パース結果:");
            if (channel != null) {
                mTextView.append("\nタイトル:" + channel.getTitle());
                mTextView.append("\nリンク:" + channel.getLink());
                mTextView.append("\n概要:" + channel.getDescription());
                mTextView.append("\n言語:" + channel.getLanguage());
                mTextView.append("\n発行した時間:" + channel.getPubDate());
                for (Iterator<FeedItem> ite = channel.getFeedItemList()
                        .iterator(); ite.hasNext();) {
                    FeedItem newsItem = ite.next();
                    String msg = String
                            .format(Locale.getDefault(),
                                    "\n\nItem:\n  Title=%1$s\n  Date=%2$s\n  Description=%3$s\n  Link=%4$s",
                                    newsItem.getTitle(), newsItem.getDate(),
                                    newsItem.getDescription(),
                                    newsItem.getLink());
                    mTextView.append(msg);
                }
            } else {
                mTextView.append("RSSのパースに失敗しました。");
            }
        } catch (IOException e) {
            mTextView.append("ファイルを読み込めませんでした。");
        }
    }

    /**
     * RSSのアイテムを取得。パースに失敗した場合はnullが返る
     *
     * @throws IOException
     */
    public FeedChannel parseXml(String filePath) throws IOException {
        AssetManager assets = getResources().getAssets();
        InputStream is = assets.open(filePath);
        AndroidSaxFeedParser parser = new AndroidSaxFeedParser();
        FeedChannel channel = null;
        try {
            channel = parser.parseAll(is);
        } catch (IOException e) {
            mTextView.append("Streamが変だった場合, e=" + e);
        } catch (SAXException e) {
            mTextView.append("パースに失敗した場合, e=" + e);
        }

        return channel;
    }

    /**
     * Android SAX を利用して、RSSを読み取る
     */
    public static class AndroidSaxFeedParser {
        static final String RSS = "rss";
        static final String CHANNEL = "channel";
        static final String TITLE = "title";
        static final String LINK = "link";
        static final String DESCRIPTION = "description";
        static final String LANGUAGE = "language";
        static final String PUB_DATE = "pubDate";
        static final String ITEM = "item";

        public String parseTitle(InputStream inputStream) throws IOException,
                SAXException {
            final StringBuilder sbTitle = new StringBuilder();

            RootElement root = new RootElement("rss");
            Element channel = root.getChild("channel");

            // １．Channelの子要素となるアイテムを取得するためのイベントを設定
            channel.getChild("title").setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            // ３．読み取った文字列を取得
                            sbTitle.append(s);
                        }
                    });

            // ２．読み込み開始
            Xml.parse(inputStream, Xml.Encoding.UTF_8, root.getContentHandler());

            // ４．結果を返す。
            return sbTitle.toString();
        }

        // @formatter: off
        public FeedChannel parseAll(InputStream inputStream)
                throws IOException, SAXException {
            final FeedChannel feedChannel = new FeedChannel();
            final FeedItem feedItem = new FeedItem();
            final ArrayList<FeedItem> itemList = new ArrayList<FeedItem>();

            RootElement root = new RootElement(RSS);
            Element channel = root.getChild(CHANNEL);

            // Channelの子要素となるアイテムを取得するためのイベントを設定
            channel.getChild(TITLE).setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            Log.v(TAG, "channel title=" + s);
                            feedChannel.setTitle(s);
                        }
                    });
            channel.getChild(LINK).setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            Log.v(TAG, "channel link=" + s);
                            feedChannel.setLink(s);
                        }
                    });
            channel.getChild(DESCRIPTION).setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            Log.v(TAG, "channel description=" + s);
                            feedChannel.setDescription(s);
                        }
                    });
            channel.getChild(LANGUAGE).setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            Log.v(TAG, "channel language=" + s);
                            feedChannel.setLanguage(s);
                        }
                    });
            channel.getChild(PUB_DATE).setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            Log.v(TAG, "channel pubDate=" + s);
                            feedChannel.setPubDate(s);
                        }
                    });

            // アイテム取得の定義
            Element item = channel.getChild(ITEM);
            item.setEndElementListener(new EndElementListener() {
                public void end() {
                    Log.v(TAG, "---- <item>");
                    itemList.add(feedItem.copy());
                }
            });
            item.getChild(TITLE).setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            Log.v(TAG, "item title=" + s);
                            feedItem.setTitle(s);
                        }
                    });
            item.getChild(LINK).setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            Log.v(TAG, "item link=" + s);
                            feedItem.setLink(s);
                        }
                    });
            item.getChild(DESCRIPTION).setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            Log.v(TAG, "item description=" + s);
                            feedItem.setDescription(s);
                        }
                    });
            item.getChild(PUB_DATE).setEndTextElementListener(
                    new EndTextElementListener() {
                        public void end(String s) {
                            Log.v(TAG, "item pub_date=" + s);
                            feedItem.setDate(s);
                        }
                    });

            // 読み込み開始
            Xml.parse(inputStream, Xml.Encoding.UTF_8, root.getContentHandler());

            feedChannel.setFeedItemList(itemList);

            return feedChannel;
        }
        // @formatter: on
    }

    public static class FeedChannel {
        private String mTitle;
        private String mLink;
        private String mDescription;
        private String mLanguage;
        private Date mPubDate;
        private ArrayList<FeedItem> mFeedItemList;

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            this.mTitle = title;
        }

        public String getLink() {
            return mLink;
        }

        public void setLink(String link) {
            this.mLink = link;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            this.mDescription = description;
        }

        public String getLanguage() {
            return mLanguage;
        }

        public void setLanguage(String language) {
            this.mLanguage = language;
        }

        public String getPubDate() {
            return FORMATTER.format(mPubDate);
        }

        public void setPubDate(String date) {
            while (!date.endsWith("00")) {
                date += "0";
            }
            try {
                this.mPubDate = FORMATTER.parse(date.trim());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        public ArrayList<FeedItem> getFeedItemList() {
            return mFeedItemList;
        }

        public void setFeedItemList(ArrayList<FeedItem> feedItemList) {
            this.mFeedItemList = feedItemList;
        }
    }

    /**
     * RSSの item 要素ごとにデータを持つBeanクラス
     */
    public static class FeedItem {
        private static final String TAG = FeedItem.class.getSimpleName();
        private String title;
        private String link;
        private String description;
        private Date date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title.trim();
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
            try {
                new URL(link);
            } catch (MalformedURLException e) {
                Log.e(TAG, "不正なURLです。", e);
            }
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description.trim();
        }

        public String getDate() {
            return FORMATTER.format(this.date);
        }

        public void setDate(String date) {
            while (!date.endsWith("00")) {
                date += "0";
            }
            try {
                this.date = FORMATTER.parse(date.trim());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        public FeedItem copy() {
            FeedItem copy = new FeedItem();
            copy.title = title;
            copy.link = link;
            copy.description = description;
            copy.date = date;
            return copy;
        }

    }
}
