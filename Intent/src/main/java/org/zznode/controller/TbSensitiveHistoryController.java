package org.zznode.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.zznode.dto.SensitiveHistoryParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Chris
 */
@Slf4j
@RestController
@RequestMapping("sensitiveHistory")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TbSensitiveHistoryController {

    private final RestTemplate restTemplate;

    @PostMapping("/list")
    public JSONObject save(@RequestBody SensitiveHistoryParam sensitiveHistoryParam) {
        return restTemplate.postForEntity("http://informationLayer/sensitiveHistory/list", sensitiveHistoryParam, JSONObject.class).getBody();
    }

    @PostMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadXlsWright(HttpServletResponse response, @RequestBody SensitiveHistoryParam param) throws UnsupportedEncodingException {
        SXSSFWorkbook wb = new SXSSFWorkbook(1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = "检查结果查询" + sdf.format(new Date()) + ".xlsx";

        SXSSFSheet sheet = wb.createSheet("检查结果查询列表");
        sheet.setDefaultColumnWidth(30);
        SXSSFRow row = sheet.createRow(0);
        String[] paryArr = new String[]{"检查时间", "检查类型", "场景资源", "素材点", "涉敏内容", "用户账号"};
        for (int i = 0; i < paryArr.length; i++) {
            // 遍历插入表头
            SXSSFCell cell = row.createCell(i);
            cell.setCellValue(paryArr[i]);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> dataPage = restTemplate.postForEntity("http://informationLayer/sensitiveHistory/excel", param, List.class).getBody();
        for (int i = 0; i < dataPage.size(); i++) {
            Map<String, Object> historyMap = dataPage.get(i);
            int count = i + 1;
            List<String> dataList = new ArrayList<>();
            dataList.add(format.format(Long.valueOf(historyMap.get("checkTime").toString())));
            // 检查类型: 1场景素材，2评论内容
            dataList.add("1".equals(historyMap.get("checkType").toString()) ? "场景素材" : "评论内容");
            dataList.add(historyMap.get("sceneSource").toString());
            dataList.add(historyMap.get("information").toString());
            dataList.add(historyMap.get("sensitiveContent").toString());
            dataList.add(historyMap.get("userName").toString());
            SXSSFRow dataRow = sheet.createRow(count);
            for (int j = 0; j < dataList.size(); j++) {
                SXSSFCell cell = dataRow.createCell(j);
                cell.setCellValue(dataList.get(j));
            }
        }
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fn, "UTF-8"));
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            wb.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            wb.dispose();
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
