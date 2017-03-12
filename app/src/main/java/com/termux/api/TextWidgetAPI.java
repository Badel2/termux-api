package com.termux.api;

import android.content.Context;
import android.content.Intent;

import com.termux.api.util.ResultReturner;
import java.io.PrintWriter;

public class TextWidgetAPI {

    public static void onReceive(final Context context, final Intent intent) {
        final int widgetId = intent.getIntExtra("id", -1);

        ResultReturner.returnData(context, intent, new ResultReturner.WithStringInput() {
            @Override
            public void writeResult(PrintWriter out) throws Exception {
                if(widgetId == -1) {
                    out.println("ERROR: No widget id");
                    return;
                }

                TextWidget t = new TextWidget(context, widgetId);
                if(t.readPreference("text") == null){
                    // The widget does not exist or has not yet been saved for the first time
                    out.println("ERROR: Invalid widget id");
                    return;
                }

                t.saveToPreferences("text", inputString);
                t.updateWidget();
/*
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                updateOneWidget(context, appWidgetManager, widgetId, inputString);
*/
            }
        });
    }

}
