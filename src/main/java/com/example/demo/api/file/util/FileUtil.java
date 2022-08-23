package com.example.demo.api.file.util;

import com.example.demo.common.util.StringUtils;

public class FileUtil {

    public static String extractFileId(String url) {
        int offset = url.lastIndexOf('/');
        if (offset < 0) {
            return null;
        }
        int offset2 = url.indexOf('.', offset + 2);
        if (offset2 < 0) {
            return StringUtils.emptyToNull(url.substring(offset + 1));
        }
        String fileId = url.substring(offset + 1, offset2);
        if (fileId.length() == 25 || fileId.length() == 40) {
            // legacy
            return null;
        }
        return fileId;
    }

}
