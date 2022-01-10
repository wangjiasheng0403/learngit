package org.zznode.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zznode.common.CommonResult;
import org.zznode.dto.*;
import org.zznode.service.OrganizationService;
import org.zznode.util.FileUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/organization")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "党组织机构管理")
public class OrganizationController {

    private final OrganizationService organizationService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> list(@RequestHeader("Authorization") String token,
                                @RequestBody ListOrganizationParam param) {
        param.setToken(token);
        return CommonResult.success(organizationService.list(param));
    }

    /**
     * 组织初始化数据
     */
    @PostMapping("/init")
    @ApiOperation(value = "组织初始化数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> init(@RequestHeader("Authorization") String token,
                                @Valid @RequestBody InitOrganizationParam param) {
        param.setToken(token);
        return CommonResult.success(organizationService.init(param));
    }

    /**
     * 新增组织
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增组织")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> add(@RequestHeader("Authorization") String token,
                               @Valid @RequestBody AddOrganizationParam param) {
        param.setToken(token);
        organizationService.add(param);
        return CommonResult.success();
    }

    /**
     * 修改组织
     */
    @PostMapping("/modify")
    @ApiOperation(value = "修改组织")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> modify(@RequestHeader("Authorization") String token,
                                  @Valid @RequestBody ModifyOrganizationParam param) {
        param.setToken(token);
        organizationService.modify(param);
        return CommonResult.success();
    }

    /**
     * 删除组织
     */
    @PostMapping("/remove")
    @ApiOperation(value = "删除组织")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    public CommonResult<?> remove(@RequestHeader("Authorization") String token,
                                  @Valid @RequestBody RemoveOrganizationParam param) {
        param.setToken(token);
        organizationService.remove(param);
        return CommonResult.success();
    }


    @PostMapping("/upload")
    @ApiOperation(value = "上传")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前操作时，上一次上传的文件名", name = "oldFileName", dataType = "String"),
            @ApiImplicitParam(value = "文件", name = "file", dataType = "MultipartFile", required = true)})
    public CommonResult<?> upload(@RequestParam(value = "oldFileName") String oldFileName,
                                  @RequestParam(value = "file") MultipartFile file) {
        return CommonResult.success(FileUtils.upload(FileUtils.WorkFlow.ORG, FileUtils.FileType.ATTACHMENT, file, oldFileName));
    }


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @ApiOperation(value = "导出党组织列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "Authorization", value = "用户token", required = true)})
    @PostMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadXlsWright(HttpServletResponse response, @RequestHeader("Authorization") String token,
                                  @RequestBody ListOrganizationParam param) throws UnsupportedEncodingException {
        param.setToken(token);
        SXSSFWorkbook wb = new SXSSFWorkbook(1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = "党组织机构列表" + sdf.format(new Date()) + ".xlsx";

        SXSSFSheet sheet = wb.createSheet("党组织机构列表");
        sheet.setDefaultColumnWidth(30);
        SXSSFRow row = sheet.createRow(0);
        String[] paryArr = new String[]{"党组织机构名称", "创建人", "创建时间"};
        for (int i = 0; i < paryArr.length; i++) {
            // 遍历插入表头
            SXSSFCell cell = row.createCell(i);
            cell.setCellValue(paryArr[i]);
        }
        IPage<Map<String, Object>> dataPage = organizationService.list(param);
        for (int i = 0; i < dataPage.getRecords().size(); i++) {
            Map<String, Object> tbParyMap = dataPage.getRecords().get(i);
            int count = i + 1;
            List<String> dataList = new ArrayList<>();
            dataList.add(tbParyMap.get("partName").toString());
            dataList.add(tbParyMap.get("userName").toString());
            dataList.add(tbParyMap.get("createTime").toString());
            SXSSFRow dataRow = sheet.createRow(count);
            for (int j = 0; j < dataList.size(); j++) {
                SXSSFCell cell = dataRow.createCell(j);
                cell.setCellValue(dataList.get(j));
            }
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Access-Control-Expose-Headers", "filename");
        response.setHeader("filename", "departmentinfo.xls");//默认Excel名称
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
