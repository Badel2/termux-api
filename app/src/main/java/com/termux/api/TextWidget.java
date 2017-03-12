package com.termux.api;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Html;
import android.widget.RemoteViews;

import static android.content.Context.MODE_PRIVATE;
import static com.termux.api.util.TermuxApiLogger.info;

public class TextWidget {
    static String PREFERENCE_NAME = "TextWidget";
    int thisWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Context context = null;
    SharedPreferences myPrefs = null;

    TextWidget(Context c, int id){
        thisWidgetId = id;
        context = c;
        myPrefs = context.getSharedPreferences(PREFERENCE_NAME + thisWidgetId,
                MODE_PRIVATE);
        info("Created TextWidget instance, with context " + context);
    }

    public int id(){
        return thisWidgetId;
    }

    public void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        String widget_text = readPreference("text");
        if(widget_text == null) {
            // The widget was first created, let the onCreate function do its job
            // Or maybe the id is just invalid
            info("Invalid widget ID: " + thisWidgetId);
            return;
        }

        // Get the layout for the App Widget and attach an on-click listener
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.text_widget);

        // Create an Intent to launch ExampleActivity
        Boolean openConfigOnClick = readPreference("show_config").equals("yes");
        if(openConfigOnClick) {
            Intent intent = new Intent(context, TextWidgetConfig.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, thisWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, thisWidgetId, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent);
        } else {
            views.setOnClickPendingIntent(R.id.widget_root, null);
        }

        // Always update widget text
        Boolean parse_html = readPreference("parse_html").equals("yes");
        if(parse_html){
            views.setTextViewText(R.id.textView, Html.fromHtml(widget_text));
        }else{
            views.setTextViewText(R.id.textView, widget_text);
        }

        // Doesn't work :(
        //views.setInt(R.id.textView, "setGravity", Gravity.CENTER);

        views.setInt(R.id.widget_root, "setBackgroundColor", Color.parseColor(readPreference("bg_color")));
        views.setTextColor(R.id.textView, Color.parseColor(readPreference("fg_color")));

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(thisWidgetId, views);
    }

    public void saveToPreferences(String key, String data) {
        // no data == empty data
        if(data == null){data = "";}
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(key, data);
        prefsEditor.commit();
    }

    public String readPreference(String key) {
        return (myPrefs.getString(key, null));
    }

    public void setForegroundColor(String color){
        // TODO: don't save invalid colors, even better implement a color picker
        try {
            Color.parseColor(color);
            saveToPreferences("fg_color", color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBackgroundColor(String color){
        try {
            Color.parseColor(color);
            saveToPreferences("bg_color", color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
