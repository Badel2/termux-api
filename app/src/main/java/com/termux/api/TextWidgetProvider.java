package com.termux.api;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import static com.termux.api.TextWidgetConfig.updateAllWidgets;

public class TextWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        updateAllWidgets(context, appWidgetManager, appWidgetIds);
    }

    /*
     * This is called when an instance the App Widget is created for the first
     * time.
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /*
     * This is called for every broadcast and before each of the above callback
     * methods.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    /*
     * This is called When all instances of App Widget is deleted from the App
     * Widget host.
     */
    @Override
    public void onDisabled(Context context) {
        // Unschedule any timers and tasks
        super.onDisabled(context);
    }

    /*
     * This is called every time an App Widget is deleted from the App Widget
     * host.
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Unschedule any timers and tasks
        // TODO: remove widget data from shared preferences
        super.onDeleted(context, appWidgetIds);
    }

}