package com.jivarela.codebarscanner.utils;


import android.os.Environment;
import java.io.File;

public class StorageHelper {
    public static final String CODE_BAR_SCANNER = "CodeBarScanner";

    public static String getCodeBarScannerExternalDirectory() {
        File directory = new File(
                Environment.getExternalStorageDirectory().toString() +
                        File.separatorChar + CODE_BAR_SCANNER);

        if(!directory.exists())
            directory.mkdirs();

        return directory.getAbsolutePath();
    }
}
