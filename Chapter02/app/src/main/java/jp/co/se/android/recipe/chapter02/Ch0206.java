package jp.co.se.android.recipe.chapter02;

import java.util.ArrayList;
import java.util.List;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Ch0206 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0201_main);

        // リソースに準備した画像ファイルからBitmapを作成
        Bitmap image;
        image = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);

        // データの作成
        List<CustomData> objects = new ArrayList<CustomData>();
        CustomData item1 = new CustomData();
        item1.setImagaData(image);
        item1.setTextData("1st");

        CustomData item2 = new CustomData();
        item2.setImagaData(image);
        item2.setTextData("2nd");

        CustomData item3 = new CustomData();
        item3.setImagaData(image);
        item3.setTextData("3rd");

        objects.add(item1);
        objects.add(item2);
        objects.add(item3);

        CustomAdapter customAdapater = new CustomAdapter(this, 0, objects);

        ListView listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(customAdapater);
    }

    public class CustomData {
        private Bitmap mImageData;
        private String mTextData;

        public void setImagaData(Bitmap image) {
            mImageData = image;
        }

        public Bitmap getImageData() {
            return mImageData;
        }

        public void setTextData(String text) {
            mTextData = text;
        }

        public String getTextData() {
            return mTextData;
        }
    }

    public class CustomAdapter extends ArrayAdapter<CustomData> {
        private LayoutInflater mLayoutInflater;

        public CustomAdapter(Context context, int textViewResourceId,
                List<CustomData> objects) {
            super(context, textViewResourceId, objects);
            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 特定の行(position)のデータを取得
            CustomData item = (CustomData) getItem(position);

            // 同じ行に表示されるViewは使い回しされるため初回だけ生成
            if (null == convertView) {
                convertView = mLayoutInflater.inflate(
                        R.layout.ch0206_list_item, null);
            }

            // データをViewの各Widgetにセット
            ImageView imageView;
            imageView = (ImageView) convertView.findViewById(R.id.image);
            imageView.setImageBitmap(item.getImageData());

            TextView textView;
            textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(item.getTextData());

            return convertView;
        }
    }
}
