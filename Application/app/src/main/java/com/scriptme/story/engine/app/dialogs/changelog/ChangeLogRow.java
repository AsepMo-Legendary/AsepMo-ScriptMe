package com.scriptme.story.engine.app.dialogs.changelog;

public class ChangeLogRow {

   /**
    * Flag to indicate a header row
    */
   protected boolean header;

   /**
    *  This corresponds to the android:versionName attribute in your manifest file. It is a required data
    */
   protected String versionName;

   /**
    * Change data. It is optional
    */
   protected String changeDate;

   //-------------------------------------------------------------------------------------------------------------------

   /**
    *  Use a bulleted list. It overrides general flag. It is optional
    */
   private boolean bulletedList;

   /**
    *  Special marker in change text. It is optional
    *  TODO: not yet implemented
    */
   private String changeTextTitle;

   /**
    * Contains the actual text that will be displayed in your change log. It is required
    */
   private String changeText;

   //-------------------------------------------------------------------------------------------------------------------

    /**
     * Replace special tags [b] [i]
     *
     * @param changeLogText
     */
    public void parseChangeText(String changeLogText) {
        if (changeLogText!=null){
            changeLogText=changeLogText.replaceAll("\\[", "<").replaceAll("\\]",">");
        }
        setChangeText(changeLogText);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("header="+header);
        sb.append(",");
        sb.append("versionName="+versionName);
        sb.append(",");
        sb.append("bulletedList="+bulletedList);
        sb.append(",");
        sb.append("changeText="+changeText);
        //sb.append(",");
        //sb.append("changeTextTitle="+changeTextTitle);
        return sb.toString();
    }

    //-------------------------------------------------------------------------------------------------------------------


   public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public boolean isBulletedList() {
        return bulletedList;
    }

    public void setBulletedList(boolean bulletedList) {
        this.bulletedList = bulletedList;
    }


    public String getChangeText() {
        return changeText;
    }

    public void setChangeText(String changeText) {
        this.changeText = changeText;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getChangeTextTitle() {
        return changeTextTitle;
    }

    public void setChangeTextTitle(String changeTextTitle) {
        this.changeTextTitle = changeTextTitle;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }


}
