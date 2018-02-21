package com.syf.analyze.web;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
public class UploadController {

    @RequestMapping("/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") CommonsMultipartFile file) throws IOException {
        PrintWriter out;
        boolean flag = false;
        if (file.getSize() > 0) {
            String path = "/assets/upload/files/";
            String uploadPath = request.getSession().getServletContext().getRealPath(path);
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(uploadPath, file.getOriginalFilename()));
                flag = true;
            } catch (Exception e) {
            }

        }
        out = response.getWriter();
        if (flag == true) {
            out.print("1");
        } else {
            out.print("2");
        }
    }

}