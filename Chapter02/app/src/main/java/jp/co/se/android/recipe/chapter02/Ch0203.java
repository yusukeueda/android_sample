package jp.co.se.android.recipe.chapter02;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class Ch0203 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0203_main);

        // 親リスト
        ArrayList<HashMap<String, String>> groupData = new ArrayList<HashMap<String, String>>();
        // 子リスト
        ArrayList<ArrayList<HashMap<String, String>>> childData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // 親リストに要素を追加
        HashMap<String, String> groupA = new HashMap<String, String>();
        groupA.put("group", "さる");
        HashMap<String, String> groupB = new HashMap<String, String>();
        groupB.put("group", "とり");

        groupData.add(groupA);
        groupData.add(groupB);

        // 子リストに要素を追加(1)
        ArrayList<HashMap<String, String>> childListA = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> childAA = new HashMap<String, String>();
        childAA.put("group", "さる");
        childAA.put("name", "ニホンザル");
        HashMap<String, String> childAB = new HashMap<String, String>();
        childAB.put("group", "さる");
        childAB.put("name", "テナガザル");
        HashMap<String, String> childAC = new HashMap<String, String>();
        childAC.put("group", "さる");
        childAC.put("name", "メガネザル");

        childListA.add(childAA);
        childListA.add(childAB);
        childListA.add(childAC);

        childData.add(childListA);

        // 子リストに要素を追加(2)
        ArrayList<HashMap<String, String>> childListB = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> childBA = new HashMap<String, String>();
        childBA.put("group", "とり");
        childBA.put("name", "ニワトリ");
        HashMap<String, String> childBB = new HashMap<String, String>();
        childBB.put("group", "とり");
        childBB.put("name", "スズメ");

        childListB.add(childBA);
        childListB.add(childBB);

        childData.add(childListB);

        // 親リスト、子リストを含んだAdapterを生成
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getApplicationContext(), groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { "group" }, new int[] { android.R.id.text1 },
                childData, android.R.layout.simple_expandable_list_item_2,
                new String[] { "name", "group" }, new int[] {
                        android.R.id.text1, android.R.id.text2 });

        // ExpandableListViewにAdapterをセット
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.ExpandablelistView);
        listView.setAdapter(adapter);
    }
}
