package com.yutong.business.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 文件处理
 * 
 * @author yuxiangtong
 *
 */
public class FileHandle {

    public static int directoryCount = 0;

    public static int success_directoryModifyCount = 0;

    public static int error_directoryModifyCount = 0;

    public static int fileCount = 0;

    /* 目录是否参与修改 */
    public static boolean directoryModifiyFlag = true;

    /* 文件是否参与修改 */
    public static boolean fileModifiyFlag = true;

    public static int lineCount = 0;

    private ReplaceModle replaceModle;


    public FileHandle(ReplaceModle replaceModle) {
        this.replaceModle = replaceModle;
    }


    public void replaceHandle() throws Exception {
        File file = new File(replaceModle.getPathName());
        if (file.exists() == false) {
            return;
        }

        /*
         * 处理逻辑:如果当前文件是
         * 目录：则扫描目录下的所有文件;
         * 文件：则处理当前文件
         */
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                File subFile = files[i];
                replaceAll(subFile);
            }
        }
        else if (file.isFile()) {
            replaceAll(file);
        }
    }


    private void replaceAll(File file) throws Exception {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            directoryCount++;
            if (directoryModifiyFlag) {
                // 重命名
                File newFile = fileRenameByReplaceModelList(file);
                if (newFile != null) {
                    success_directoryModifyCount++;
                    file = newFile;
                }
                else {
                    error_directoryModifyCount++;
                }
            }
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                File subFile = files[i];
                replaceAll(subFile);
            }
        }
        else if (file.isFile()) {
            fileCount++;
            // 重命名文件名
            if (fileModifiyFlag) {
                File newFile = fileRenameByReplaceModelList(file);
                if (newFile != null) {
                    success_directoryModifyCount++;
                    file = newFile;
                }
                else {
                    error_directoryModifyCount++;
                }
            }

            // 2.替换支持的文件内容
            // 2.1.获取文件扩展名
            String fileName = file.getName();
            int lastPointIndex = file.getName().lastIndexOf(".");
            String extFileName = "";
            if (lastPointIndex != -1) {
                extFileName = fileName.substring(lastPointIndex);
            }
            // 2.2.判定 是否支持替换
            List<String> fileExtNameList = replaceModle.getFileExtNameList();
            if (existsListString(fileExtNameList, extFileName)) {
                String filePath = file.getAbsolutePath();
                String newFileExtNameBuf =
                        new SimpleDateFormat("yyyyMMddHHmmssSSS")
                                .format(new Date());

                String newFileExtName = "TT" + newFileExtNameBuf; // 新文件的扩展名

                String newFilePath = filePath + newFileExtName;

                boolean flag = readAndWriter(filePath, newFilePath);

                /* 如果此文件中含有要替换的信息，则进行重命名 */
                if (flag) {
                    if (file.delete()) {
                        fileRename(new File(newFilePath), newFileExtName, "");
                    }
                }
                else {
                    /* 没有匹配上时,需要把已创建的文件删除 */
                    boolean deleteFlag = new File(newFilePath).delete();
                    System.out.println(newFilePath + " del:" + deleteFlag);
                }
            }
        }
    }


    private boolean existsListString(List<String> list, String item) {
        boolean flag = false;
        if (CollectionUtils.isEmpty(list)) {
            return flag;
        }
        for (int i = 0; i < list.size(); i++) {
            String buffItem = list.get(i);
            if (StringUtils.equals(buffItem, item)) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    private boolean readAndWriter(String filePath, String newFilePath)
        throws Exception {
        boolean flag = false;

        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        String line = null;
        // new FileWriter(path + "t.txt", true) 这里加入true 可以不覆盖原有TXT文件内容 续写
        bufferedWriter = new BufferedWriter(new FileWriter(newFilePath, true));

        // 根据文件路径创建缓冲输入流
        bufferedReader = new BufferedReader(new FileReader(filePath));

        // 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
        int currFileCount = 0;
        while ((line = bufferedReader.readLine()) != null) {
            currFileCount++;
            lineCount++;
            System.out.println(lineCount);
            StringBuffer stringBuffer = new StringBuffer();
            List<ReplaceData> replaceDataList =
                    replaceModle.getReplaceDataList();
            for (ReplaceData replaceData : replaceDataList) {
                boolean regex = replaceData.getRegex();
                String oriString = replaceData.getOri();
                String destString = replaceData.getDest();
                if (regex) {
                    Pattern pattern = Pattern.compile(oriString);
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.matches() == false) {
                        continue;
                    }
                    line = matcher.replaceAll(destString);
                    flag = true;
                }
                else {
                    if (line.indexOf(oriString) == -1) {
                        continue;
                    }
                    line = StringUtils.replace(line, oriString, destString);
                    flag = true;
                }
            }
            stringBuffer.append(line);
            stringBuffer.append(System.getProperty("line.separator"));
            bufferedWriter.write(stringBuffer.toString());
            bufferedWriter.flush();
        }

        IOUtils.closeQuietly(bufferedReader);
        IOUtils.closeQuietly(bufferedWriter);

        return flag;
    }


    /**
     * 重命名文件目录或文件名称
     * 
     * @param file
     * @return 成功: 新的文件目录或文件对象<br/>
     *         失败: null
     */
    private File fileRenameByReplaceModelList(File file) {
        File newFile = null;
        // 重命名文件名称----start
        String oldFileParentPath = "";
        if (null != file.getParentFile()) {
            oldFileParentPath =
                    file.getParentFile().getAbsolutePath() + File.separator;
        }

        String oldFileName = file.getName();

        List<ReplaceData> replaceDataList = replaceModle.getReplaceDataList();
        for (ReplaceData replaceData : replaceDataList) {
            boolean regex = replaceData.getRegex();
            String oriString = replaceData.getOri();
            String destString = replaceData.getDest();
            if (regex) {
                oldFileName = oldFileName.replaceAll(replaceData.getOri(),
                        replaceData.getDest());
            }
            else {
                oldFileName =
                        StringUtils.replace(oldFileName, oriString, destString);
            }
        }
        newFile = new File(oldFileParentPath + oldFileName);
        boolean flag = file.renameTo(newFile);
        if (flag == false) {
            newFile = null;
        }
        return newFile;
    }


    /**
     * 重命名文件目录或文件名称
     * 
     * @param file
     * @return 成功: 新的文件目录或文件对象<br/>
     *         失败: null
     */
    private File fileRename(File file, String ori, String dest) {
        File newFile = null;
        // 重命名文件名称----start
        String oldFileParentPath = "";
        if (null != file.getParentFile()) {
            oldFileParentPath =
                    file.getParentFile().getAbsolutePath() + File.separator;
        }

        String oldFileName = file.getName();
        oldFileName = oldFileName.replace(ori, dest);

        newFile = new File(oldFileParentPath + oldFileName);
        boolean flag = file.renameTo(newFile);
        if (flag == false) {
            newFile = null;
        }
        return newFile;
    }


    public static void main(String[] args) {
        /* 文件路径或文件名称 */
        String pathName =
                "C:\\Users\\tech-winning\\Desktop\\test\\R002360\\R001250Action.java";

        /* 替换的文件扩展名集合 */
        List<String> fileExtNameList = new ArrayList<String>();
        fileExtNameList.add(".java");
        fileExtNameList.add(".xml");
        fileExtNameList.add(".vm");

        /* 替换数据信息集合 */
        List<ReplaceData> replaceDataList = new ArrayList<ReplaceData>();
        replaceDataList
                .add(new ReplaceData("// ActionHelper helper =", "", false));
        replaceDataList.add(new ReplaceData(
                "// WebContextUtil.getActionHelper(webctx.getRequest());", "",
                false));
        replaceDataList.add(new ReplaceData(
                "// helper.validateOfEffectiveAuthenticated(webctx.getRequest());",
                "", false));

        ReplaceModle replaceModle =
                new ReplaceModle(pathName, fileExtNameList, replaceDataList);
        FileHandle fileHandle = new FileHandle(replaceModle);

        try {
            fileHandle.replaceHandle();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
