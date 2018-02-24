package com.syf.analyze.web;

import com.syf.analyze.context.BaseReturn;
import com.syf.analyze.context.ErrorCode;
import com.syf.analyze.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
public class UploadController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${web.upload-path}")
    private String uploadPath;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String add(String name, @RequestParam(required = false) MultipartFile file, HttpServletRequest request)
            throws IOException {

        logger.info("-----上传文件:{}-----", name);
        if (null == file || file.isEmpty()) {
            logger.info("-----没有上传文件-----");
        } else {
            logger.info("-----文件大小:{}----- ", file.getSize());
            logger.info("-----文件类型:{}-----", file.getContentType());
            logger.info("-----表单名称:{}-----", file.getName());
            logger.info("-----文件原名:{}-----", file.getOriginalFilename());
            if (!file.getOriginalFilename().contains("xls")) {
                return BaseReturn.response(ErrorCode.UNSUPPORTED_TYPE, "不支持的数据表类型：" + file.getContentType());
            }
            String data = FileUploadService.saveData(request, file, uploadPath);
            return BaseReturn.response(ErrorCode.SUCCESS, data);
        }
        return BaseReturn.response(ErrorCode.SUCCESS);
    }

}