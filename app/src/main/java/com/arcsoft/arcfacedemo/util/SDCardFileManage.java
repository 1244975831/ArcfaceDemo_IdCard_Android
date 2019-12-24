package com.arcsoft.arcfacedemo.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SDCardFileManage {

    private static final boolean isSaveFile = false;
    private static ArrayList<String> allFile = new ArrayList<>();
    private static volatile SDCardFileManage mInstance;
    private static final Object SINGLE_BYTE = new Object();

    private SDCardFileManage() {
    }

    public static SDCardFileManage getInstance() {
        if (mInstance == null) {
            synchronized (SINGLE_BYTE) {
                if (mInstance == null) {
                    mInstance = new SDCardFileManage();
                }
            }
        }
        return mInstance;
    }

    public void saveLog2(String logPath, String content) {
        saveLogThread(logPath, content);
    }

    /**
     * 开启线程保存log日志
     *
     * @param fileName
     * @param content
     */
    private synchronized void saveLogThread(String fileName, String content) {
        Disposable mLogDisposable = Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            saveLog(fileName, content);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer type) {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 保存日志通用方法
     *
     * @param fileName
     * @param content
     */
    private void saveLog(String fileName, String content) {
        FileWriter writer = null;
        try {
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new FileWriter(fileName, true);
            writer.write(content + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

//    /**
//     * 保存活体检测失败时的图片
//     */
//    public void saveFaceLivePic( byte[] nv21) {
////        String filePath = getFaceLivePicName(checkMode, alive, trackId, flTime);
//        String filePath = System.currentTimeMillis() + ".jpg";
//        File picFile = new File(SDCardPathManage.getLivenessDumpDirPath() + File.separator + filePath);
//        Disposable mPicDisposable = Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
//            try {
//                FileUtils.createOrExistsDir(picFile.getParentFile());
//                FileUtils.deleteFile(picFile);
//                FileOutputStream fos = new FileOutputStream(picFile.getAbsolutePath());
//                int previewWidth = SPUtils.getInstance().getInt("preview_width");
//                int previewHeight = SPUtils.getInstance().getInt("preview_height");
//                YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, 720, 720, null);
//                yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, fos);
//                fos.close();
//                emitter.onNext(1);
//                emitter.onComplete();
//            } catch (Exception e) {
//                emitter.onNext(0);
//                emitter.onComplete();
//                e.printStackTrace();
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribeWith(new DisposableObserver<Integer>() {
//                    @Override
//                    public void onNext(Integer type) {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
//    }


    public String getSocketStatus(String pathandname) {
        int end = pathandname.lastIndexOf("|");
        if (end != -1) {
            return pathandname.substring(0, end);
        } else {
            return null;
        }
    }

    public String getSocketMeg(String pathandname) {
        int end = pathandname.lastIndexOf("|");
        if (end != -1) {
            return pathandname.substring(end + 1, pathandname.length());
        } else {
            return null;
        }
    }

    public String getSocketPort(String pathandname) {
        int end = pathandname.lastIndexOf(":");
        if (end != -1) {
            return pathandname.substring(end + 1, pathandname.length());
        } else {
            return null;
        }
    }

    public String getFileName(String filename) {
        int start = filename.lastIndexOf("/");
        int end = filename.lastIndexOf(".");
        if (end != -1 && start != -1) {
            return filename.substring(start + 1, end);
        } else {
            return null;
        }
    }

    public String getDirName(String filename) {
        int start = filename.lastIndexOf("/");
        filename = filename.substring(0, start);
        start = filename.lastIndexOf("/");
        if (start != -1) {
            return filename.substring(start + 1);
        } else {
            return null;
        }
    }

    public String getStartTime(String filename) {
        filename = getFileName(filename);
        int start = filename.indexOf("_");
        int end = filename.lastIndexOf("_");
        return filename.substring(start + 1, end);
    }

    public String getEndTime(String filename) {
        filename = getFileName(filename);
        int start = filename.lastIndexOf("_");
        int end = filename.lastIndexOf("%");
        if (end == -1) {
            end = filename.length();
        }
        return filename.substring(start + 1, end);
    }

    public String getBackgroundId(String filename) {
        filename = getFileName(filename);
        int start = filename.indexOf("_");
        return filename.substring(0, start);
    }

    public String getBase64(String pathandname) {
        int start = 0;
        start = pathandname.lastIndexOf(",");
        if (start != 0) {
            return pathandname.substring(start + 1, pathandname.length());
        } else {
            return pathandname.substring(0, pathandname.length());
        }
    }

    public void clearAllFile() {
        allFile.clear();
    }

    public ArrayList<String> refreshFileList(String strPath) {
        String filename;
        String suf;
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        ArrayList<String> wechats = new ArrayList<>();
        if (files == null) {
            return null;
        }

        for (int i = 0; i < files.length; i++) {

            if (files[i].isDirectory()) {
                System.out.println("---" + files[i].getAbsolutePath());
                refreshFileList(files[i].getAbsolutePath());
            } else {
                filename = files[i].getName();
                int j = filename.lastIndexOf(".");
                suf = filename.substring(j + 1);

                //判断是不是msml后缀的文件
                if (suf.equalsIgnoreCase("jpg") || suf.equalsIgnoreCase("png") || suf.equalsIgnoreCase("jpeg") || suf.equalsIgnoreCase("bmp")) {
                    //对于文件才把它的路径加到filelist中
                    wechats.add(files[i].getAbsolutePath());
                    allFile.add(files[i].getAbsolutePath());
                }
            }

        }
        return allFile;
    }
}
