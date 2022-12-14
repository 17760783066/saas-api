package com.example.demo.api.file.service;

import com.example.demo.api.admin.authority.AdminContexts;
import com.example.demo.api.admin.model.Admin;
import com.example.demo.api.admin.repository.AdminRepository;
import com.example.demo.api.file.entity.AliUploadToken;
import com.example.demo.api.file.entity.OSSConfig;
import com.example.demo.api.file.entity.UploadOptions;
import com.example.demo.api.file.entity.UploadToken;
import com.example.demo.api.ui.entity.WaterMarkUtil;
import com.example.demo.common.L;
import com.example.demo.common.controller.auth.AdminType;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.common.model.ErrorCode;
import com.example.demo.common.resources.R;
import com.example.demo.common.util.CollectionUtils;
import com.example.demo.common.util.SimpleHttpClient;
import com.example.demo.common.util.StringUtils;
import com.example.demo.common.util.URLUtils;
import com.example.demo.common.util.UUIDCreatorFactory;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.sunnysuperman.commons.util.FileUtil;
import com.sunnysuperman.commons.util.JSONUtil;
import com.sunnysuperman.commons.util.PlaceholderUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

@Service
public class FileService implements ErrorCode {
    private static final int MAX_UPLOAD_SIZE = 15 * 1024 * 1024;
    private static final int MAX_UPLOAD_SIZE_VIDEO = 2000 * 1024 * 1024;
    private static final String STS_API_VERSION = "2015-04-01";
    private static final UUIDCreatorFactory.UUIDCreator TMP_FILE_ID_CREATOR = UUIDCreatorFactory.get();
    private String POLICY_TEMPLATE;
    @Autowired
    private OSSConfig ossConfig;
    private OSS ossClient;
    private UUIDCreatorFactory.UUIDCreator ossFileNameCreator = UUIDCreatorFactory.get();
    @Autowired
    private AdminRepository adminRepository;
    private static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn,
                                                 String roleSessionName, String policy, int expireSeconds) throws Exception {
        // ???????????? Aliyun Acs Client, ???????????? OpenAPI ??????
        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        // ???????????? AssumeRoleRequest ?????????????????????
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setVersion(STS_API_VERSION);
        request.setMethod(MethodType.POST);
        request.setProtocol(ProtocolType.HTTPS);
        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);
        request.setPolicy(policy);
        request.setDurationSeconds((long) expireSeconds);
        // ????????????????????????response
        return client.getAcsResponse(request);
    }

    @PostConstruct
    public void init() throws Exception {
        POLICY_TEMPLATE = FileUtil.read(R.getStream("file/file_ali_policy.json"));
        POLICY_TEMPLATE = JSONUtil.toJSONString(JSONUtil.parse(POLICY_TEMPLATE));
        ossClient = new OSSClientBuilder().build(ossConfig.getInternalEndpoint(), ossConfig.getKey(), ossConfig.getSecret());
    }

    public String generateObjectKey(String namespace, String fileName, int randomLength) {
        return generateObjectKey(namespace, fileName, null, randomLength);
    }

    public String generateObjectKey(String namespace, String fileName, String ext, int randomLength) {
        if (StringUtils.isEmpty(namespace)) {
            namespace = "f";
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        StringBuilder buf = new StringBuilder();
        if (ossConfig.getNamespace() != null) {
            buf.append(ossConfig.getNamespace()).append('/');
        }
        buf.append(namespace).append('/').append(year).append('/').append(month).append('/').append(day).append('/')
                .append(ossFileNameCreator.create());
        if (randomLength > 0) {
            buf.append(StringUtils.randomString(StringUtils.LOWERCASE_ALPHA_NUMERIC, randomLength));
        }
        if (StringUtils.isEmpty(ext)) {
            ext = FileUtil.getFileExt(fileName);
        }
        if (ext != null) {
            buf.append('.').append(ext);
        }
        return buf.toString();
    }

    public String upload(File file, UploadOptions options) throws Exception {
        ObjectMetadata metadata = new ObjectMetadata();
        String contentType = options.getContentType();
        if (contentType == null) {
            contentType = Files.probeContentType(file.toPath());
        }
        if (contentType != null) {
            metadata.setContentType(contentType);
        }
        if (options.getPermission() != null) {
            switch (options.getPermission()) {
                case PRIVATE:
                    metadata.setObjectAcl(CannedAccessControlList.Private);
                    break;
                case PUBLIC_READ:
                    metadata.setObjectAcl(CannedAccessControlList.PublicReadWrite);
                    break;
                default:
                    break;
            }
        }
        String fileName = options.getFileName() != null ? options.getFileName() : file.getName();
        String objectKey = generateObjectKey(options.getNamespace(), fileName, options.getRandomLength());
        ossClient.putObject(ossConfig.getBucket(), objectKey, file, metadata);
        return new StringBuilder("https://").append(ossConfig.getCanonicalDomain()).append("/").append(objectKey)
                .toString();
    }

    public UploadToken uploadToken(String namespace, String fileName, int fileSize, boolean cdn) throws Exception {
        int max = MAX_UPLOAD_SIZE;
        if (namespace != null && namespace.equals("video")) {
            max = MAX_UPLOAD_SIZE_VIDEO;
        }
        if (fileSize > max) {
            throw new ServiceException(ERR_FILE_SIZE);
        }
        String objectKey = generateObjectKey(namespace, fileName, 8);
        String policy;
        {
            Map<String, Object> context = new HashMap<>(2);
            context.put("bucket", ossConfig.getBucket());
            context.put("objectKey", objectKey);
            policy = PlaceholderUtil.compile(POLICY_TEMPLATE, context);
        }
        AssumeRoleResponse response = assumeRole(ossConfig.getKey(), ossConfig.getSecret(), ossConfig.getPutArn(),
                "adm", policy, 1800);
        AliUploadToken token = new AliUploadToken();
        token.setVendor("ali");
        token.setAccessKey(response.getCredentials().getAccessKeyId());
        token.setAccessSecret(response.getCredentials().getAccessKeySecret());
        token.setStsToken(response.getCredentials().getSecurityToken());
        token.setRegion(ossConfig.getRegion());
        token.setEndpoint("https://oss-" + ossConfig.getRegion() + ".aliyuncs.com");

        token.setBucket(ossConfig.getBucket());
        token.setKey(objectKey);
        token.setUrl("https://" + (cdn ? ossConfig.getCdnDomain() : ossConfig.getCanonicalDomain()) + "/" + objectKey);
        return token;
    }

    public String makeSignedUrl(String objectKey, int expireSeconds, boolean cdn) throws Exception {
        Date expiration = new Date(System.currentTimeMillis() + 1000L * expireSeconds);
        String signedUrl = ossClient.generatePresignedUrl(ossConfig.getBucket(), objectKey, expiration).toString();
        String finalUrl = URLUtils.replaceDomain(signedUrl,
                (cdn ? ossConfig.getCdnDomain() : ossConfig.getCanonicalDomain()));
        return finalUrl;
    }

    public void delete(File file) {
        if (file == null) {
            return;
        }
        try {
            file.delete();
        } catch (Exception e) {
            L.error(e);
        }
    }

    public String md5(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            return DigestUtils.md5Hex(in);
        } finally {
            FileUtil.close(in);
        }
    }

    public File createTmpFile(String namespace, String fileName, String extension) throws Exception {
        File dir = new File("/tmp/hanlin-api", namespace);
        dir.mkdirs();
        if (StringUtils.isNull(fileName)) {
            fileName = TMP_FILE_ID_CREATOR.create();
        }
        if (extension != null) {
            fileName += "." + extension;
        }
        File file = new File(dir, fileName);
        return file;
    }

    public File createTmpFile(String namespace, String extension) throws Exception {
        return createTmpFile(namespace, null, extension);
    }

    /***
     * ????????????
     * @param url
     * @param file
     */
    public void download(String url, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            new SimpleHttpClient().download(url, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????????
     *
     * @param folderPath
     * @param folderName
     * @return
     */
    public String createFolder(String folderPath, String folderName) {
        File file = new File(folderPath + File.separator + folderName);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getPath();
    }

    public File createFile(String filePath, String fileName) {
        File file = null;
        if (!StringUtils.isNull(filePath) && !StringUtils.isNull(fileName)) {
            file = new File(filePath + File.separator + fileName);
        } else {
            throw new ServiceException(ErrorCode.ERR_ILLEGAL_ARGUMENT);
        }
        return file;
    }


    /**
     * ??????????????????
     *
     * @param file
     */
    public int getFileSize(File file) {
        int length = 0;
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            length = (int) file.length();
            System.out.println("??????" + fileName + "???????????????" + length);
            return length;
        }
        return length;
    }

    /**
     * java??????????????????
     *
     * @param properties
     * @param folder
     * @param fileName
     * @param separator
     */
    public void obgectToFile(Properties properties, String folder, String fileName, String separator) {
        PrintWriter printWriter = null;
        try {
            // ???????????????????????????????????????
            printWriter = new PrintWriter(new FileWriter(createFile(folder, fileName)), true);
            // ??????properties????????????key
            Set<Object> keys = properties.keySet();
            // ???????????????key
            for (Object object : keys) {
                // ???object?????????String
                String key = (String) object;
                // ??????key????????????????????????
                String value = properties.getProperty(key);
                // key????????????????????????
                printWriter.println(key + separator + value);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }
    private Admin findByAdminId(Integer id) {
        Admin admin = adminRepository.findById(id).orElse(null);
        
        return admin;
    }
    
    public Map uploadWithWaterMark(MultipartFile file, AdminType adminType) throws Exception {
        if (file == null) {
            throw new ServiceException(ERR_DATA_NOT_FOUND);
        }
        // if (file.getSize() > MAX_UPLOAD_SIZE) {
        // throw new ServiceException(ERR_FILE_SIZE);
        // }

        String remark = null;

        if (adminType == AdminType.ADMIN) {
            Integer id = AdminContexts.requestAdminId();
            Admin admin = findByAdminId(id);
            remark = admin.getMobile();
        }

        File tmpFile = createTmpFile("img-watermark", FileUtil.getFileExt(file.getOriginalFilename()));
        FileUtil.copy(file.getInputStream(), new FileOutputStream(tmpFile));
        WaterMarkUtil.pressImage(tmpFile, remark);

        UploadOptions options = new UploadOptions();
        options.setContentType("application/x-jpg");
        String url = upload(tmpFile, options);

        try {
            FileUtil.delete(tmpFile);
        } catch (Exception e) {

        }

        return CollectionUtils.arrayAsMap("url", url);
    }

}
