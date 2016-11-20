package com.yutong.business.file;

import java.util.ArrayList;
import java.util.List;


/**
 * 替换模型对象
 * 
 * @author yuxiangtong
 *
 */
public class ReplaceModle {

    private String pathName; // 文件路径或文件名称

    private List<String> fileExtNameList = new ArrayList<String>(); // 替换的文件扩展名集合

    private List<ReplaceData> replaceDataList = new ArrayList<ReplaceData>(); // 替换数据信息集合


    public ReplaceModle(String pathName, List<String> fileExtNameList,
        List<ReplaceData> replaceDataList) {
        this.pathName = pathName;
        this.fileExtNameList = fileExtNameList;
        this.replaceDataList = replaceDataList;
    }


    public String getPathName() {
        return pathName;
    }


    public void setPathName(String pathName) {
        this.pathName = pathName;
    }


    public List<String> getFileExtNameList() {
        return fileExtNameList;
    }


    public void setFileExtNameList(List<String> fileExtNameList) {
        this.fileExtNameList = fileExtNameList;
    }


    public List<ReplaceData> getReplaceDataList() {
        return replaceDataList;
    }


    public void setReplaceDataList(List<ReplaceData> replaceDataList) {
        this.replaceDataList = replaceDataList;
    }

}
