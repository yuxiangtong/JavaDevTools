package com.yutong.business.file;

public class ReplaceData {

    private String ori;

    private String dest;

    private boolean regex;


    public ReplaceData(String ori, String dest, boolean regex) {
        this.ori = ori;
        this.dest = dest;
        this.regex = regex;
    }


    public boolean getRegex() {
        return regex;
    }


    public void setRegex(boolean regex) {
        this.regex = regex;
    }


    public String getOri() {
        return ori;
    }


    public void setOri(String ori) {
        this.ori = ori;
    }


    public String getDest() {
        return dest;
    }


    public void setDest(String dest) {
        this.dest = dest;
    }

}
