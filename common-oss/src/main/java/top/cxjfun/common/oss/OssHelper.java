package top.cxjfun.common.oss;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.MD5;
import io.minio.*;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class OssHelper {

    protected static MinioClient minioClient;

    protected static OssConfigurationProperties properties;

    /**
     * 获取Oss Id
     *
     * @param stream
     * @return
     */
    public static String getOssId(InputStream stream) {
        return MD5.create().digestHex(stream);
    }


    /**
     * 创建桶
     *
     * @param bucketName 通名称
     */
    public static boolean createBucket(String bucketName) {
        Assert.notNull(bucketName, "Bucket Name param don't null");

        BucketExistsArgs bucket = BucketExistsArgs.builder().bucket(bucketName).build();
        try {
            if (!minioClient.bucketExists(bucket)) {
                MakeBucketArgs make = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(make);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 上传文件
     *
     * @param info 文件
     * @return
     */
    public static ObjectWriteResponse uploadFile(OssPutFile info) throws Exception {
        Assert.notNull(info, "OSS Put File is empty");

        //检查桶名
        if (ObjectUtil.isEmpty(info.getBucketName())) {
            info.setBucketName(properties.getBucket());
        }
        Assert.notNull(info.getBucketName(), "Bucket Name is undefined");

        InputStream inputStream = info.getInputStream();
        int available = inputStream.available();

//        if (inputStream == null || available <= 0) {
//            throw new FileNotFoundException(" File input stream is empty or input stream size is zeron");
//        }

        if (ObjectUtil.isEmpty(info.getOssId())) {
            info.setOssId(OssHelper.getOssId(inputStream));
        }

        // 判断存储桶是否存在
        createBucket(info.getBucketName());
        // 开始上传
        PutObjectArgs build = PutObjectArgs.builder()
                .bucket(info.getBucketName())
                .object(info.getOssId())
                .contentType(info.getContentType())
                .stream(inputStream, available, properties.getPartSize())
                .build();

        return minioClient.putObject(build);
    }

    /**
     * 下载文件
     *
     * @param ossId      object Id
     * @param bucketName 存储桶名
     * @return
     */
    public static InputStream downloadFile(String ossId, String bucketName) throws Exception {
        Assert.notNull(ossId, "OSS server Object id don't empty");
        Assert.notNull(bucketName, "OSS bucket name don't empty");

        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName).object(ossId).build();
        return minioClient.getObject(objectArgs);
    }
}
