package com.termux.api;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.termux.api.util.TermuxApiLogger.info;


public class TextWidgetConfig extends Activity {
    public static String ACTION_WIDGET_CONFIGURE = "WIDGET_CONFIGURED";

    TextWidget t = null;
    EditText ed = null;
    Button save = null;
    TextView txt_help_gest = null;
    LinearLayout advanced_settings = null;
    TextView widgetId = null;
    CheckBox show_config = null;
    CheckBox parse_html = null;
    EditText bg_color = null;
    EditText fg_color = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_widget_config);

        widgetId = (TextView) findViewById(R.id.id_tv);
        ed = (EditText) findViewById(R.id.editText1);
        save = (Button) findViewById(R.id.button1);
        txt_help_gest = (TextView) findViewById(R.id.txt_help_gest);
        advanced_settings = (LinearLayout) findViewById(R.id.advanced_settings);

        // show advanced settings by default
        advanced_settings.setVisibility( View.VISIBLE );

        show_config = (CheckBox) findViewById(R.id.checkBox1);
        parse_html = (CheckBox) findViewById(R.id.checkBox2);
        bg_color = (EditText) findViewById(R.id.editText5);
        fg_color = (EditText) findViewById(R.id.editText6);


        getIdOfCurrentWidget(savedInstanceState);

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (show_config.isChecked()) {
                    // use booleans in the future :v
                    t.saveToPreferences("show_config", "yes");
                } else {
                    Log.d("myTAG", "disabling on click handler!");
                    // omg it hurts
                    t.saveToPreferences("show_config", "no");
                }
                if (parse_html.isChecked()) {
                    t.saveToPreferences("parse_html", "yes");
                } else {
                    t.saveToPreferences("parse_html", "no");
                }

                t.setBackgroundColor(bg_color.getText().toString());
                t.setForegroundColor(fg_color.getText().toString());

                if (true || ed.getText().toString().trim().length() > 0) {
                    t.saveToPreferences("text", ed.getText()
                            .toString().trim());

                    setResultDataToWidget(RESULT_OK);
                } else {
                    setResultDataToWidget(RESULT_CANCELED);
                }

                t.updateWidget();
            }
        });

    }

    /** Get the Id of Current Widget from the intent of the Widget
     *  and update fields according to saved preferences           **/
    void getIdOfCurrentWidget(Bundle savedInstanceState) {

        setResult(RESULT_CANCELED);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (t == null) {
                t = new TextWidget(this, extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID));
            }
            // Update fields according to preferences
            widgetId.setText("ID: " + t.id());

            // All of the below are already set to default, no need to change them on creation
            if (t.readPreference("text") != null) {
                save.setText("Update");
                ed.append(t.readPreference("text"));
                parse_html.setChecked(t.readPreference("parse_html").equals("yes"));
                bg_color.setText(t.readPreference("bg_color"));
                fg_color.setText(t.readPreference("fg_color"));
                // TODO: set all the remaining setting values
            }
        }

        // If they gave us an intent without the widget id, just bail.
        if (t.id() == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    static void updateOneWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String newText){
        TextWidget t = new TextWidget(context, appWidgetId);
        if(t == null){
            return;
        }
        t.saveToPreferences("text", newText);
        t.updateWidget();
    }

    static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager,
                                 int[] appWidgetIds){
        info("Updating all widgets!");
        for(int appWidgetId : appWidgetIds) {
            TextWidget t = new TextWidget(context, appWidgetId);
            if (t == null) {
                continue;
            }
            t.updateWidget();
        }
    }

    void setResultDataToWidget(int result) {
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, t.id());
        setResult(result, resultValue);
        finish();
    }

    public void toggle_contents(View v){
        advanced_settings.setVisibility( advanced_settings.isShown()
                ? View.GONE
                : View.VISIBLE );
        //txt_help_gest.setText("Show advanced settings");
    }
}