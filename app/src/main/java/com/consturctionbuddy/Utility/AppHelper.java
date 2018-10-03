package com.consturctionbuddy.Utility;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AppHelper {


    public static byte[] readBytesFromFile(String aFilePath) {

        FileInputStream fio = null;
        byte[] bytesArray = null;

        try {

            File file = new File(aFilePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fio = new FileInputStream(file);
            fio.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fio != null) {
                try {
                    fio.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytesArray;
    }



}
