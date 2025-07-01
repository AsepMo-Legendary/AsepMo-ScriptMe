package com.scriptme.story.application;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.reminder.StoreRetrieveData;
import com.scriptme.story.engine.app.reminder.ToDoItem;
import com.scriptme.story.engine.app.settings.theme.Theme;
import com.scriptme.story.engine.widget.MaterialSpinner;
import com.scriptme.story.service.TodoNotificationService;

public class ApplicationReminderResult extends AppCompatActivity {
    public static final String TAG = "ApplicationReminderResult";
    
    private TextView mtoDoTextTextView;
    private Button mRemoveToDoButton;
    private MaterialSpinner mSnoozeSpinner;
    private String[] snoozeOptionsArray;
    private StoreRetrieveData storeRetrieveData;
    private ArrayList<ToDoItem> mToDoItems;
    private ToDoItem mItem;
    public static final String EXIT = "com.scriptme.story.exit";
    private TextView mSnoozeTextView;
    String theme;
    private void closeApp() {
        Intent i = new Intent(getApplication(), ApplicationReminder.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.putExtra(EXIT, true);
        SharedPreferences sharedPreferences = getSharedPreferences(ApplicationReminder.SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(EXIT, true);
        editor.apply();
        startActivity(i);

    }

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ScriptMe_Theme_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_reminder_result);
        storeRetrieveData = new StoreRetrieveData(getApplicationContext(), ApplicationReminder.FILENAME);
        mToDoItems = ApplicationReminder.getLocallyStoredData(storeRetrieveData);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.engine_app_reminder);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        UUID id = (UUID) i.getSerializableExtra(TodoNotificationService.TODOUUID);
        mItem = null;
        for (ToDoItem toDoItem : mToDoItems) {
            if (toDoItem.getIdentifier().equals(id)) {
                mItem = toDoItem;
                break;
            }
        }

        snoozeOptionsArray = getResources().getStringArray(R.array.engine_reminder_snooze_options);

        mRemoveToDoButton = (Button) findViewById(R.id.toDoReminderRemoveButton);
        mtoDoTextTextView = (TextView) findViewById(R.id.toDoReminderTextViewBody);
        mSnoozeTextView = (TextView) findViewById(R.id.reminderViewSnoozeTextView);
        mSnoozeSpinner = (MaterialSpinner) findViewById(R.id.todoReminderSnoozeSpinner);

//        mtoDoTextTextView.setBackgroundColor(item.getTodoColor());
        mtoDoTextTextView.setText(mItem.getToDoText());

        if (theme.equals(ApplicationReminder.LIGHTTHEME)) {
            mSnoozeTextView.setTextColor(getResources().getColor(R.color.engine_light_color_textSecondary));
        } else {
            mSnoozeTextView.setTextColor(Color.WHITE);
            mSnoozeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_alarm_snooze, 0, 0, 0
            );
        }

        mRemoveToDoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mToDoItems.remove(mItem);
                    changeOccurred();
                    saveData();
                    closeApp();
//                finish();
                }
            });


//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, snoozeOptionsArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_text_view, snoozeOptionsArray);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        mSnoozeSpinner.setAdapter(adapter);
//        mSnoozeSpinner.setSelection(0);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminder, menu);
        return true;
    }

    private void changeOccurred() {
        SharedPreferences sharedPreferences = getSharedPreferences(ApplicationReminder.SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ApplicationReminder.CHANGE_OCCURED, true);
//        editor.commit();
        editor.apply();
    }

    private Date addTimeToDate(int mins) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, mins);
        return calendar.getTime();
    }

    private int valueFromSpinner() {
        switch (mSnoozeSpinner.getSelectedItemPosition()) {
            case 0:
                return 10;
            case 1:
                return 30;
            case 2:
                return 60;
            default:
                return 0;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toDoReminderDoneMenuItem:
                Date date = addTimeToDate(valueFromSpinner());
                mItem.setToDoDate(date);
                mItem.setHasReminder(true);
                Log.d("OskarSchindler", "Date Changed to: " + date);
                changeOccurred();
                saveData();
                closeApp();
                //foo
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void saveData() {
        try {
            storeRetrieveData.saveToFile(mToDoItems);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
	
    
}
