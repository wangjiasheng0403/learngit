package org.zznode.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class FileUtils {
    @Value("${upload.saveUrl}")
    private String saveUrlTmp;
    @Value("${upload.showUrl}")
    private String showUrlTmp;

    private static String saveUrl;
    private static String showUrl;

    @PostConstruct
    public void init() {
        saveUrl = saveUrlTmp;
        showUrl = showUrlTmp;
    }

    /**
     * 上传文件
     * 成功返回 url 失败返回 null
     *
     * @param workFlow 流程名
     * @param fileType 文件类型
     * @param file     file
     * @return url
     */
    public static String upload(WorkFlow workFlow, FileType fileType, MultipartFile file) {
        return upload(workFlow, fileType, file, "");
    }

    /**
     * 上传文件
     * 成功返回 url 失败返回 null
     *
     * @param workFlow    流程名
     * @param fileType    文件类型
     * @param file        file
     * @param oldFileName oldFileName
     * @return url
     */
    public static String upload(WorkFlow workFlow, FileType fileType, MultipartFile file, String oldFileName) {
        // 获取文件名
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        System.out.println("file.getOriginalFilename()" + file.getOriginalFilename());
        String url = showUrl + workFlow.path + fileType.path + fileName;

        File dest = new File(saveUrl + workFlow.path + fileType.path);
        // 检测是否存在目录
        if (!dest.exists() && !dest.isDirectory()) {
            if (!dest.mkdirs()) {
                return null;
            }
        }

        File target = new File(dest, fileName);
        try {
            file.transferTo(target);

        } catch (IOException ioException) {
            log.error("uploadErr", ioException);
        }

        //删除旧文件
        if (StringUtils.isNotEmpty(oldFileName)) {
            File oldFile = new File(saveUrl + workFlow.path + fileType.path + oldFileName);
            if (!oldFile.delete()) {
                log.error("文件{}删除失败。", oldFileName);
            }
        }
        return url;
    }

    public enum WorkFlow {
        //场景流程
        SCENES("scenes/"),
        //内容运营商流程
        OPERATOR("operator/"),
        //组织机构流程
        ORG("org/");

        WorkFlow(String path) {
            this.path = path;
        }

        private final String path;
    }

    public enum FileType {
        //附件
        ATTACHMENT("attachment/"),
        //音频
        AUDIO("audio/"),
        //视频
        VIDEO("video/"),
        //图片
        PICTURE("picture/");

        FileType(String path) {
            this.path = path;
        }

        private final String path;
    }
}
