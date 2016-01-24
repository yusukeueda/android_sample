package jp.co.se.android.recipe.chapter04;

import jp.co.se.android.recipe.chapter04.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.RemoteViews;

public class Ch0408AppWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_WIDGET_UPDATE = "jp.co.se.android.recipe.action.ACTION_WIDGET_UPDATE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // クリック時に発行するIntentを設定する
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.ch0408_widget);

            Intent intent = new Intent(context, Ch0408AppWidgetProvider.class);
            intent.setAction(ACTION_WIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            // PendingIntent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    appWidgetId, intent, 0);
            views.setOnClickPendingIntent(R.id.text, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // クリック時に発行するIntentを設定する
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        if (ACTION_WIDGET_UPDATE.equals(intent.getAction())) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.ch0408_widget);
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

            // 画面を更新
            long millis = System.currentTimeMillis();
            Time time = new Time();
            time.set(millis);
            views.setTextViewText(R.id.text, time.format("%Y年%m月%d日 %H:%M:%S")
                    + "." + (millis % 1000));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
