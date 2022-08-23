package com.example.demo.common.resources;

import com.sunnysuperman.commons.util.FileUtil;

import java.io.IOException;

public class R {

    public static java.io.InputStream getStream(String path) {
        return R.class.getResourceAsStream("/" + path);
    }

    public static String getString(String path) {
        try {
            return FileUtil.read(getStream(path));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] getBytes(String path) {
        try {
            return FileUtil.readAsByteArray(getStream(path));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
