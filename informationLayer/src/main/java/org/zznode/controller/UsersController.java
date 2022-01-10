package org.zznode.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Objects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import org.zznode.beans.MD5Utils;
import org.zznode.dao.*;
import org.zznode.dto.*;
import org.zznode.entity.*;
import org.zznode.service.impl.DepartmentServiceImpl;
import org.zznode.service.impl.DictServiceImpl;
import org.zznode.service.impl.RoleServiceImpl;
import org.zznode.service.impl.UsersServiceImpl;
import org.zznode.util.GetUuidUtil;
import org.zznode.util.SMSUtil;
import org.zznode.util.SnowflakeIdGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CrossOrigin
@RequestMapping(value = "/master")
@Controller
@Api(tags = "管理后台权限角色类接口")
public class UsersController {

//======================================================service===================================================//
//================================================================================================================//

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private UsersServiceImpl userService;

    @Autowired
    private DictServiceImpl dictService;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private GetUuidUtil getUuidUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SMSUtil sMSUtil;

    //======================================================mappers===================================================//
   //================================================================================================================//
    @Autowired
    private TbUsersMapper tbUsersMapper;

    @Autowired
    private TbDepartmentMapper tbDepartmentMapper;

    @Autowired
    private TbRolesRulesMapper tbRolesRulesMapper;

    @Autowired
    private TbRoleLogsMapper tbRoleLogsMapper;

    @Autowired
    private TbMakesMapper tbMakesMapper;

    @Autowired
    private TbRolesUsersMapper tbRolesUsersMapper;

    @Autowired
    private TbUserCollectMapper tbUserCollectMapper;

    @Autowired
    private TbParyMapper tbParyMapper;

    @Autowired
    private TbUsersOptionsMapper tbUsersOptionsMapper;

    @Autowired
    private TbUsersScoreMapper tbUsersScoreMapper;

    @Autowired
    private TbDataResourceMapper tbDataResourceMapper;

    @Autowired
    private TbAuthRuleMapper tbAuthRuleMapper;

//================================================table tb_department=============================================//
//================================================================================================================//

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "exportDepartment",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "根据部门字段导出部门信息接口", notes = "通过部门名称或时间范围导出部门信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public void exportDepartment(HttpServletResponse response,@Valid @RequestBody ExportDepartmentParam param,@RequestHeader("Authorization") String token) throws IOException {

        if(this.checkToken(token)) {

            JSONArray jsonArray = departmentService.serchDepartmentByNameAndTime(param);
            System.out.println(jsonArray);

            HSSFWorkbook wb = new HSSFWorkbook();

            HSSFSheet sheet = wb.createSheet("部门信息");

            HSSFRow row = null;

            //创建第一个单元格
            row = sheet.createRow(0);
            row.setHeight((short) (26.25 * 20));
            //为第一行单元格设值
            row.createCell(0).setCellValue("部门信息列表");

            /*为标题设计空间
             * firstRow从第1行开始
             * lastRow从第0行结束
             *
             *从第1个单元格开始
             * 从第3个单元格结束
             */
            CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 3);
            sheet.addMergedRegion(rowRegion);

            /*CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
            sheet.addMergedRegion(columnRegion);*/
            row = sheet.createRow(1);
            row.setHeight((short) (22.50 * 20));//设置行高
            row.createCell(0).setCellValue("部门编号");//为第一个单元格设值
            row.createCell(1).setCellValue("部门名称");//为第二个单元格设值
            row.createCell(2).setCellValue("所属党组织");//为第三个单元格设值
            row.createCell(3).setCellValue("创建时间");//为第四个单元格设值

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsont = JSON.parseObject(jsonArray.get(i).toString());
                row = sheet.createRow(i + 2);
                row.createCell(0).setCellValue(jsont.getString("departid"));
                row.createCell(1).setCellValue(jsont.getString("departname"));
                row.createCell(2).setCellValue(jsont.getString("partname"));
                row.createCell(3).setCellValue(jsont.getString("createtime"));
            }
            sheet.setDefaultRowHeight((short) (16.5 * 20));
            //列宽自适应
            for (int i = 0; i <= 13; i++) {
                sheet.autoSizeColumn(i);
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            OutputStream os = response.getOutputStream();
            response.setHeader("Access-Control-Expose-Headers", "filename");
            response.setHeader("filename", "departmentinfo.xls");//默认Excel名称
            wb.write(os);
            os.flush();
            os.close();
        }
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "serchDepartmentByNameAndTime",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "根据部门字段查询部门信息接口", notes = "通过部门名称或时间范围查询部门信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject serchDepartmentByNameAndTime(@Valid @RequestBody DepartmentParam param,@RequestHeader("Authorization") String token){

        List<String> nulllist = new ArrayList<String>();
        JSONObject json = new JSONObject();

        if(this.checkToken(token)) {
            JSONArray jsonArray = departmentService.serchDepartmentByNameAndTime(param);
            System.out.println("jsonArray:" + jsonArray);
            if (jsonArray.size() == 0) {
                json.put("roles", nulllist);
                json.put("result", "success");
                json.put("code", "000000");
                json.put("description", "没有符合条件的数据");
                return json;
            }

            if(null == param.getPageIndex() || null == param.getPageSize() || param.getPageIndex() < 1 || param.getPageSize() <1){
                JSONObject jsonAuthorizeDate = new JSONObject();
                jsonAuthorizeDate.put("result","success");
                jsonAuthorizeDate.put("code","000000");
                jsonAuthorizeDate.put("description","成功");
                jsonAuthorizeDate.put("roles",jsonArray);
                jsonAuthorizeDate.put("total",jsonArray.size());
                return jsonAuthorizeDate;
            }

            int pageBegin = (param.getPageIndex() - 1) * param.getPageSize();
            int pageEnd = param.getPageIndex() * param.getPageSize();

            JSONArray retJsonarray = new JSONArray();

            if (pageBegin >= jsonArray.size()) {
                json.put("result", "success");
                json.put("code", "000000");
                json.put("description", "超出最大值");
                json.put("roles", nulllist);
                json.put("total", jsonArray.size());
                return json;
            }

            for (int i = pageBegin; i < pageEnd && i < jsonArray.size(); i++) {
                retJsonarray.add(jsonArray.get(i));
            }

            json.put("result", "success");
            json.put("code", "000000");
            json.put("description", "成功");
            json.put("roles", retJsonarray);
            json.put("total", jsonArray.size());
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "insertDepartment",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "新增部门", notes = "新增行政部门", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject insertDepartment(@Valid @RequestBody InsertDepartmentParam param, @RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            json = departmentService.insertDepartment(param);
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "updateDepartment",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "部门信息更新", notes = "行政部门信息更新", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject updateDepartment(@Valid @RequestBody UpdateDepartmentParam param, @RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        System.out.println("updateDepartment:" + param.getDepartname());
        if(this.checkToken(token)) {
            json = departmentService.updateDepartment(param);
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "deleteDepartment",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "删除部门", notes = "根据departid删除部门", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject deleteDepartment(@Valid @RequestBody UpdateDepartmentParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            json = departmentService.deleteDepartment(param.getDepartid());
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "deleteDepartmentAll",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "批量删除部门", notes = "根据Long数组，批量删除部门", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject deleteDepartmentAll(@Valid @RequestBody UpdateDepartmentParam param,@RequestHeader("Authorization") String token){

        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            for (Long departmentid : param.getDepartmentsid()) {
                json = departmentService.deleteDepartment(departmentid);
                if (json.getString("code").equals("000001") || json.getString("code").equals("000002")) {
                    return json;
                }
            }
            json.put("description", "删除成功");
            json.put("code", "000000");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @RequestMapping(value = "serchDepartmentByDepartid",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "根据部门ID查询部门信息", notes = "根据部门ID查询部门信息", httpMethod = "POST",response = void.class)
    @ApiImplicitParams({})
    public TbDepartment serchDepartmentByDepartid(@Valid @RequestBody UpdateDepartmentParam param,@RequestHeader("Authorization") String token){
        TbDepartment tbDepartment = null;
        if(this.checkToken(token)) {
            tbDepartment = tbDepartmentMapper.selectByPrimaryKey(param.getDepartid());
        }
        return tbDepartment;
    }

//===================================================table tb_dict================================================//
//================================================================================================================//

    @RequestMapping(value = "serchDictByArea",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "按数据域查询字典表", notes = "area =1 为行政类角色枚举，area =2 为组织类角色枚举，area=3 为其他类型枚举", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "需要查询的数据域", name = "area", dataType="Integer", required = true,paramType = "query")})
    public  JSONObject serchDictByArea(@Valid @RequestBody SerchDictByAreaParam param,@RequestHeader("Authorization") String token){
        JSONArray jsonArray = new JSONArray();
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            jsonArray = dictService.serchDictByArea(param.getArea());
            json.put("arealist", jsonArray);
            json.put("result","success");
            json.put("code","000000");
            json.put("description","成功");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

//================================================table tb_rules==================================================//
//================================================================================================================//
    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "serchRuleslist",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "查询所有权限信息", notes = "查询权限表中所有权限信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject serchRuleslist(@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if(this.checkToken(token)) {
            jsonArray = roleService.serchRuleInfo();
            json.put("rules", jsonArray);
            json.put("description", "成功");
            json.put("code", "000000");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

//================================================table tb_roles==================================================//
//================================================================================================================//

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "exportRoleInfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "导出角色表中所有角色", notes = "导出角色表中所有角色", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public void exportRoleInfo(HttpServletResponse response,@RequestHeader("Authorization") String token) throws IOException {

        if(this.checkToken(token)) {
            JSONObject json = roleService.serchRoleInfoByRoleNameOrDescriptorOrCreateTime();
            System.out.println(json);
            JSONArray jsonArray = json.getJSONArray("rules");
            System.out.println(jsonArray);


            HSSFWorkbook wb = new HSSFWorkbook();

            HSSFSheet sheet = wb.createSheet("角色信息");

            HSSFRow row = null;

            //创建第一个单元格
            row = sheet.createRow(0);
            row.setHeight((short) (26.25 * 20));
            //为第一行单元格设值
            row.createCell(0).setCellValue("角色信息列表");

            /*为标题设计空间
             * firstRow从第1行开始
             * lastRow从第0行结束
             *
             *从第1个单元格开始
             * 从第3个单元格结束
             */
            CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 4);
            sheet.addMergedRegion(rowRegion);

            /*CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
            sheet.addMergedRegion(columnRegion);*/
            row = sheet.createRow(1);
            row.setHeight((short) (22.50 * 20));//设置行高
            row.createCell(0).setCellValue("角色编号");//为第一个单元格设值
            row.createCell(1).setCellValue("名称");//为第二个单元格设值
            row.createCell(2).setCellValue("数据权限");//为第三个单元格设值
            row.createCell(3).setCellValue("描述");//为第四个单元格设值
            row.createCell(4).setCellValue("创建日期");//为第四个单元格设值
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsont = JSON.parseObject(jsonArray.get(i).toString());
                row = sheet.createRow(i + 2);
                row.createCell(0).setCellValue(jsont.getString("rolesid"));
                row.createCell(1).setCellValue(jsont.getString("rolesname"));
                JSONArray jsonArrayt = jsont.getJSONArray("rulesList");
                String auth = null;
                if (null == jsonArrayt) {
                    row.createCell(2).setCellValue("暂未分配权限");
                } else {
                    auth = null;
                    for (int j = 0; j < jsonArrayt.size(); j++) {
                        JSONObject jsontt = JSON.parseObject(jsonArrayt.get(j).toString());
                        if (j == 0) {
                            auth = jsontt.getString("rulesname");
                        } else {
                            auth = auth + "," + jsontt.getString("rulesname");
                        }
                    }
                    System.out.println(auth);
                    row.createCell(2).setCellValue(auth);
                }

                row.createCell(3).setCellValue(jsont.getString("descriptor"));
                row.createCell(4).setCellValue(jsont.getString("createtime"));

            }
            sheet.setDefaultRowHeight((short) (16.5 * 20));
            //列宽自适应
            for (int i = 0; i <= 13; i++) {
                sheet.autoSizeColumn(i);
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            OutputStream os = response.getOutputStream();
            response.setHeader("Access-Control-Expose-Headers", "filename");
            response.setHeader("filename", "RoleInfo.xls");//默认Excel名称
            wb.write(os);
            os.flush();
            os.close();
        }
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "serchRoleInfoAll",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "查询角色表中所有角色", notes = "查询角色表中所有角色", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject serchRoleInfoAll(@RequestHeader("Authorization") String token,@Valid @RequestBody SerchRoleInfoAllParam param){

        List<String> nulllist = new ArrayList<String>();
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            List<TbRoles> tbroles = roleService.serchRoleInfoAll();
            if (tbroles.size() == 0) {
                json.put("result", "fail");
                json.put("code", "000002");
                json.put("description", "数据为空");
                json.put("roles", null);
                json.put("total", null);
                return json;
            }

            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(tbroles));

            int pageBegin = (param.getPageIndex() - 1) * param.getPageSize();
            int pageEnd = param.getPageIndex() * param.getPageSize();
            if (pageBegin >= jsonArray.size()) {
                json.put("roles", nulllist);
                json.put("description", "已超出最大值");
                json.put("code", "000000");
                json.put("result", "success");
                json.put("total", jsonArray.size());
                return json;
            }

            JSONArray retJsonarray = new JSONArray();
            for (int i = pageBegin; i < pageEnd && i < jsonArray.size(); i++) {
                retJsonarray.add(jsonArray.get(i));
            }

            json.put("roles", retJsonarray);
            json.put("description", "成功");
            json.put("code", "000000");
            json.put("result", "success");
            json.put("total", jsonArray.size());
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "insertRoleInfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "添加角色同时批量添加权限", notes = "添加角色同时批量添加权限", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject insertRoleInfo(@Valid @RequestBody InsertRoleInfoparam param,@RequestHeader("Authorization") String token){

        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            json = roleService.insertRolest(param);
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "updateRoleInfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "修改角色信息", notes = "修改角色信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject updateRoleInfo(@Valid @RequestBody UpdateRoleInfoParam param,@RequestHeader("Authorization") String token) {
        JSONObject json = new JSONObject();
        if (this.checkToken(token)){
            json = roleService.updateRoleInfo(param);
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "deleteRoleInfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "删除角色", notes = "根据角色ID删除角色", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject deleteRoleInfo(@Valid @RequestBody DeleteRoleInfoParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            roleService.deleteById(param.getRolesid()); //删除角色
            tbRolesRulesMapper.deleteByRolesID(param.getRolesid()); //删除角色权限中间表
            tbRolesUsersMapper.deleteByrolesID(param.getRolesid()); //删除角色用户中间表

            json.put("description", "删除成功");
            json.put("code", "000000");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "deleteRoleInfoAll",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "批量删除角色", notes = "根据角色id数组，批量删除角色", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject deleteRoleInfoAll(@Valid @RequestBody DeleteRoleInfoAllParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            List<Long> rolelists = ConvetDataType(param.getRolesid());
            for (Long roleid : rolelists) {
                roleService.deleteById(roleid); //删除角色
                tbRolesRulesMapper.deleteByRolesID(roleid); //删除角色权限中间表
                tbRolesUsersMapper.deleteByrolesID(roleid); //删除角色用户中间表
            }
            json.put("description", "删除成功");
            json.put("code", "000000");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "serchRoleInfoByRoleNameOrDescriptorOrCreateTime",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "按条件查询角色信息", notes = "根据角色名称、描述内容、起止时间查询角色信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject serchRoleInfoByRoleNameOrDescriptorOrCreateTime(@Valid @RequestBody SerchRoleInfoByRoleNameOrDescriptorOrCreateTimeParam param,@RequestHeader("Authorization") String token) {

        JSONObject json = new JSONObject();
        if (this.checkToken(token)){
            json = roleService.serchRoleInfoByRoleNameOrDescriptorOrCreateTime(param);
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

//==================================================table tb_users================================================//
//================================================================================================================//

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "updataNickname",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "更新用户昵称", notes = "通过用户ID更新用户昵称", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject updataNickname(@Valid @RequestBody UpdataNicknameParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)){
            Long userid = param.getUserid();
            String nickname = param.getNickname();
           int i = tbUsersMapper.updataNickname(nickname,userid);
            System.out.println("updataNickname type:" + i);
            json.put("result","success");
            json.put("code","000000");
            json.put("description","昵称更新成功");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }

        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "exportUserInfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "按条件导出用户信息", notes = "根据用户账号、用户名称、用户手机号码导出用户信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public void exportUserInfo(HttpServletResponse response,@Valid @RequestBody SerchUserInfoByaccountAndUsernameAndPhoneParam param,@RequestHeader("Authorization") String token) throws IOException {

        if(this.checkToken(token)) {
            JSONArray jsonArray = userService.serchUserInfoByaccountAndUsernameAndPhone(param);
            System.out.println(jsonArray);

            HSSFWorkbook wb = new HSSFWorkbook();

            HSSFSheet sheet = wb.createSheet("用户信息");

            HSSFRow row = null;

            //创建第一个单元格
            row = sheet.createRow(0);
            row.setHeight((short) (26.25 * 20));
            //为第一行单元格设值
            row.createCell(0).setCellValue("用户信息列表");

            /*为标题设计空间
             * firstRow从第1行开始
             * lastRow从第0行结束
             *
             *从第1个单元格开始
             * 从第3个单元格结束
             */
            CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 8);
            sheet.addMergedRegion(rowRegion);

            /*CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
            sheet.addMergedRegion(columnRegion);*/
            row = sheet.createRow(1);
            row.setHeight((short) (22.50 * 20));//设置行高
            row.createCell(0).setCellValue("用户编号");//为第一个单元格设值
            row.createCell(1).setCellValue("用户账号");//为第二个单元格设值
            row.createCell(2).setCellValue("真实姓名");//为第三个单元格设值
            row.createCell(3).setCellValue("性别");//为第四个单元格设值
            row.createCell(4).setCellValue("电话");//为第四个单元格设值
            row.createCell(5).setCellValue("岗位");//为第四个单元格设值
            row.createCell(6).setCellValue("邮箱");//为第四个单元格设值
            row.createCell(7).setCellValue("锁定");//为第四个单元格设值
            row.createCell(8).setCellValue("创建时间");//为第四个单元格设值
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsont = JSON.parseObject(jsonArray.get(i).toString());
                row = sheet.createRow(i + 2);
                row.createCell(0).setCellValue(jsont.getString("userid"));
                row.createCell(1).setCellValue(jsont.getString("account"));
                row.createCell(2).setCellValue(jsont.getString("username"));
                if (jsont.getString("sex").equals("M")) {
                    row.createCell(3).setCellValue("男");
                } else {
                    row.createCell(3).setCellValue("女");
                }
                row.createCell(4).setCellValue(jsont.getString("phone"));
                if (null == jsont.getString("departdutyid")) {
                    row.createCell(5).setCellValue("");
                } else if (jsont.getString("departdutyid").equals("201")) {
                    row.createCell(5).setCellValue("总经理");
                } else if (jsont.getString("departdutyid").equals("202")) {
                    row.createCell(5).setCellValue("分管领导");
                } else if (jsont.getString("departdutyid").equals("203")) {
                    row.createCell(5).setCellValue("部门经理");
                }
                row.createCell(6).setCellValue(jsont.getString("email"));
                if (jsont.getString("accountstatus").equals("1")) {
                    row.createCell(7).setCellValue("正常");
                } else {
                    row.createCell(7).setCellValue("锁定");
                }
//                System.out.println(jsont.getString("createtime"));
                if (null == jsont.getString("createtime")) {
                    row.createCell(8).setCellValue("");
                } else {
//                    Date date = new Date(Long.parseLong(jsont.getString("createtime")));
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    row.createCell(8).setCellValue(jsont.getString("createtime"));
                }

            }
            sheet.setDefaultRowHeight((short) (16.5 * 20));
            //列宽自适应
            for (int i = 0; i <= 13; i++) {
                sheet.autoSizeColumn(i);
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            OutputStream os = response.getOutputStream();
            response.setHeader("Access-Control-Expose-Headers", "filename");
            response.setHeader("filename", "userinfo.xls");//默认Excel名称
            wb.write(os);
            os.flush();
            os.close();
        }
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "serchUserInfoByaup",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "按条件查询用户信息", notes = "根据用户账号、用户名称、用户手机号码查询用户信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject serchUserInfoByaccountAndUsernameAndPhone(@Valid @RequestBody SerchUserInfoByaccountAndUsernameAndPhoneParam param,@RequestHeader("Authorization") String token){

        List<String> nulllist = new ArrayList<String>();
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            JSONArray jsonArray = userService.serchUserInfoByaccountAndUsernameAndPhone(param);
            if (jsonArray.size() == 0) {
                json.put("result", "fail");
                json.put("code", "000002");
                json.put("description", "数据为空");
                json.put("roles", nulllist);
                json.put("total", 0);
                return json;
            }

            int pageBegin = (param.getPageIndex() - 1) * param.getPageSize();
            int pageEnd = param.getPageIndex() * param.getPageSize();

            if (pageBegin >= jsonArray.size()) {
                json.put("result", "success");
                json.put("code", "000000");
                json.put("description", "超出数据最大值");
                json.put("roles", nulllist);
                json.put("total", jsonArray.size());
                return json;
            }

            JSONArray retJsonarray = new JSONArray();
            for (int i = pageBegin; i < pageEnd && i < jsonArray.size(); i++) {
                if (jsonArray.size() > 0) {
                    JSONObject jsontemp = jsonArray.getJSONObject(i);
                    QueryWrapper<TbRolesUsers> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("userID", jsontemp.getString("userid"));
                    List<TbRolesUsers> rolesusers = tbRolesUsersMapper.selectList(queryWrapper);
                    jsontemp.put("rolesusers", rolesusers);
                    retJsonarray.add(jsontemp);
                }
            }

            json.put("users", retJsonarray);
            json.put("description", "成功");
            json.put("code", "000000");
            json.put("result", "success");
            json.put("total", jsonArray.size());
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "insertUserinfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "新增用户", notes = "新增用户", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject insertUserinfo(@Valid @RequestBody InsertUserinfoParam param,@RequestHeader("Authorization") String token) throws NoSuchAlgorithmException {
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            String pwd = param.getPassword();
            if (!checkPWD(pwd)) {
                JSONObject jsonp = new JSONObject();
                jsonp.put("code", "000002");
                jsonp.put("description", "密码设置不合法，密码必须包含大小写字母、数字、特殊符号");
                jsonp.put("result", "faild");
                return jsonp;
            }

            json = userService.insertUserinfo(param);
        }else{
            json.put("result","fail");
            json.put("code","000002");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "updateUserinfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject updateUserinfo(@Valid @RequestBody InsertUserinfoParam param,@RequestHeader("Authorization") String token) throws NoSuchAlgorithmException {

        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            QueryWrapper<TbUsers> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq(StringUtils.isNotBlank(param.getPassword()),"passWord",param.getPassword())
                    .eq(StringUtils.isNotBlank(param.getAccount()),"account",param.getAccount());
            List<TbUsers> usersList = tbUsersMapper.selectList(userQueryWrapper);
            if(usersList.size() == 0){
                String pwd = param.getPassword();
                if (!checkPWD(pwd)) {
                    json.put("code", "000002");
                    json.put("description", "密码设置不合法，密码必须包含大小写字母、数字、特殊符号");
                    json.put("result", "faild");
                    return json;
                }
            }else{
                param.setPassword(null);
            }

            json = userService.updateUserinfo(param);
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "changepwd",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject changepwd(@Valid @RequestBody ChangepwdParam param,@RequestHeader("Authorization") String token) throws NoSuchAlgorithmException {
        JSONObject json = new JSONObject();

        if(this.checkToken(token)) {
            if (checkPWD(param.getNewpwd())) {
				String oldinputString = MD5Utils.getMD5Str(param.getOldpwd());
				String newinputString = MD5Utils.getMD5Str(param.getNewpwd());
                TbUsers tbUsers = tbUsersMapper.selectByPrimaryKey(param.getUsersid());

                if (!tbUsers.getPassword().equals(oldinputString)) {
                    json.put("code", "000002");
                    json.put("description", "密码输入错误");
                    json.put("result", "fild");
                    return json;
                }

                tbUsers.setPassword(newinputString);
                tbUsersMapper.updateByPrimaryKey(tbUsers);
                json.put("code", "000000");
                json.put("description", "成功");
                json.put("result", "success");
                return json;
            }

            json.put("code", "000002");
            json.put("description", "密码设置不合法，密码必须包含大小写字母、数字、特殊符号");
            json.put("result", "faild");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;


    }


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "deleteUserinfo",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "删除用户", notes = "根据用户ID删除用户", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject deleteUserinfo(@Valid @RequestBody DeleteUserinfoParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        System.out.println("deleteUserinfo:" + param.getUsersid());
        if(this.checkToken(token)) {
            json = userService.deleteUserinfo( param.getUsersid());
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;

    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "deleteUserinfoAll",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "deleteUserinfoAll", notes = "批量删除用户", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject deleteUserinfoAll(@Valid @RequestBody DeleteUserinfoAllParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        System.out.println("deleteUserinfoAll:" + param.getUsersid());
        if(this.checkToken(token)) {
            List<Long> useridlist = ConvetDataType(param.getUsersid());
            for (Long userid : useridlist) {
                userService.deleteUserinfo(userid);
            }
            json.put("description", "成功");
            json.put("code", "000000");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

//================================================table tb_user_collect=============================================//
//==================================================================================================================//

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "addUserCollector",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "添加收藏", notes = "用户添加收藏场景", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject addUserCollector(@Valid @RequestBody UserCollectorParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();

        if(this.checkToken(token)) {
            TbUserCollect tbu = new TbUserCollect();
            tbu.setId(snowflakeIdGenerator.nextId());
            tbu.setUserid(param.getUserid());
            tbu.setDataname(param.getDataname());
            tbu.setDatapath(param.getDatapath());
            tbUserCollectMapper.insertOne(tbu);

            json.put("code", "000000");
            json.put("description", "成功");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }


    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "delUserCollector",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "删除收藏", notes = "用户删除收藏场景", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject delUserCollector(@Valid @RequestBody UserCollectorParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();

        if(this.checkToken(token)) {
            tbUserCollectMapper.deleteByuseridAnddatapath(param.getUserid(), param.getDatapath());
            json.put("code", "000000");
            json.put("description", "成功");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "serchUserCollector",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "查询收藏", notes = "根据userid查询用户的场景收藏信息", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject serchUserCollector(@Valid @RequestBody SerchHistoryByuserIDParam param, @RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            Long userid = param.getUserid();
            List<TbUserCollect> tbUserCollectList = tbUserCollectMapper.selectByuserID(userid);
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(tbUserCollectList));
            JSONArray jsonArrayRetu = new JSONArray();
            for(int i =0;i<jsonArray.size();i++){
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                String datapath = jsonObject.getString("datapath");
                List<TbDataResource> tbDataResourceList = tbDataResourceMapper.getDateResource(datapath);
                if(tbDataResourceList.size() > 0){
                    String dataid = tbDataResourceList.get(0).getDataid();
                    System.out.println("dataid:" + dataid);
                    JSONObject jb = this.serch(dataid);
                    jsonObject.put("imgPath",tbDataResourceList.get(0).getImgpath());
                    jsonObject.put("watch",jb.getInteger("countW"));
                    jsonObject.put("like",jb.getInteger("countL"));
                    jsonObject.put("dataid",tbDataResourceList.get(0).getDataid());
                    jsonArrayRetu.add(jsonObject);
                }
            }

            json.put("collectors", jsonArrayRetu);
            json.put("code", "000000");
            json.put("description", "成功");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }
    public JSONObject serch(String dataid){
        Map<String,Integer> m =  tbRoleLogsMapper.serchLikedByDataID(dataid);
        JSONObject json =new JSONObject();
        if(null == m){
            json.put("countW",0);
            json.put("countL",0);
        }else{
            json.put("countW",m.get("countW"));
            json.put("countL",m.get("countL"));
        }
        return json;
    }
//================================================table tb_user_collect=============================================//
//==================================================================================================================//

    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*", methods={RequestMethod.POST,
            RequestMethod.POST,RequestMethod.DELETE,RequestMethod.OPTIONS,
            RequestMethod.HEAD,RequestMethod.PUT,RequestMethod.PATCH},origins="*")
    @RequestMapping(value = "serchHistoryByuserID",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "查询浏览记录", notes = "根据用户ID查询用户最近浏览过的10个场景", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject serchHistoryByuserID(@Valid @RequestBody SerchHistoryByuserIDParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            Long userid = param.getUserid();
            List<Map<String, String>> ml = tbRoleLogsMapper.getHistoryByuserID(userid);
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(ml));
            JSONArray jsonArrayRetu = new JSONArray();
            for(int i = 0; i<jsonArray.size(); i++){
                JSONObject jsonTemp = (JSONObject)jsonArray.get(i);
                System.out.println("cTime:" + jsonTemp.getString("cTime"));
                Date date = new Date(Long.parseLong(jsonTemp.getString("cTime")));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                jsonTemp.put("cTimeStr",format.format(date));
                jsonArrayRetu.add(jsonTemp);
            }
            json.put("historys", jsonArrayRetu);
            json.put("code", "000000");
            json.put("description", "成功");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }

//==================================================提供给睿悦的接口======================================================//
//=============================================================================================================//


    @GetMapping("serchMakesBydataID")
    @ResponseBody
    public JSONObject serchMakesBydataID(String dataid){

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonretu = new JSONObject();

        //获取role_logs表里对dataID的所有访问记录
        QueryWrapper<TbRoleLogs> rolesUsersQueryWrapper = new QueryWrapper<>();
        rolesUsersQueryWrapper.eq("dataID",dataid);
        List<TbRoleLogs> roleLogslist = tbRoleLogsMapper.selectList(rolesUsersQueryWrapper);
        for(int i=0;i<roleLogslist.size();i++){

            //根据访问记录列表遍历makes表
            QueryWrapper<TbMakes> makesQueryWrapper = new QueryWrapper<>();
            makesQueryWrapper.eq("logsid",roleLogslist.get(i).getId());
            List<TbMakes> makeslist = tbMakesMapper.selectList(makesQueryWrapper);
            for(int j=0;j<makeslist.size();j++){
                JSONObject json = new JSONObject();
                json.put("nickname",makeslist.get(j).getNickname());
                json.put("makeInfo",makeslist.get(j).getMakeInfo());
                json.put("createTime",makeslist.get(j).getCtime());
                jsonArray.add(json);
            }
        }
        jsonretu.put("code","000000");
        jsonretu.put("data",jsonArray);
        jsonretu.put("description","成功");
        jsonretu.put("result","success");

        return jsonretu;
    }

    @GetMapping("serchCollectBydataIDAndUserid")
    @ResponseBody
    public JSONObject serchCollectBydataIDAndUserid(String dataid,String userid){

        JSONObject jsonretu = new JSONObject();
        JSONObject json = new JSONObject();

        QueryWrapper<TbRoleLogs> rolesUsersQueryWrapper = new QueryWrapper<>();
        rolesUsersQueryWrapper.eq("dataID",dataid).eq("userID",userid);
        List<TbRoleLogs> roleLogslist = tbRoleLogsMapper.selectList(rolesUsersQueryWrapper);
        if(roleLogslist.size() > 0){
            json.put("liked",roleLogslist.get(0).getLiked());
            json.put("collect",roleLogslist.get(0).getCollect());
        }

        jsonretu.put("code","000000");
        jsonretu.put("data",json);
        jsonretu.put("description","");
        jsonretu.put("result","success");
        return jsonretu;
    }


//===================================================util======================================================//
//=============================================================================================================//
    @RequestMapping(value = "getLongRandom",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "获取Long型随机ID", notes = "给前端提供的唯一ID", httpMethod = "POST",response = void.class)
    public JSONObject getLongRandom(@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            long i = Long.MAX_VALUE;
            json.put("SN", snowflakeIdGenerator.nextId());
            json.put("code", "000000");
            json.put("i",i);
            json.put("description", "成功");
            json.put("result", "success");
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }


    /*
     * 这个方法是给登陆获取用户信息用的
     *  */
    @GetMapping(value = "serchUserinfoByAcount",produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "通过用户账号查询用户信息", notes = "通过用户账号查询用户信息", httpMethod = "GET",response = void.class)
    public JSONObject serchUserinfoByAcount(String account){
        JSONObject json = new JSONObject();

        json.put("account", account);
        JSONObject jsonRetu = userService.serchUserinfo(json);
        json = (JSONObject) jsonRetu.get("users");

        return json;
    }

    /*
     * 这个方法是给登陆获取用户权限用的
     *  */
    @GetMapping(value = "serchRolesIDByUserID", produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "通过用户ID查询用户信息", notes = "通过用户ID查询用户信息", httpMethod = "GET",response = void.class)
    public JSONObject serchRolesIDByUserID(Long userid){
        JSONObject json = new JSONObject();

        json =  userService.serchRolesIDByUserID(userid);

        return json;
    }

    /*
     * 提供给 用户自定义头像上传功能 将头像地址更新到用户表里用的
     * */
    @GetMapping("updateUserinfoByusersid")
    @ResponseBody
    public JSONObject updateUserinfoByuserid(Long usersid,String portraitpath){
        JSONObject json = new JSONObject();
        TbUsers tbUsers = new TbUsers();
        tbUsers.setUserid(usersid);
        tbUsers.setPortraitpath(portraitpath);
        tbUsersMapper.updateById(tbUsers);
        json.put("return","success");
        return json;
    }

    /*
     * 提供给 通过组织ID获取组织信息
     * */
    @GetMapping("serchParyinfo")
    @ResponseBody
    public JSONObject serchParyinfo(Long partid){

        TbPary tbPary = tbParyMapper.selectByPrimaryKey(partid);

        JSONObject json = JSONObject.parseObject(JSON.toJSONString(tbPary));
        return json;
    }


    public Boolean checkPWD(String pwd){

        //同时包含数字，字母，特殊符号
        String pattern = "^^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*_-]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*_-]+$)(?![\\d!@#$%^&*_-]+$)[a-zA-Z\\d!@#$%^&*_-]+$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(pwd);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取短信验证码
     */
    @RequestMapping(value = "/getVerificationCode",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "获取短信验证码", notes = "获取短信验证码", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户账号", name = "userAccount", dataType = "String", required = true,paramType = "query")})
    public JSONObject getVerificationCode( HttpServletRequest request) throws IOException {
        JSONObject json = new JSONObject();
        String useraccount = request.getParameter("userAccount");
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(useraccount),"account",useraccount);
        List<TbUsers> tbUsersList =  tbUsersMapper.selectList(queryWrapper);
        if(tbUsersList.size() == 0){
            json.put("code","000002");
            json.put("description","用户账号不存在");
            json.put("result","faild");
            return json;
        }
        String phone = tbUsersList.get(0).getPhone();
        System.out.println("phone:" + phone);
        if(null != phone && phone.length() == 11){
            String  verificationCode = getUuidUtil.getVerificationCode();
            String account = tbUsersList.get(0).getAccount();


            Map<String, Object> map = new HashMap<String, Object>();
            map.put("account", account);
            map.put("verificationCode", verificationCode);

            ResponseEntity<JSONObject> jsonretu = restTemplate.getForEntity("http://paryBuildingProc/redis/setVerificationCode?account={account}&verificationCode={verificationCode}", JSONObject.class, map);
            System.out.println(jsonretu.getBody().getString("result") );
            if(jsonretu.getBody().getString("result").equals("success")){
                String content = "您本次找回密码的验证码是：" + verificationCode;
                sMSUtil.sendMsg(phone,content);
            }
        }else{
            json.put("code","000002");
            json.put("description","手机号码不合法，密码找回失败");
            json.put("result","faild");
            return json;
        }
        json.put("code","000000");
        json.put("description","成功");
        json.put("result","success");

        return json;
    }
    /**
     * 通过验证码修改密码
     */
    @RequestMapping(value = "/setPasswdByVerificationCode",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "通过验证码修改密码", notes = "通过验证码修改密码", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject setPasswdByVerificationCode(@Valid @RequestBody SetPasswdByVerificationCodeParam param ) throws IOException, NoSuchAlgorithmException {
        JSONObject json = new JSONObject();
        String useraccount = param.getUserAccount();
        String verificationCode = param.getVerificationCode();
        String passwd = param.getPasswd();
        System.out.println("userAccount：" + param.getUserAccount());
        System.out.println("verificationCode：" + param.getVerificationCode());
        System.out.println("passwd：" + param.getPasswd());
        System.out.println(useraccount + "  "  + verificationCode +  "  " + passwd);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("account", useraccount);
        ResponseEntity<JSONObject> jsonretu = restTemplate.getForEntity("http://paryBuildingProc/redis/getVerificationCode?account={account}", JSONObject.class, map);
        System.out.println("userscontroller: " + jsonretu.getBody().getString("verificationCode") );

        if(null != jsonretu.getBody().getString("verificationCode")  ){
            System.out.println("verificationCode:" + jsonretu.getBody().getString("verificationCode"));

            if(jsonretu.getBody().getString("verificationCode").equals(verificationCode)){
                if(checkPWD(passwd)){
                    QueryWrapper queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq(StringUtils.isNotBlank(useraccount),"account",useraccount);
                    List<TbUsers> tbUsersList =  tbUsersMapper.selectList(queryWrapper);
                    if(tbUsersList.size() == 0){
                        json.put("code","000002");
                        json.put("description","用户账号不存在");
                        json.put("result","faild");
                        return json;
                     }else{
                        TbUsers tbUsers = tbUsersList.get(0);
                        tbUsers.setPassword(MD5Utils.getMD5Str(passwd));
                        tbUsersMapper.updateById(tbUsers);
                    }
                }else{
                    json.put("code","000002");
                    json.put("description","密码设置不合法，密码必须包含大小写字母、数字、特殊符号");
                    json.put("result","faild");
                    return json;
                }
            }else{
                json.put("code","000003");
                json.put("description","验证码输入错误");
                json.put("result","faild");
                return json;
            }
        }else{
            json.put("code","000004");
            json.put("description","验证码已失效，请重新获取验证码");
            json.put("result","faild");
            return json;
        }
        json.put("code","000000");
        json.put("description","密码修改成功");
        json.put("result","success");
        return json;

    }

//====================================================Tree===================================================//
//===========================================================================================================//
    @GetMapping("/viewDepartTree")
    @ResponseBody
    public Map<String, Object> viewDepartTree(Long userid,Integer relationType){

        System.out.println("userid:" + userid + "relationType:" + relationType);
        Map<String, Long> userInfo = tbUsersMapper.getUserPartIdAndSecondDepartId(userid);

        Map<String, Object> result = new HashMap<>(2);

        /*
          getAllDepList
                 select a1.*,
               IF(tb_depart_relation.visitParyID is not null or a1.partId = #{partyId}, true,
                  false) as isAuth
        from (select departID   as departId,
                     fatherID   as fatherId,
                     lv,
                     departName as departName,
                     partID     as partId,
                     partName
              from tb_department
             ) as a1
                 left join tb_depart_relation on tb_depart_relation.visitParyID = a1.partId
            and tb_depart_relation.paryID = #{partyId}
        order by lv, partID
        * */

        /*
        getDepList
        select a1.*,
        IF(tb_depart_relation.visitParyID is not null or a1.partId = #{partyId}, true,
        false) as isAuth
        from (
        select t1.departID as departId,
        t1.fatherID as fatherId,
        t1.lv,
        t1.departName as departName,
        t1.partID as partId,
        t1.partName
        from tb_department t1,
        tb_department t2
        <if test="type==0">
            where t2.secondDepartID = #{secondDepartId}
        </if>
        <if test="type==1">
            where t2.secondDepartID != #{secondDepartId}
        </if>
        and t1.departID = t2.fatherID
        and t2.lv = 2
        ) as a1
        left join tb_depart_relation on tb_depart_relation.visitParyID = a1.partId
        and tb_depart_relation.paryID = #{partyId}
        union
        select a1.*,
        IF(tb_depart_relation.visitParyID is not null or a1.partId = #{partyId}, true,
        false) as isAuth
        from (select departID as departId,
        fatherID as fatherId,
        lv,
        departName as departName,
        partID as partId,
        partName
        from tb_department
        <if test="type == 0">where secondDepartID = #{secondDepartId}</if>
        <if test="type == 1">where secondDepartID != #{secondDepartId}</if>
        ) as a1
        left join tb_depart_relation on tb_depart_relation.visitParyID = a1.partId
        and tb_depart_relation.paryID = #{partyId}
        order by lv, partID
        * */

        List<Map<String, Object>> orgAuthMap;
        if (userInfo.get("secondDepartId") == null || userInfo.get("secondDepartId") == 0) {
            orgAuthMap = tbDepartmentMapper.getAllDepList(userInfo.get("partyId"));
        } else {
            orgAuthMap = tbDepartmentMapper.getDepList(userInfo.get("secondDepartId"), userInfo.get("partyId"),relationType);
        }

        List<Map<String, Object>> newOrgAuthMap = new ArrayList<>();

        for (Map<String, Object> map : orgAuthMap) {
            Map<String, Object> mapCopy = new HashMap<>();
            mapCopy.put("departID", "0");
            mapCopy.put("partID", map.get("partId"));
            mapCopy.put("fatherID", map.get("departId"));
            mapCopy.put("departName", map.get("partName"));
            System.out.println("isAuth:"+ map.get("isAuth"));
            if( Integer.parseInt(map.get("isAuth").toString()) == 1 ) {
                mapCopy.put("clickType", "true");
                System.out.println("111");
            }else{
                mapCopy.put("clickType", "false");
                System.out.println("222");
            }
            mapCopy.put("departLV", "99");
            newOrgAuthMap.add(mapCopy);

            map.put("departID",map.get("departId"));
            map.put("fatherID",map.get("fatherId"));
            map.put("clickType","false");
            map.put("departLV",map.get("lv"));

            map.remove("departId");
            map.remove("fatherId");
            map.remove("lv");
            map.remove("isAuth");
            map.remove("partName");
        }

        newOrgAuthMap.addAll(orgAuthMap);
        System.out.println("newOrgAuthMap:" + newOrgAuthMap);
        result.put("administrativeTree", newOrgAuthMap.stream()
                .filter(i -> "1".equals(i.get("departLV").toString()))
                .peek(i -> i.put("childrenDepart", setChild(i, newOrgAuthMap)))
                .collect(Collectors.toList()));

        return result;

    }

    private List<Map<String, Object>> setChild(Map<String, Object> data, List<Map<String, Object>> result) {
        System.out.println("data:" + data);
        System.out.println("result:" + result);
        return result.stream()
                .filter(i -> Objects.equal(i.get("fatherID"), data.get("departID")))
                .peek(i -> i.put("childrenDepart", setChild(i, result)))
                .collect(Collectors.toList());
    }

    /*
    * intent调用
    * */
    @GetMapping("insertUserOptions")
    @ResponseBody
    public JSONObject setUsersOptions(Long userid,String account,int loginType){
        JSONObject jsonretu = new JSONObject();
        TbUsersOptions options = new TbUsersOptions();

        options.setId(snowflakeIdGenerator.nextId());
        options.setUserid(userid);
        options.setAccount(account);
        if(loginType == 1) {
            options.setOptiontype("登陆");
        }else{
            options.setOptiontype("登出");
        }
        Date date = new Date();
        options.setCreatetime(date);


        tbUsersOptionsMapper.insert(options);
        jsonretu.put("result", "success");
        return jsonretu;

    }


    @RequestMapping(value = "/serchDayscore",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "获取用户今日学习积分", notes = "获取用户今日学习积分", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({})
    public JSONObject serchDayscore(@Valid @RequestBody SerchDayscoreParam param,@RequestHeader("Authorization") String token){
        JSONObject json = new JSONObject();

        if(this.checkToken(token)) {
            int lc = tbRoleLogsMapper.selectLogincount(param.getUserid());
            int wc = tbRoleLogsMapper.selectWatchcount(param.getUserid());
            int mc = tbRoleLogsMapper.selectMakecount(param.getUserid());

            System.out.println("lc:" + lc + "wc:" + wc + "mc:" + mc);

            int lcscore = 0;
            int wcscore = 0;
            int mcscore = 0;



            if (lc > 0) {
                lcscore = 2;
            }

            if (wc > 0 && wc < 4) {
                wcscore = wc * 2;
            } else if (wc > 3) {
                wcscore = 6;
            }

            if (mc > 0 && mc < 3) {
                mcscore = mc * 1;
            } else if (mc > 2) {
                mcscore = 2;
            }

            TbUsersScore serchuserscore = tbUsersScoreMapper.serchuserscore(param.getUserid(), "total");

            if(null != serchuserscore) {
                json.put("userid", param.getUserid());
                json.put("loginscore", lcscore);
                json.put("watchscore", wcscore);
                json.put("mcscore", mcscore);
                json.put("dayscore", lcscore + wcscore + mcscore);
                json.put("totalscore", serchuserscore.getScore());
                json.put("code", "000000");
                json.put("description", "成功");
                json.put("result", "success");
            }else{
                json.put("result","success");
                json.put("code","000000");
                json.put("description","该用户没有积分信息");
            }
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }



    @RequestMapping(value = "/sercscore",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "获取用户学习报表（月、年）", notes = "获取用户学习报表（月、年）", httpMethod = "POST",response = JSONObject.class)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户ID", name = "userid", dataType = "Long", required = true,paramType = "query")})
    public JSONObject serchscore(@Valid @RequestBody SerchDayscoreParam param,@RequestHeader("Authorization") String token){

        JSONObject json = new JSONObject();
        if(this.checkToken(token)) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("YYYY");
            String dateYear = format.format(date);
            format = new SimpleDateFormat("YYYY-MM");
            String datemonth = format.format(date);

            TbUsersScore serchuserscore1 = tbUsersScoreMapper.serchuserscore(param.getUserid(), dateYear);
            TbUsersScore serchuserscore2 = tbUsersScoreMapper.serchuserscore(param.getUserid(),datemonth);
            TbUsersScore serchuserscore3 = tbUsersScoreMapper.serchuserscore(param.getUserid(), "total");

            TbUsers tbUsers = userService.selectById(param.getUserid());
            Long paryid = tbUsers.getParyid();
            List<Map<String, Object>> maplist1 = tbUsersScoreMapper.serchorder(paryid, dateYear);
            List<Map<String, Object>> maplist2 = tbUsersScoreMapper.serchorder(paryid, datemonth);


            for (int i = 0; i < maplist2.size(); i++) {
                if (maplist2.get(i).get("userName").equals(tbUsers.getUsername())) {
                    json.put("monthindex", i + 1);
                }
            }

            for (int i = 0; i < maplist1.size(); i++) {
                if (maplist1.get(i).get("userName").equals(tbUsers.getUsername())) {
                    json.put("yearindex", i + 1);
                }
            }
            if(null != serchuserscore3 && null != serchuserscore2 && null != serchuserscore1){
                json.put("total", serchuserscore3.getScore());
                json.put("monthscore", serchuserscore2.getScore());
                json.put("yearscore", serchuserscore1.getScore());
                json.put("mothorder", maplist2);
                json.put("yearorder", maplist1);
                json.put("code","000000");
                json.put("description","成功");
                json.put("result","success");
            }else{
                json.put("result","success");
                json.put("code","000000");
                json.put("description","该用户没有积分信息");
            }
        }else{
            json.put("result","fail");
            json.put("code","000001");
            json.put("description","token不存在或已过期，请登陆");
        }
        return json;
    }



    public Boolean checkToken(String token){
        Boolean checkType = false;
        String redisToken = getToken(token);
        if(token.equals(redisToken)) {
            refreshredis(token);
            checkType = true;
        }
        return checkType;
    }

    public String getToken(String token){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/getToken?token={token}",JSONObject.class,params);
        JSONObject body = response.getBody();
        if(body.get("token") == null){
            return null;
        }else {
            return body.get("token").toString();
        }
    }

    public  String refreshredis(String token){
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity("http://paryBuildingProc/redis/refreshredis?token={token}",JSONObject.class,params);
        JSONObject body = response.getBody();
        System.out.println("RedisService" + body.get("result"));
        if(body.get("result") == null){
            return null;
        }else {
            return body.get("result").toString();
        }
    }

    @GetMapping("serchAuth")
    @ResponseBody
    private JSONObject serchAuth(String intfname){
        JSONObject json = new JSONObject();
        List<TbAuthRule> tbAuthRuleList =  tbAuthRuleMapper.serchAuth(intfname);
        if(tbAuthRuleList.size() > 0 ){
            TbAuthRule tar = tbAuthRuleList.get(0);
            //class转json
            json = JSONObject.parseObject(JSONObject.toJSONString(tar));
        }else{
            json.put("resule","falt");
        }
        return json;
    }

    private List<Long> ConvetDataType(String sourceData)
    {
        return Arrays.stream(sourceData.split(",")).map(s->Long.parseLong(s.trim())).collect(Collectors.toList());
    }
}
//====================================================end===================================================//
//===========================================================================================================//
