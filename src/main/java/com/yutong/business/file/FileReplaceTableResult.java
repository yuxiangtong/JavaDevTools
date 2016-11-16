package com.yutong.business.file;

import javafx.beans.property.SimpleStringProperty;


public class FileReplaceTableResult {

    private SimpleStringProperty ori;

    private SimpleStringProperty dest;


    public FileReplaceTableResult(String ori, String dest) {
        this.ori = new SimpleStringProperty(ori);
        this.dest = new SimpleStringProperty(dest);
    }


    public String getOri() {
        return ori.get();
    }


    public void setOri(String ori) {
        this.ori.set(ori);
    }


    public String getDest() {
        return dest.get();
    }


    public void setDest(String dest) {
        this.dest.set(dest);
    }

}
