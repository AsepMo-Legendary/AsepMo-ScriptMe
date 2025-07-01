package com.scriptme.story.engine.app.dialogs.changelog.parser;

import android.content.Context;

import com.scriptme.story.engine.app.dialogs.changelog.ChangeLog;

public abstract class BaseParser {

    /**
     *  Context
     */
    protected Context mContext;

    /**
     * Use a bulleted List
     */
    protected boolean bulletedList;

    //--------------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------------

    /**
     * Create a new instance for a context.
     *
     * @param context  current Context
     */
    public BaseParser(Context context){
        this.mContext=context;
    }

    //--------------------------------------------------------------------------------

    /**
     * Read and Parse the changeLog file and return a new {@link ChangeLog}.
     *
     * @return The content of changelog file
     *
     * @throws Exception
     */
    public abstract ChangeLog readChangeLogFile() throws Exception;

}
