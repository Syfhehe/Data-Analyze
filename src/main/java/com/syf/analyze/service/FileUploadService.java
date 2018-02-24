package com.syf.analyze.service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

import com.syf.analyze.util.UUIDGenerator;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadService {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);


    public static String saveData(HttpServletRequest request, MultipartFile file, String uploadPath) {

        String ext = file.getOriginalFilename().split("\\.")[1];
        String newFileName = UUIDGenerator.getUUID() + "." + ext;
        String filePathAndName = null;
        if (uploadPath.endsWith(File.separator)) {
            filePathAndName = uploadPath + newFileName;
        } else {
            filePathAndName = uploadPath + File.separator + newFileName;
        }
        logger.info("-----上传的文件:{}-----", filePathAndName);
        try {
            // 先把文件保存到本地
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(uploadPath, newFileName));
        } catch (IOException e1) {
            logger.error("-----文件保存到本地发生异常:{}-----", e1.getMessage());
        }

        return uploadPath + newFileName;
    }
}
