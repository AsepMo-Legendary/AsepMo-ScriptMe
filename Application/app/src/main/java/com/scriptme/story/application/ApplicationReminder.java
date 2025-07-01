package com.scriptme.story.application;

import android.support.annotation.NonNull;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.reminder.StoreRetrieveData;
import com.scriptme.story.engine.app.reminder.ToDoItem;
import com.scriptme.story.engine.app.listeners.OnRecyclerScrollListener;
import com.scriptme.story.engine.app.settings.theme.Theme;
import com.scriptme.story.engine.graphics.calligraphy.CalligraphyContextWrapper;
import com.scriptme.story.engine.widget.RecyclerViewEmptySupport;
import com.scriptme.story.engine.widget.TextDrawable;
import com.scriptme.story.engine.widget.WidgetUtils;
import com.scriptme.story.engine.widget.helper.ItemTouchHelperClass;
import com.scriptme.story.service.TodoNotificationService;

public class ApplicationReminder extends AppCompatActivity {

    public static final String TAG = "ApplicationReminder";
    public static void start(Context c) {
        Intent mIntent = new Intent(c, ApplicationReminder.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(mIntent);
    }
    
    private Theme theme;
    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    private RecyclerViewEmptySupport mRecyclerView;
    private FloatingActionButton mAddToDoItemFAB;
    private ArrayList<ToDoItem> mToDoItemsArrayList;
    
    public static final String TODOITEM = "com.scriptme.story.application.MainActivity";
    private BasicListAdapter adapter;
    private static final int REQUEST_ID_TODO_ITEM = 100;
    private ToDoItem mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;
    public static final String DATE_TIME_FORMAT_12_HOUR = "MMM d, yyyy  h:mm a";
    public static final String DATE_TIME_FORMAT_24_HOUR = "MMM d, yyyy  k:mm";
    public static final String FILENAME = "todoitems.json";
    private StoreRetrieveData storeRetrieveData;
    public ItemTouchHelper itemTouchHelper;
    private OnRecyclerScrollListener mOnRecyclerScrollListener;
    public static final String SHARED_PREF_DATA_SET_CHANGED = "com.scriptme.story.datasetchanged";
    public static final String CHANGE_OCCURED = "com.scriptme.story.changeoccured";
    
    public static final String THEME_PREFERENCES = "com.scriptme.story.themepref";
    public static final String RECREATE_ACTIVITY = "com.scriptme.story.recreateactivity";
    public static final String EXTRA_RECREATE = "recreate";
    public static final String THEME_SAVED = "com.scriptme.story.savedtheme";
    public static final String DARKTHEME = "com.scriptme.story.darktheme";
    public static final String LIGHTTHEME = "com.scriptme.story.lighttheme";
    private String[] testStrings = {"Clean my room",
        "Water the plants",
        "Get car washed",
        "Get my dry cleaning"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme = Theme.with(this);
        theme.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_reminder);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mCoordinatorLayout.setBackgroundColor(theme.getBackgroundColor());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {     
            mToolbar.setPopupTheme(theme.getPopupToolbarStyle());
            mToolbar.setBackgroundColor(theme.getBackgroundColor());
            mToolbar.setTitle(getString(R.string.engine_app_reminder));
            Drawable home = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            if (home != null) {
                home.setColorFilter(theme.getIconColor(), PorterDuff.Mode.SRC_ATOP);
            }
            mToolbar.setNavigationIcon(home);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            setSupportActionBar(mToolbar);
        }   
        
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHANGE_OCCURED, false);
        editor.apply();

        storeRetrieveData = new StoreRetrieveData(getApplication(), FILENAME);
        mToDoItemsArrayList = getLocallyStoredData(storeRetrieveData);
        adapter = new BasicListAdapter(mToDoItemsArrayList);
        setAlarms();

        mAddToDoItemFAB = (FloatingActionButton) findViewById(R.id.addToDoItemFAB);
        mAddToDoItemFAB.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    Intent newTodo = new Intent(ApplicationReminder.this, ApplicationAddReminder.class);
                    ToDoItem item = new ToDoItem("", "", false, null);
                    int color = WidgetUtils.ColorGenerator.MATERIAL.getRandomColor();
                    item.setTodoColor(color);
                    //noinspection ResourceType
                    newTodo.putExtra(TODOITEM, item);
                    startActivityForResult(newTodo, REQUEST_ID_TODO_ITEM);
                }
            });


        mRecyclerView = (RecyclerViewEmptySupport) findViewById(R.id.toDoRecyclerView);
        mRecyclerView.setEmptyView(findViewById(R.id.toDoEmptyView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOnRecyclerScrollListener = new OnRecyclerScrollListener() {
            @Override
            public void show() {
                mAddToDoItemFAB.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAddToDoItemFAB.getLayoutParams();
                int fabMargin = lp.bottomMargin;
                mAddToDoItemFAB.animate().translationY(mAddToDoItemFAB.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
        };
        mRecyclerView.addOnScrollListener(mOnRecyclerScrollListener);

        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        mRecyclerView.setAdapter(adapter);
        
        theme.setStatusBarColor(theme.getBackgroundColor());
        theme.setNavBarColor(theme.getBackgroundColor());
        theme.setRecentApp(getString(R.string.engine_app_name));

        setUpTransitions();
    }

    public static ArrayList<ToDoItem> getLocallyStoredData(StoreRetrieveData storeRetrieveData) {
        ArrayList<ToDoItem> items = null;

        try {
            items = storeRetrieveData.loadFromFile();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (items == null) {
            items = new ArrayList<>();
        }
        return items;

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(ApplicationReminderResult.EXIT, false)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(ApplicationReminderResult.EXIT, false);
            editor.apply();
            finish();
        }
        /*
         We need to do this, as this activity's onCreate won't be called when coming back from SettingsActivity,
         thus our changes to dark/light mode won't take place, as the setContentView() is not called again.
         So, inside our SettingsFragment, whenever the checkbox's value is changed, in our shared preferences,
         we mark our recreate_activity key as true.

         Note: the recreate_key's value is changed to false before calling recreate(), or we woudl have ended up in an infinite loop,
         as onResume() will be called on recreation, which will again call recreate() and so on....
         and get an ANR

         */
        if (getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).getBoolean(RECREATE_ACTIVITY, false)) {
            SharedPreferences.Editor editor = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean(RECREATE_ACTIVITY, false);
            editor.apply();
            recreate();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(CHANGE_OCCURED, false)) {

            mToDoItemsArrayList = getLocallyStoredData(storeRetrieveData);
            adapter = new BasicListAdapter(mToDoItemsArrayList);
            mRecyclerView.setAdapter(adapter);
            setAlarms();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHANGE_OCCURED, false);
//            editor.commit();
            editor.apply();


        }
    }

    private void setAlarms() {
        if (mToDoItemsArrayList != null) {
            for (ToDoItem item : mToDoItemsArrayList) {
                if (item.hasReminder() && item.getToDoDate() != null) {
                    if (item.getToDoDate().before(new Date())) {
                        item.setToDoDate(null);
                        continue;
                    }
                    Intent i = new Intent(getApplication(), TodoNotificationService.class);
                    i.putExtra(TodoNotificationService.TODOUUID, item.getIdentifier());
                    i.putExtra(TodoNotificationService.TODOTEXT, item.getToDoText());
                    createAlarm(i, item.getIdentifier().hashCode(), item.getToDoDate().getTime());
                }
            }
        }
    }

    public void addThemeToSharedPreferences(String theme) {
        SharedPreferences sharedPreferences = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(THEME_SAVED, theme);
        editor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED && requestCode == REQUEST_ID_TODO_ITEM) {
            ToDoItem item = (ToDoItem) data.getSerializableExtra(TODOITEM);
            if (item.getToDoText().length() <= 0) {
                return;
            }
            boolean existed = false;

            if (item.hasReminder() && item.getToDoDate() != null) {
                Intent i = new Intent(getApplication(), TodoNotificationService.class);
                i.putExtra(TodoNotificationService.TODOTEXT, item.getToDoText());
                i.putExtra(TodoNotificationService.TODOUUID, item.getIdentifier());
                createAlarm(i, item.getIdentifier().hashCode(), item.getToDoDate().getTime());
                Log.d(TAG, "Alarm Created: " + item.getToDoText() + " at " + item.getToDoDate());
            }

            for (int i = 0; i < mToDoItemsArrayList.size(); i++) {
                if (item.getIdentifier().equals(mToDoItemsArrayList.get(i).getIdentifier())) {
                    mToDoItemsArrayList.set(i, item);
                    existed = true;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
            if (!existed) {
                addToDataStore(item);
            }
        }
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private boolean doesPendingIntentExist(Intent i, int requestCode) {
        PendingIntent pi = PendingIntent.getService(getApplication(), requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private void createAlarm(Intent i, int requestCode, long timeInMillis) {
        AlarmManager am = getAlarmManager();
        PendingIntent pi = PendingIntent.getService(getApplication(), requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
        Log.d(TAG, "createAlarm " + requestCode + " time: " + timeInMillis + " PI " + pi.toString());
    }

    private void deleteAlarm(Intent i, int requestCode) {
        if (doesPendingIntentExist(i, requestCode)) {
            PendingIntent pi = PendingIntent.getService(getApplication(), requestCode, i, PendingIntent.FLAG_NO_CREATE);
            pi.cancel();
            getAlarmManager().cancel(pi);
            Log.d("OskarSchindler", "PI Cancelled " + doesPendingIntentExist(i, requestCode));
        }
    }

    private void addToDataStore(ToDoItem item) {
        mToDoItemsArrayList.add(item);
        adapter.notifyItemInserted(mToDoItemsArrayList.size() - 1);

    }


    public void makeUpItems(ArrayList<ToDoItem> items, int len) {
        for (String testString : testStrings) {
            ToDoItem item = new ToDoItem(testString, testString, false, new Date());
            //noinspection ResourceType
//            item.setTodoColor(getResources().getString(R.color.red_secondary));
            items.add(item);
        }

    }

    public class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {
        
        private Theme theme;
        private ArrayList<ToDoItem> items;
        
        public BasicListAdapter(ArrayList<ToDoItem> items) {
            this.items = items;
            this.theme = new Theme();
        }
        
        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(items, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(items, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(final int position) {
            //Remove this line if not using Google Analytics

            mJustDeletedToDoItem = items.remove(position);
            mIndexOfDeletedToDoItem = position;
            Intent i = new Intent(getApplication(), TodoNotificationService.class);
            deleteAlarm(i, mJustDeletedToDoItem.getIdentifier().hashCode());
            notifyItemRemoved(position);

//            String toShow = (mJustDeletedToDoItem.getToDoText().length()>20)?mJustDeletedToDoItem.getToDoText().substring(0, 20)+"...":mJustDeletedToDoItem.getToDoText();
            String toShow = "Todo";
            Snackbar.make(mCoordinatorLayout, "Deleted " + toShow, Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Comment the line below if not using Google Analytics
                        items.add(mIndexOfDeletedToDoItem, mJustDeletedToDoItem);
                        if (mJustDeletedToDoItem.getToDoDate() != null && mJustDeletedToDoItem.hasReminder()) {
                            Intent i = new Intent(getApplication(), TodoNotificationService.class);
                            i.putExtra(TodoNotificationService.TODOTEXT, mJustDeletedToDoItem.getToDoText());
                            i.putExtra(TodoNotificationService.TODOUUID, mJustDeletedToDoItem.getIdentifier());
                            createAlarm(i, mJustDeletedToDoItem.getIdentifier().hashCode(), mJustDeletedToDoItem.getToDoDate().getTime());
                        }
                        notifyItemInserted(mIndexOfDeletedToDoItem);
                    }
                }).show();
        }

        @Override
        public BasicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final BasicListAdapter.ViewHolder holder, final int position) {
            ToDoItem item = items.get(position);   
            holder.linearLayout.setBackgroundColor(theme.getBackgroundRow());

            if (item.hasReminder() && item.getToDoDate() != null) {
                holder.mToDoTextview.setMaxLines(1);
                holder.mTimeTextView.setVisibility(View.VISIBLE);
            } else {
                holder.mTimeTextView.setVisibility(View.GONE);
                holder.mToDoTextview.setMaxLines(2);
            }
            holder.mToDoTextview.setText(item.getToDoText());
            holder.mToDoTextview.setTextColor(theme.getTextColor());

            Log.d(TAG, "Color: " + item.getTodoColor());
            TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(item.getToDoText().substring(0, 1), item.getTodoColor());
            holder.mColorImageView.setImageDrawable(myDrawable);
            if (item.getToDoDate() != null) {
                String timeToShow;
                if (android.text.format.DateFormat.is24HourFormat(ApplicationReminder.this)) {
                    timeToShow = ApplicationAddReminder.formatDate(DATE_TIME_FORMAT_24_HOUR, item.getToDoDate());
                } else {
                    timeToShow = ApplicationAddReminder.formatDate(DATE_TIME_FORMAT_12_HOUR, item.getToDoDate());
                }
                holder.mTimeTextView.setText(timeToShow);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder {

            View mView;
            LinearLayout linearLayout;
            TextView mToDoTextview;
            ImageView mColorImageView;
            TextView mTimeTextView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToDoItem item = items.get(ViewHolder.this.getAdapterPosition());
                            Intent i = new Intent(getApplication(), ApplicationAddReminder.class);
                            i.putExtra(TODOITEM, item);
                            startActivityForResult(i, REQUEST_ID_TODO_ITEM);
                        }
                    });
                mToDoTextview = (TextView) v.findViewById(R.id.toDoListItemTextview);
                mTimeTextView = (TextView) v.findViewById(R.id.todoListItemTimeTextView);
                mColorImageView = (ImageView) v.findViewById(R.id.toDoListItemColorImageView);
                linearLayout = (LinearLayout) v.findViewById(R.id.listItemLinearLayout);
            }
        }
    }

    //Used when using custom fonts

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void saveDate() {
        try {
            storeRetrieveData.saveToFile(mToDoItemsArrayList);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            storeRetrieveData.saveToFile(mToDoItemsArrayList);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(mOnRecyclerScrollListener);
    }


    public void setUpTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition enterT = new Slide(Gravity.RIGHT);
            enterT.setDuration(500);

            Transition exitT = new Slide(Gravity.LEFT);
            exitT.setDuration(300);
            Fade fade = new Fade();
            fade.setDuration(500);

            getWindow().setExitTransition(fade);
            getWindow().setReenterTransition(fade);
        }
    }

    /*@Override
     public boolean onCreateOptionsMenu(Menu menu)
     {
     // TODO: Implement this method
     getMenuInflater().inflate(R.menu.menu_main ,menu);
     return super.onCreateOptionsMenu(menu);
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item)
     {
     // TODO: Implement this method
     switch(item.getItemId()){
     case R.id.action_github:
     //String urlGithub = "https://github.com/ZRock-Application/ZRock_Engine/releases";
     //Shared.setLink(EngineActivity.this ,urlGithub);
     EditorActivity.start(ReminderActivity.this);
     break;
     case R.id.action_settings:
     SettingActivity.start(ReminderActivity.this);
     break;
     }
     return super.onOptionsItemSelected(item);
     }*/
}
