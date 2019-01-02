package com.example.jeffr.capstone_stage2.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.ui.DetailCategoryFavoriteActivity;
import timber.log.Timber;

import static com.example.jeffr.capstone_stage2.widget.ListViewWidgetService.categoryName;

public class FavoriteListWidget extends AppWidgetProvider {

  private static final String PREVIOUS_BUTTON = "previous_button";
  private static final String NEXT_BUTTON = "next_button";


  @Override public void onReceive(Context context, Intent intent) {
    switch (intent.getAction()) {
      case PREVIOUS_BUTTON:
        Timber.d("Previous button press");
        --ListViewWidgetService.categoryIndex;
        onUpdate(context, AppWidgetManager.getInstance(context),
            intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_ID));
        break;
      case NEXT_BUTTON:
        Timber.d("Next button press");
        ++ListViewWidgetService.categoryIndex;
        onUpdate(context, AppWidgetManager.getInstance(context),
            intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_ID));
        break;
      default:
        Timber.d("Unknown button press");
        super.onReceive(context, intent);
    }
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      Timber.d("Got to update method");
      RemoteViews views =
          new RemoteViews(context.getPackageName(), R.layout.widget_restaurant_list);
      Intent serviceIntent = new Intent(context, ListViewWidgetService.class);
      serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
      serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
      views.setRemoteAdapter(R.id.resturant_list_view, serviceIntent);
      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.resturant_list_view);
      views.setEmptyView(R.id.resturant_list_view, R.id.empty_layout);
      views.setTextViewText(R.id.widget_category_title,categoryName);
      views.setOnClickPendingIntent(R.id.back_button,
          getPendingSelfIntent(context, PREVIOUS_BUTTON, appWidgetIds));
      views.setOnClickPendingIntent(R.id.forward_button,
          getPendingSelfIntent(context, NEXT_BUTTON, appWidgetIds));
      Intent viewIntent = new Intent(context, DetailCategoryFavoriteActivity.class);
      viewIntent.putExtra("Category", ListViewWidgetService.currentCategory);
      PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
      views.setOnClickPendingIntent(R.id.default_widget_layout, viewPendingIntent);
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }

  protected PendingIntent getPendingSelfIntent(Context context, String action, int[] appWidgetIds) {
    Intent intent = new Intent(context, getClass());
    intent.setAction(action);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
    return PendingIntent.getBroadcast(context, 0, intent, 0);
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}

