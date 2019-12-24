package com.arcsoft.arcfacedemo.util;

import android.os.Environment;

import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SDCardPathManage {



    public static void init() {
        FileUtils.createOrExistsDir(getRootPathDir());
        FileUtils.createOrExistsDir(getRecognizeImageDir());
        FileUtils.createOrExistsDir(getBatchRegisterOriDir());
        FileUtils.createOrExistsDir(getLogDir());
        FileUtils.createOrExistsDir(getRgbLivenessImageDir());
        FileUtils.createOrExistsDir(getFqImageDir());
    }

    private static String getRootPathDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "senseTime";
    }

    public static String getRecognizeImageDir() {
        return getRootPathDir() + File.separator + "recognizeImage";
    }

    public static String getBatchRegisterOriDir() {
        return getRootPathDir() + File.separator + "registerImage";
    }

    public static String getRgbLivenessImageDir() {
        return getRootPathDir() + File.separator + "LivenessImage";
    }

    public static String getFqImageDir() {
        return getRootPathDir() + File.separator + "qualityImage";
    }


    public static String getLogDir() {
        return getRootPathDir() + File.separator + "log";
    }

    public static List<File> getFileList(File[] oriFile) {
        List<File> fileList = new ArrayList<>();
        for (File file : oriFile) {
            if (file.isDirectory()) {
                File[] childFileArr = file.listFiles();
                Collections.addAll(fileList, childFileArr);
            } else if (file.isFile()) {
                Collections.addAll(fileList, file);
            }
        }
        return fileList;
    }

    public static String getMethodTime() {
        return getLogDir() +
                File.separator +
                "log.txt";
    }

    public static String getRegisterLog() {
        return getLogDir() +
                File.separator +
                "registerLog.txt";
    }

    public static String getCompareLog() {
        return getLogDir() +
                File.separator +
                "compareLog.txt";
    }

    public static String getPerformanceLog() {
        return getLogDir() +
                File.separator +
                "performance.txt";
    }

    public static String getQualityLog() {
        return getLogDir() +
                File.separator +
                "qualityLog.txt";
    }

    public static String getAgeGenderLog() {
        return getLogDir() +
                File.separator +
                "AgeGender.txt";
    }

    public static String getIrLivenessLog() {
        return getLogDir() +
                File.separator +
                "IrLiveness.txt";
    }

    public static String getRgbLivenessLog() {
        return getLogDir() +
                File.separator +
                "Liveness.txt";
    }
}
