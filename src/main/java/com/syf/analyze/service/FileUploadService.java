package com.syf.analyze.service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.syf.analyze.domain.TestData;
import com.syf.analyze.repository.TestDataRepository;
import com.syf.analyze.util.ExcelImportUtils;
import com.syf.analyze.util.UUIDGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Autowired
    private TestDataRepository testDataRepository;

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

    public String batchImport(String filePath, String fileName) {

        File file = new File(filePath);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if (ExcelImportUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            //根据excel里面的内容读取知识库信息
            return readExcelValue(wb, file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return "导入出错！请检查数据格式！";
    }


    /**
     * 解析Excel里面的数据
     *
     * @param wb
     * @return
     */
    private String readExcelValue(Workbook wb, File tempFile) {

        //错误信息接收器
        String errorMsg = "";
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if (totalRows >= 2 && sheet.getRow(1) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        List<TestData> testDataList = new ArrayList<TestData>();
        TestData tempTestDataPair;

        String br = "<br/>";

        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            String rowMessage = "";
            Row row = sheet.getRow(r);
            if (row == null) {
                errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
                continue;
            }
            tempTestDataPair = new TestData();

            Date date;
            Double value;

            //循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        //java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                        date = cell.getDateCellValue();
                        if (StringUtils.isEmpty(date)) {
                            rowMessage += "时间不能为空；";
                        }
                        tempTestDataPair.setDate(date);
                    } else if (c == 1) {
                        value = cell.getNumericCellValue();
                        if (StringUtils.isEmpty(value)) {
                            rowMessage += "值不能为空；";
                        }
                        tempTestDataPair.setValue(value);
                    }
                } else {
                    rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
                }
            }
            //拼接每行的错误提示
            if (!StringUtils.isEmpty(rowMessage)) {
                errorMsg += br + "第" + (r + 1) + "行，" + rowMessage;
            } else {
                testDataList.add(tempTestDataPair);
            }
        }

        //全部验证通过才导入到数据库
        if (StringUtils.isEmpty(errorMsg)) {
            for (TestData testData : testDataList) {
                testDataRepository.save(testData);
            }
            errorMsg = "导入成功，共" + testDataList.size() + "条数据！";
        }
        return errorMsg;
    }
}
