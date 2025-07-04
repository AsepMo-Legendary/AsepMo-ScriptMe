package com.scriptme.story.engine.app.dialogs.changelog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scriptme.story.R;
import com.scriptme.story.engine.app.dialogs.changelog.parser.XmlParser;

public class ChangeLogListView extends ListView implements AdapterView.OnItemClickListener {

    //--------------------------------------------------------------------------
    // Custom Attrs
    //--------------------------------------------------------------------------
    protected int mRowLayoutId = ChangeLog.mRowLayoutId;
    protected int mRowHeaderLayoutId = ChangeLog.mRowHeaderLayoutId;
    protected int mChangeLogFileResourceId = ChangeLog.mChangeLogFileResourceId;

    //--------------------------------------------------------------------------
    protected static String TAG="ChangeLogListView";
    // Adapter
    protected ChangeLogAdapter mAdapter;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public ChangeLogListView(Context context) {
        super(context);
        init(null, 0);
    }

    public ChangeLogListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public ChangeLogListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs,defStyle);
    }

    //--------------------------------------------------------------------------
    // Init
    //--------------------------------------------------------------------------

    /**
     * Initialize
     *
     * @param attrs
     * @param defStyle
     */
    protected void init(AttributeSet attrs, int defStyle){
        //Init attrs
        initAttrs(attrs,defStyle);
        //Init adapter
        initAdapter();

        //Set divider to 0dp
        setDividerHeight(0);
    }

    /**
     * Init custom attrs.
     *
     * @param attrs
     * @param defStyle
     */
    protected void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.ChangeLogListView, defStyle, defStyle);

        try {
            //Layout for rows and header
            mRowLayoutId = a.getResourceId(R.styleable.ChangeLogListView_rowLayoutId, mRowLayoutId);
            mRowHeaderLayoutId = a.getResourceId(R.styleable.ChangeLogListView_rowHeaderLayoutId, mRowHeaderLayoutId);

            //Changelog.xml file
            mChangeLogFileResourceId = a.getResourceId(R.styleable.ChangeLogListView_changeLogFileResourceId,mChangeLogFileResourceId);

            //String which is used in header row for Version
            //mStringVersionHeader= a.getResourceId(R.styleable.ChangeLogListView_StringVersionHeader,mStringVersionHeader);

        } finally {
            a.recycle();
        }
    }

    /**
     * Init adapter
     */
    protected void initAdapter() {

        try{
            //Read and parse changelog.xml
            XmlParser parse = new XmlParser(getContext(),mChangeLogFileResourceId);
            //ChangeLog chg=parse.readChangeLogFile();
            ChangeLog chg = new ChangeLog();

            if (chg!=null){
                //Create adapter and set custom attrs
                mAdapter = new ChangeLogAdapter(getContext(),chg.getRows());
                mAdapter.setmRowLayoutId(mRowLayoutId);
                mAdapter.setmRowHeaderLayoutId(mRowHeaderLayoutId);

                //Parse in a separate Thread to avoid UI block with large files
                new ParseAsyncTask(mAdapter,parse).execute();
                setAdapter(mAdapter);
            }else{
                setAdapter(null);
            }
        }catch (Exception e){
            Log.e(TAG,getResources().getString(R.string.engine_changelog_internal_error_parsing),e);
        }

    }

    /**
     * Async Task to parse xml file in a separate thread
     *
     */
    protected class ParseAsyncTask extends AsyncTask<Void, Void, ChangeLog>{

        private ChangeLogAdapter mAdapter;
        private XmlParser mParse;

        public ParseAsyncTask(ChangeLogAdapter adapter,XmlParser parse){
            mAdapter=adapter;
            mParse= parse;
        }

        @Override
        protected ChangeLog doInBackground(Void... params) {

            try{
                if (mParse!=null){
                    ChangeLog chg=mParse.readChangeLogFile();
                    return chg;
                }
            }catch (Exception e){
                Log.e(TAG,getResources().getString(R.string.engine_changelog_internal_error_parsing),e);
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void onPostExecute(ChangeLog chg) {

            //Notify data changed
            if (chg!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                    mAdapter.addAll(chg.getRows());
                }else{
                    if(chg.getRows()!=null){
                        for(ChangeLogRow row:chg.getRows()){
                            mAdapter.add(row);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * Sets the list's adapter, enforces the use of only a ChangeLogAdapter
     */
    public void setAdapter(ChangeLogAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO
    }


}
