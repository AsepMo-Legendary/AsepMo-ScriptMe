package com.scriptme.story.engine.app.dialogs.changelog;

import java.util.LinkedList;

import com.scriptme.story.R;

public class ChangeLog {
	public static final boolean FOR_AMAZON = false;
	  /**
     *  Resource id for changelog.xml file.
     *
     *  You shouldn't modify this value.
     *  You can use changeLogResourceId attribute in ChangeLogListView
     **/
    public static final int mChangeLogFileResourceId = R.raw.changelog;

    /**
     *  Layout resource id for changelog item rows.
     *
     *  You shouldn't modify this value.
     *  You can use rowLayoutId attribute in ChangeLogListView
     **/
    public static final int mRowLayoutId = R.layout.changelogrow_layout;

    /**
     * Layout resource id for changelog header rows.
     *
     *  You shouldn't modify this value.
     *  You can use rowHeaderLayoutId attribute in ChangeLogListView
     **/
    public static final int mRowHeaderLayoutId = R.layout.changelogrowheader_layout;


    /**
     *  String resource id for text Version in header row.
     *
     * You shouldn't modify this value.
     *  You can use changelog_header_version in strings.xml
     */
    public static final int mStringVersionHeader = R.string.engine_changelog_header_version;

    /**
     * All changelog rows
     */
    private LinkedList<ChangeLogRow> rows;

    /**
     * Use a bulleted List
     */
    private boolean bulletedList;

    //-----------------------------------------------------------------------

    public ChangeLog(){
        rows=new LinkedList<ChangeLogRow>();
    }

    /**
     * Add new {@link ChangeLogRow} to rows
     *
     * @param row
     */
    public void addRow(ChangeLogRow row){
        if (row!=null){
            if (rows==null) rows=new LinkedList<ChangeLogRow>();
            rows.add(row);
        }
    }

    /**
     * Clear all rows
     */
    public void clearAllRows(){
        rows=new LinkedList<ChangeLogRow>();
    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("bulletedList="+bulletedList);
        sb.append("\n");
        if (rows!=null){
            for (ChangeLogRow row:rows){
                sb.append("row=[");
                sb.append(row.toString());
                sb.append("]\n");
            }
        }else{
            sb.append("rows:none");
        }
        return sb.toString();
    }

    //-----------------------------------------------------------------------

    public boolean isBulletedList() {
        return bulletedList;
    }

    public void setBulletedList(boolean bulletedList) {
        this.bulletedList = bulletedList;
    }

    public LinkedList<ChangeLogRow> getRows() {
        return rows;
    }

    public void setRows(LinkedList<ChangeLogRow> rows) {
        this.rows = rows;
    }




}
