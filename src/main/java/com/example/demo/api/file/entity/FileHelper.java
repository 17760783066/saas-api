package com.example.demo.api.file.entity;


import com.example.demo.common.util.StringUtils;

public class FileHelper {
    public static class UrlAndNamespace {
        private String url;
        private String namespace;

        public UrlAndNamespace(String url, String namespace) {
            super();
            this.url = url;
            this.namespace = namespace;
        }

        public String getUrl() {
            return url;
        }

        public String getNamespace() {
            return namespace;
        }

    }

    public static String extractPath(String url, String domain) {
        if (url == null) {
            return null;
        }
        int offset = url.indexOf(domain);
        if (offset >= 0) {
            return url.substring(offset + domain.length() + 1);
        }
        return url;
    }


    public static String extractFileId(String url) {
        if (url == null) {
            return null;
        }
        int paramOffset = url.indexOf('?');
        if (paramOffset > 0) {
            url = url.substring(0, paramOffset);
        }
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


    public static boolean isAudio(String url) {
        int index = url.lastIndexOf('.');
        if (index < 0) {
            return false;
        }
        String suffix = url.substring(index + 1).toLowerCase();
        return suffix.equals("mp3") || suffix.equals("m4a") || suffix.equals("amr");
    }
}
