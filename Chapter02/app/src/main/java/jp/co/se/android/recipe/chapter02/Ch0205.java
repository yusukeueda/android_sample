package jp.co.se.android.recipe.chapter02;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Ch0205 extends ListActivity {
    /***
     * インデックスデータ
     *
     * @author yokmama
     *
     */
    private class BindData {
        String title;
        String line1;
        String line2;

        public BindData(String string0, String string1, String string2) {
            this.title = string0;
            this.line1 = string1;
            this.line2 = string2;
        }
    }

    // インデックスを表示するためのサンプルデータ
    private BindData[] INDEX_DATA = new BindData[] {
            new BindData("タイトル１", null, null),
            new BindData(null, "foo", "bar"),
            new BindData("タイトル2", null, null),
            new BindData(null, "hoge", "fuga"),
            new BindData(null, "null", "po"),
            new BindData("タイトル3", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("タイトル4", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("タイトル5", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("タイトル6", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("タイトル7", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("タイトル8", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("タイトル9", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("タイトル10", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"), };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0205_main);

        // インデックスデータの追加
        List<BindData> list = new ArrayList<BindData>();
        for (int i = 0; i < INDEX_DATA.length; i++) {
            list.add(INDEX_DATA[i]);
        }

        // インデックス表示のアダプターを生成
        Ch0205Adapter adapter = new Ch0205Adapter(this, list);

        // アダプターを設定
        setListAdapter(adapter);
    }

    /***
     * リスト高速表示のためViewを保持ためのクラス
     *
     * @author yokmama
     *
     */
    private class ViewHolder {
        TextView title;
        TextView line1;
        TextView line2;
    }

    private class Ch0205Adapter extends ArrayAdapter<BindData> {
        private LayoutInflater mInflater;

        public Ch0205Adapter(Context context, List<BindData> objects) {
            super(context, 0, objects);
            this.mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public boolean isEnabled(int position) {
            // 選択不可にする
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            // リストアイテム表示用のレイアウトを読み込み生成
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ch0205_list_item,
                        parent, false);
                // リストの表示を高速化するため、View保持用クラスを生成しTagに設定
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.line1 = (TextView) convertView.findViewById(R.id.line1);
                holder.line2 = (TextView) convertView.findViewById(R.id.line2);
                convertView.setTag(holder);
            } else {
                // View保持用のインスタンスをTagから取得
                holder = (ViewHolder) convertView.getTag();
            }

            // Adapterに設定されているリストからBindDataを取得
            BindData data = getItem(position);

            if (getItem(position).title != null) {
                // インデックス用のインデックスデータならインデックスタイトルを表示
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(data.title);
                holder.line1.setVisibility(View.GONE);
                holder.line2.setVisibility(View.GONE);
                // line1,2
            } else {
                // 　インデックス用のデータでない場合はテキストだけを表示
                holder.title.setVisibility(View.GONE);
                holder.line1.setVisibility(View.VISIBLE);
                holder.line1.setText(data.line1);
                holder.line2.setVisibility(View.VISIBLE);
                holder.line2.setText(data.line2);
            }
            return convertView;
        }
    }
}
