package com.scriptme.story.application;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.animation.Animator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.reminder.StoreRetrieveData;
import com.scriptme.story.engine.app.reminder.ToDoItem;
import com.scriptme.story.engine.app.settings.theme.Theme;
import com.scriptme.story.engine.graphics.calligraphy.CalligraphyContextWrapper;

public class ApplicationAddReminder extends AppCompatActivity {

    public static final String TAG = "ApplicationAddReminder";
    private Date mLastEdited;

    private EditText mToDoTextBodyEditText;
    private EditText mToDoTextBodyDescription;

    private SwitchCompat mToDoDateSwitch;
    //    private TextView mLastSeenTextView;
    private LinearLayout mUserDateSpinnerContainingLinearLayout;
    private TextView mReminderTextView;

    private String CombinationText;

    private EditText mDateEditText;
    private EditText mTimeEditText;
    private String mDefaultTimeOptions12H[];
    private String mDefaultTimeOptions24H[];

    private Button mChooseDateButton;
    private Button mChooseTimeButton;
    private Button mCopyClipboard;

    private ToDoItem mUserToDoItem;
    private FloatingActionButton mToDoSendFloatingActionButton;
    public static final String DATE_FORMAT = "MMM d, yyyy";
    public static final String DATE_FORMAT_MONTH_DAY = "MMM d";
    public static final String DATE_FORMAT_TIME = "H:m";

    private String mUserEnteredText;
    private String mUserEnteredDescription;
    private boolean mUserHasReminder;
    private Toolbar mToolbar;
    private Date mUserReminderDate;
    private int mUserColor;
    private boolean setDateButtonClickedOnce = false;
    private boolean setTimeButtonClickedOnce = false;
    private LinearLayout mContainerLayout;

    private ImageButton reminderIconImageButton;
    private TextView reminderRemindMeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ScriptMe_Theme_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_reminder_add_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.engine_app_reminder);
        setSupportActionBar(toolbar);

        //Show an X in place of <-
        final Drawable cross = getResources().getDrawable(R.drawable.ic_action_close_white);
        if (cross != null) {
            cross.setColorFilter(getResources().getColor(R.color.engine_light_color_icons), PorterDuff.Mode.SRC_ATOP);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(cross);
        }


        mUserToDoItem = (ToDoItem) getIntent().getSerializableExtra(ApplicationReminder.TODOITEM);

        mUserEnteredText = mUserToDoItem.getToDoText();
        mUserEnteredDescription = mUserToDoItem.getmToDoDescription();
        mUserHasReminder = mUserToDoItem.hasReminder();
        mUserReminderDate = mUserToDoItem.getToDoDate();
        mUserColor = mUserToDoItem.getTodoColor();

        reminderIconImageButton = (ImageButton) findViewById(R.id.userToDoReminderIconImageButton);
        reminderRemindMeTextView = (TextView) findViewById(R.id.userToDoRemindMeTextView);
        /*if (theme.equals(ReminderActivity.DARKTHEME)) {
         reminderIconImageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_alarm_add_white_24dp));
         reminderRemindMeTextView.setTextColor(Color.WHITE);
         }*/



        //Button for Copy to Clipboard
        mCopyClipboard = (Button) findViewById(R.id.copyclipboard);

        mContainerLayout = (LinearLayout) findViewById(R.id.todoReminderAndDateContainerLayout);
        mUserDateSpinnerContainingLinearLayout = (LinearLayout) findViewById(R.id.toDoEnterDateLinearLayout);
        mToDoTextBodyEditText = (EditText) findViewById(R.id.userToDoEditText);
        mToDoTextBodyDescription = (EditText) findViewById(R.id.userToDoDescription);
        mToDoDateSwitch = (SwitchCompat) findViewById(R.id.toDoHasDateSwitchCompat);
//        mLastSeenTextView = (TextView)findViewById(R.id.toDoLastEditedTextView);
        mToDoSendFloatingActionButton = (FloatingActionButton) findViewById(R.id.makeToDoFloatingActionButton);
        mReminderTextView = (TextView) findViewById(R.id.newToDoDateTimeReminderTextView);


        //OnClickListener for CopyClipboard Button
        mCopyClipboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String toDoTextContainer = mToDoTextBodyEditText.getText().toString();
                    String toDoTextBodyDescriptionContainer = mToDoTextBodyDescription.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    CombinationText = "Title : " + toDoTextContainer + "\nDescription : " + toDoTextBodyDescriptionContainer + "\n -Copied From MinimalToDo";
                    ClipData clip = ClipData.newPlainText("text", CombinationText);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(ApplicationAddReminder.this, "Copied To Clipboard!", Toast.LENGTH_SHORT).show();
                }
            });

        mContainerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(mToDoTextBodyEditText);
                    hideKeyboard(mToDoTextBodyDescription);
                }
            });


        if (mUserHasReminder && (mUserReminderDate != null)) {
//            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
            setReminderTextView();
            setEnterDateLayoutVisibleWithAnimations(true);
        }
        if (mUserReminderDate == null) {
            mToDoDateSwitch.setChecked(false);
            mReminderTextView.setVisibility(View.INVISIBLE);
        }

//        TextInputLayout til = (TextInputLayout)findViewById(R.id.toDoCustomTextInput);
//        til.requestFocus();
        mToDoTextBodyEditText.requestFocus();
        mToDoTextBodyEditText.setText(mUserEnteredText);
        mToDoTextBodyDescription.setText(mUserEnteredDescription);
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//        imm.showSoftInput(mToDoTextBodyEditText, InputMethodManager.SHOW_IMPLICIT);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        mToDoTextBodyEditText.setSelection(mToDoTextBodyEditText.length());


        mToDoTextBodyEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mUserEnteredText = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        mToDoTextBodyDescription.setText(mUserEnteredDescription);
        mToDoTextBodyDescription.setSelection(mToDoTextBodyDescription.length());
        mToDoTextBodyDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mUserEnteredDescription = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


//        String lastSeen = formatDate(DATE_FORMAT, mLastEdited);
//        mLastSeenTextView.setText(String.format(getResources().getString(R.string.last_edited), lastSeen));

        setEnterDateLayoutVisible(mToDoDateSwitch.isChecked());

        mToDoDateSwitch.setChecked(mUserHasReminder && (mUserReminderDate != null));
        mToDoDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        mUserReminderDate = null;
                    }
                    mUserHasReminder = isChecked;
                    setDateAndTimeEditText();
                    setEnterDateLayoutVisibleWithAnimations(isChecked);
                    hideKeyboard(mToDoTextBodyEditText);
                    hideKeyboard(mToDoTextBodyDescription);
                }
            });


        mToDoSendFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mToDoTextBodyEditText.length() <= 0) {
                        mToDoTextBodyEditText.setError(getString(R.string.engine_reminder_todo_error));
                    } else if (mUserReminderDate != null && mUserReminderDate.before(new Date())) {
                        makeResult(RESULT_CANCELED);
                    } else {
                        makeResult(RESULT_OK);
                        finish();
                    }
                    hideKeyboard(mToDoTextBodyEditText);
                    hideKeyboard(mToDoTextBodyDescription);
                }
            });


        mDateEditText = (EditText) findViewById(R.id.newTodoDateEditText);
        mTimeEditText = (EditText) findViewById(R.id.newTodoTimeEditText);

        mDateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Date date;
                    hideKeyboard(mToDoTextBodyEditText);
                    if (mUserToDoItem.getToDoDate() != null) {
//                    date = mUserToDoItem.getToDoDate();
                        date = mUserReminderDate;
                    } else {
                        date = new Date();
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(ApplicationAddReminder.this, new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                setDate(year, month, day);
                            }
                        }, year, month, day);          
                    datePickerDialog.show();

                }
            });


        mTimeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Date date;
                    hideKeyboard(mToDoTextBodyEditText);
                    if (mUserToDoItem.getToDoDate() != null) {
//                    date = mUserToDoItem.getToDoDate();
                        date = mUserReminderDate;
                    } else {
                        date = new Date();
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(ApplicationAddReminder.this, new TimePickerDialog.OnTimeSetListener(){
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                setTime(hour, minute);
                            }  
                        }, hour, minute, DateFormat.is24HourFormat(ApplicationAddReminder.this));           
                    timePickerDialog.show();
                }
            });

//        mDefaultTimeOptions12H = new String[]{"9:00 AM", "12:00 PM", "3:00 PM", "6:00 PM", "9:00 PM", "12:00 AM"};
//        mDefaultTimeOptions24H = new String[]{"9:00", "12:00", "15:00", "18:00", "21:00", "24:00"};
        setDateAndTimeEditText();

//

//        mChooseDateButton = (Button)findViewById(R.id.newToDoChooseDateButton);
//        mChooseTimeButton = (Button)findViewById(R.id.newToDoChooseTimeButton);
//
//        mChooseDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Date date;
//                hideKeyboard(mToDoTextBodyEditText);
//                if(mUserToDoItem.getToDoDate()!=null){
//                    date = mUserToDoItem.getToDoDate();
//                }
//                else{
//                    date = new Date();
//                }
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddToDoActivity.this, year, month, day);
//                if(theme.equals(MainActivity.DARKTHEME)){
//                    datePickerDialog.setThemeDark(true);
//                }
//                datePickerDialog.show(getFragmentManager(), "DateFragment");
//            }
//        });
//
//        mChooseTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Date date;
//                hideKeyboard(mToDoTextBodyEditText);
//                if(mUserToDoItem.getToDoDate()!=null){
//                    date = mUserToDoItem.getToDoDate();
//                }
//                else{
//                    date = new Date();
//                }
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddToDoActivity.this, hour, minute, DateFormat.is24HourFormat(AddToDoActivity.this));
//                if(theme.equals(MainActivity.DARKTHEME)){
//                    timePickerDialog.setThemeDark(true);
//                }
//                timePickerDialog.show(getFragmentManager(), "TimeFragment");
//            }
//        });
    }

    private void setDateAndTimeEditText() {

        if (mUserToDoItem.hasReminder() && mUserReminderDate != null) {
            String userDate = formatDate("d MMM, yyyy", mUserReminderDate);
            String formatToUse;
            if (DateFormat.is24HourFormat(ApplicationAddReminder.this)) {
                formatToUse = "k:mm";
            } else {
                formatToUse = "h:mm a";

            }
            String userTime = formatDate(formatToUse, mUserReminderDate);
            mTimeEditText.setText(userTime);
            mDateEditText.setText(userDate);

        } else {
            mDateEditText.setText(getString(R.string.engine_reminder_date_default));
//            mUserReminderDate = new Date();
            boolean time24 = DateFormat.is24HourFormat(ApplicationAddReminder.this);
            Calendar cal = Calendar.getInstance();
            if (time24) {
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
            } else {
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 1);
            }
            cal.set(Calendar.MINUTE, 0);
            mUserReminderDate = cal.getTime();
            Log.d("OskarSchindler", "Imagined Date: " + mUserReminderDate);
            String timeString;
            if (time24) {
                timeString = formatDate("k:mm", mUserReminderDate);
            } else {
                timeString = formatDate("h:mm a", mUserReminderDate);
            }
            mTimeEditText.setText(timeString);
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            if(hour<9){
//                timeOption = time24?mDefaultTimeOptions24H[0]:mDefaultTimeOptions12H[0];
//            }
//            else if(hour < 12){
//                timeOption = time24?mDefaultTimeOptions24H[1]:mDefaultTimeOptions12H[1];
//            }
//            else if(hour < 15){
//                timeOption = time24?mDefaultTimeOptions24H[2]:mDefaultTimeOptions12H[2];
//            }
//            else if(hour < 18){
//                timeOption = time24?mDefaultTimeOptions24H[3]:mDefaultTimeOptions12H[3];
//            }
//            else if(hour < 21){
//                timeOption = time24?mDefaultTimeOptions24H[4]:mDefaultTimeOptions12H[4];
//            }
//            else{
//                timeOption = time24?mDefaultTimeOptions24H[5]:mDefaultTimeOptions12H[5];
//            }
//            mTimeEditText.setText(timeOption);
        }
    }

    private String getThemeSet() {
        return getSharedPreferences(ApplicationReminder.THEME_PREFERENCES, MODE_PRIVATE).getString(ApplicationReminder.THEME_SAVED, ApplicationReminder.LIGHTTHEME);
    }

    public void hideKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }


    public void setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
//        int currentYear = calendar.get(Calendar.YEAR);
//        int currentMonth = calendar.get(Calendar.MONTH);
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if (reminderCalendar.before(calendar)) {
            //    Toast.makeText(this, "My time-machine is a bit rusty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }

        if (DateFormat.is24HourFormat(ApplicationAddReminder.this)) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        } else {

            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        mUserReminderDate = calendar.getTime();
        setReminderTextView();
//        setDateAndTimeEditText();
        setDateEditText();
    }

    public void setTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }

//        if(DateFormat.is24HourFormat(this) && hour == 0){
//            //done for 24h time
//                hour = 24;
//        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("OskarSchindler", "Time set: " + hour);
        calendar.set(year, month, day, hour, minute, 0);
        mUserReminderDate = calendar.getTime();

        setReminderTextView();
//        setDateAndTimeEditText();
        setTimeEditText();
    }

    public void setDateEditText() {
        String dateFormat = "d MMM, yyyy";
        mDateEditText.setText(formatDate(dateFormat, mUserReminderDate));
    }

    public void setTimeEditText() {
        String dateFormat;
        if (DateFormat.is24HourFormat(ApplicationAddReminder.this)) {
            dateFormat = "k:mm";
        } else {
            dateFormat = "h:mm a";

        }
        mTimeEditText.setText(formatDate(dateFormat, mUserReminderDate));
    }

    public void setReminderTextView() {
        if (mUserReminderDate != null) {
            mReminderTextView.setVisibility(View.VISIBLE);
            if (mUserReminderDate.before(new Date())) {
                Log.d("OskarSchindler", "DATE is " + mUserReminderDate);
                mReminderTextView.setText(getString(R.string.engine_reminder_date_error_check_again));
                mReminderTextView.setTextColor(Color.RED);
                return;
            }
            Date date = mUserReminderDate;
            String dateString = formatDate("d MMM, yyyy", date);
            String timeString;
            String amPmString = "";

            if (DateFormat.is24HourFormat(ApplicationAddReminder.this)) {
                timeString = formatDate("k:mm", date);
            } else {
                timeString = formatDate("h:mm", date);
                amPmString = formatDate("a", date);
            }
            String finalString = String.format(getResources().getString(R.string.engine_reminder_remind_date_and_time), dateString, timeString, amPmString);
            mReminderTextView.setTextColor(getResources().getColor(R.color.engine_light_color_textSecondary));
            mReminderTextView.setText(finalString);
        } else {
            mReminderTextView.setVisibility(View.INVISIBLE);

        }
    }

    public void makeResult(int result) {
        Log.d(TAG, "makeResult - ok : in");
        Intent i = new Intent();
        if (mUserEnteredText.length() > 0) {

            String capitalizedString = Character.toUpperCase(mUserEnteredText.charAt(0)) + mUserEnteredText.substring(1);
            mUserToDoItem.setToDoText(capitalizedString);
            Log.d(TAG, "Description: " + mUserEnteredDescription);
            mUserToDoItem.setmToDoDescription(mUserEnteredDescription);
        } else {
            mUserToDoItem.setToDoText(mUserEnteredText);
            Log.d(TAG, "Description: " + mUserEnteredDescription);
            mUserToDoItem.setmToDoDescription(mUserEnteredDescription);
        }
//        mUserToDoItem.setLastEdited(mLastEdited);
        if (mUserReminderDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mUserReminderDate);
            calendar.set(Calendar.SECOND, 0);
            mUserReminderDate = calendar.getTime();
        }
        mUserToDoItem.setHasReminder(mUserHasReminder);
        mUserToDoItem.setToDoDate(mUserReminderDate);
        mUserToDoItem.setTodoColor(mUserColor);
        i.putExtra(ApplicationReminder.TODOITEM, mUserToDoItem);
        setResult(result, i);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(ApplicationAddReminder.this) != null) {
                    makeResult(RESULT_CANCELED);
                    NavUtils.navigateUpFromSameTask(ApplicationAddReminder.this);
                }
                hideKeyboard(mToDoTextBodyEditText);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String formatDate(String formatString, Date dateToFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }

    public void setEnterDateLayoutVisible(boolean checked) {
        if (checked) {
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setEnterDateLayoutVisibleWithAnimations(boolean checked) {
        if (checked) {
            setReminderTextView();
            mUserDateSpinnerContainingLinearLayout.animate().alpha(1.0f).setDuration(500).setListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }
            );
        } else {
            mUserDateSpinnerContainingLinearLayout.animate().alpha(0.0f).setDuration(500).setListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }
            );
        }

    }
}
