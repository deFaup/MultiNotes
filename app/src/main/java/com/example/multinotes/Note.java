package com.example.multinotes;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable
{
    private String title_;
    private String lastUpdateTime_;
    private String text_;

    public Note(String title, String text){
        this.title_ = title;
        lastUpdateTime_ = new Date().toString();
        this.text_ = text;
    }

    public Note(String title, String text, String date){
        this.title_ = title;
        lastUpdateTime_ = date;
        this.text_ = text;
    }

    /* -------- Getters -------- */
    public String getTitle_() {
        return title_;
    }
    public String getLastUpdateTime_() {
        return lastUpdateTime_;
    }
    public String getText_() {
        return text_;
    }

    /* -------- Setters -------- */
    public void setTitle_(String title_) {
        this.title_ = title_;
    }
    public void setLastUpdateTime_(String lastUpdateTime_) {
        this.lastUpdateTime_ = lastUpdateTime_;
    }
    public void setText_(String text_) {
        this.text_ = text_;
    }

    /**********************************/
    public String getShortText(){
        if (text_.length()>80)
            return text_.substring(0,80)+"...";
        else return getText_();
    }
}
