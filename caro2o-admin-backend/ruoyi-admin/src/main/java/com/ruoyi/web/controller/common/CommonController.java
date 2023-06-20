package com.ruoyi.web.controller.common;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.common.config.MiniIoConfig;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MiniIoConfig miniIoConfig;


    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }


    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload1")
    public AjaxResult uploadFile1(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isNotEmpty(fileName)) {
            String fileTyle = fileName.substring(fileName.lastIndexOf("."));
            if (StringUtils.isNotEmpty(fileTyle)) {
                fileName = String.valueOf(System.currentTimeMillis()).concat(IdUtils.simpleUUID()).concat(fileTyle);
            } else {
                fileName = String.valueOf(System.currentTimeMillis()).concat(IdUtils.simpleUUID());
            }

        }
        // 1：创建minio客户端连接对象
        // 2: 检查捅是否存在, 桶不存在上传不了
        boolean ok = minioClient.bucketExists(BucketExistsArgs.builder().bucket(MiniIoConfig.getBucket()).build());
        if (!ok) {
            return AjaxResult.error("文件服务配置异常，联系管理员");
        }
        // 3:上传文件
        minioClient.putObject(PutObjectArgs.builder().bucket(MiniIoConfig.getBucket()).object(fileName).stream(file.getInputStream(), file.getInputStream().available(), -1).build());
        // 4：拼接图片访问连接
        String url = MiniIoConfig.getEndpoint() + "/" + MiniIoConfig.getBucket() + "/" + fileName;
        AjaxResult ajax = AjaxResult.success();
        ajax.put("url", url);
        return ajax;
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        String url = serverConfig.getUrl() + fileName;
        AjaxResult ajax = AjaxResult.success();
        ajax.put("url", url);
        ajax.put("fileName", fileName);
        ajax.put("newFileName", FileUtils.getName(fileName));
        ajax.put("originalFilename", file.getOriginalFilename());
        return ajax;

    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception {

        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath();
        List<String> urls = new ArrayList<String>();
        List<String> fileNames = new ArrayList<String>();
        List<String> newFileNames = new ArrayList<String>();
        List<String> originalFilenames = new ArrayList<String>();
        for (MultipartFile file : files) {
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            urls.add(url);
            fileNames.add(fileName);
            newFileNames.add(FileUtils.getName(fileName));
            originalFilenames.add(file.getOriginalFilename());
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
        ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
        ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
        ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
        return ajax;

    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }
}
