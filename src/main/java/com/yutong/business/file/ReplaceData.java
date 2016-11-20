package com.yutong.business.file;

public class ReplaceData {

    private String ori;

    private String dest;


    public ReplaceData(String ori, String dest) {
        this.ori = ori;
        this.dest = dest;
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
