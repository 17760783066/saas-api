package com.example.demo.api.file.service;

import com.example.demo.api.file.entity.FileConfig;
import com.example.demo.api.file.entity.UploadOptions;
import com.example.demo.common.util.StringUtils;
import com.sunnysuperman.commons.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
public class ImgService {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileConfig fileConfig;

    private static final String FOLDER_TMP = "tmp-img";

    public String parseContent(String content) throws Exception {

        if (StringUtils.isEmpty(content)) {
            return null;
        }

        if (content.replaceAll("<.*?>", "").length() == content.length()) {
            return content;
        }

        Document doc = Jsoup.parse(content);

        int count = 0;
        Elements imgs = doc.select("img");
        for (Element img : imgs) {

            String src = img.attr("src");
            System.out.println(src);

            String minioUrl = upload(src);

            System.out.println(minioUrl);
            if (minioUrl != null) {
                img.attr("src", minioUrl);

                count++;
            }
        }

        if (count > 0) {
            return doc.body().html();
        } else {
            return content;
        }

    }

    public String upload(String src) throws Exception {

        String fileName = downLoadToTmp(src);
        System.out.println(fileName);

        if (fileName == null)
            return null;

        return fileService.upload(new File(fileName), new UploadOptions());
    }

    private String downLoadToTmp(String src) {
        try {

            if (!src.startsWith("http") || src.contains(fileConfig.getDomain())) {
                return null;
            }
            if (src.contains("?")) {
                src = src.split("\\?")[0];
            }

            String fileName = getFileName(src);
            if (fileName == null)
                return null;

            System.out.println(fileName);

            URL url = new URL(src);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            InputStream inputStream = conn.getInputStream();
            byte[] getData = readInputStream(inputStream);

            File saveDir = new File(fileConfig.getTmpDir() + File.separator + FOLDER_TMP);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            String fullName = saveDir + File.separator + fileName;
            File file = new File(fullName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            fos.close();
            if (inputStream != null) {
                inputStream.close();
            }

            return fullName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileName(String src) {
        try {
            StringBuilder sb = new StringBuilder();
            String ext = FileUtil.getFileExt(src);
            if (ext == null)
                return null;

            return sb.append(UUID.randomUUID()).append(".").append(ext).toString();

        } catch (Exception e) {
            return null;
        }
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


}
