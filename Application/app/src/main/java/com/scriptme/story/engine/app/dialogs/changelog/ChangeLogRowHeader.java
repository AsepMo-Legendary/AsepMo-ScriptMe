package com.scriptme.story.engine.app.dialogs.changelog;

public class ChangeLogRowHeader extends ChangeLogRow{

    //-----------------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------------

    public ChangeLogRowHeader(){
        super();
        setHeader(true);
        setBulletedList(false);
        setChangeTextTitle(null);
    }

    //-----------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("header="+super.header);
        sb.append(",");
        sb.append("versionName="+versionName);
        sb.append(",");
        sb.append("changeDate="+changeDate);

        return sb.toString();
    }

}
