package com.yutong.business.bjson.datamodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class BJsonResult {

    private SimpleIntegerProperty index;

    private SimpleStringProperty result;

    private SimpleIntegerProperty start;

    private SimpleIntegerProperty end;


    public BJsonResult(int index, String result, int start, int end) {
        this.index = new SimpleIntegerProperty(index);
        this.result = new SimpleStringProperty(result);
        this.start = new SimpleIntegerProperty(start);
        this.end = new SimpleIntegerProperty(end);
    }


    public int getIndex() {
        return index.get();
    }


    public void setIndex(SimpleIntegerProperty index) {
        this.index = index;
    }


    public String getResult() {
        return result.get();
    }


    public void setResult(SimpleStringProperty result) {
        this.result = result;
    }


    public int getStart() {
        return start.get();
    }


    public void setStart(SimpleIntegerProperty start) {
        this.start = start;
    }


    public int getEnd() {
        return end.get();
    }


    public void setEnd(SimpleIntegerProperty end) {
        this.end = end;
    }

}
