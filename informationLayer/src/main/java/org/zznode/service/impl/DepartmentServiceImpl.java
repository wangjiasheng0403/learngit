package org.zznode.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zznode.dao.TbDepartmentMapper;
import org.zznode.dao.TbParyMapper;
import org.zznode.dto.DepartmentParam;
import org.zznode.dto.ExportDepartmentParam;
import org.zznode.dto.InsertDepartmentParam;
import org.zznode.dto.UpdateDepartmentParam;
import org.zznode.entity.TbDepartment;
import org.zznode.entity.TbPary;
import org.zznode.service.DepartmentService;
import org.zznode.util.SnowflakeIdGenerator;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<TbDepartmentMapper, TbDepartment> implements DepartmentService {

    @Autowired
    private TbDepartmentMapper departnameMapper;

    @Autowired
    private TbParyMapper paryMapper;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public JSONArray serchDepartmentByNameAndTime(ExportDepartmentParam param) {

        String dname = null;
        if(null != param.getDepartName()){
            dname = param.getDepartName().replace("%","\\%");
        }

        QueryWrapper<TbDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dname),"departName",dname)
                .ge(StringUtils.isNotBlank(param.getStartTime()),"createTime",param.getStartTime())
                .le(StringUtils.isNotBlank(param.getEndTime()),"createTime",param.getEndTime());

        List<TbDepartment> departments = departnameMapper.selectList(queryWrapper);
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(departments));
        return jsonArray;
    }

    public JSONArray serchDepartmentByNameAndTime(DepartmentParam param) {

        String dname = null;
        if(null != param.getDepartName()){
            dname = param.getDepartName().replace("%","\\%");
        }

        QueryWrapper<TbDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dname),"departName",dname)
                .ge(StringUtils.isNotBlank(param.getStartTime()),"createTime",param.getStartTime())
                .le(StringUtils.isNotBlank(param.getEndTime()),"createTime",param.getEndTime());

        List<TbDepartment> departments = departnameMapper.selectList(queryWrapper);
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(departments));
        return jsonArray;
    }

    @Override
    public JSONObject insertDepartment(InsertDepartmentParam param){

        JSONObject jsonRetu = new JSONObject();
        String dname = null;
        if(null != param.getDepartname()){
            dname = param.getDepartname().replace("%","\\%");
        }
        QueryWrapper<TbDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(dname),"departName",dname);
        List<TbDepartment> departments = departnameMapper.selectList(queryWrapper);
        if(departments.size() > 0){
            jsonRetu.put("description","该部门名称已占用");
            jsonRetu.put("code","000002");
            jsonRetu.put("result","falt");
            jsonRetu.put("fatherid",null);
            jsonRetu.put("fathername",null);
            jsonRetu.put("lv",null);
            jsonRetu.put("departid",null);
            jsonRetu.put("departname",null);
            return jsonRetu;
        }else{
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("departID",param.getFatherid());
            departments = departnameMapper.selectList(queryWrapper);
            if(departments.size() == 0){
                jsonRetu.put("description","上级部门不存在");
                jsonRetu.put("code","000002");
                jsonRetu.put("result","falt");
                jsonRetu.put("fatherid",null);
                jsonRetu.put("fathername",null);
                jsonRetu.put("lv",null);
                jsonRetu.put("departid",null);
                jsonRetu.put("departname",null);
                return jsonRetu;
            }else{
                TbDepartment ftd = departments.get(0);

                jsonRetu.put("fatherid",ftd.getDepartid());
                jsonRetu.put("fathername",ftd.getDepartname());
                jsonRetu.put("lv",ftd.getLv()+1);
                jsonRetu.put("departid",snowflakeIdGenerator.nextId());
                jsonRetu.put("departname",param.getDepartname());

                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                jsonRetu.put("createtime",format.format(date));

                if(ftd.getLv() == 1){
                    jsonRetu.put("seconddepartid",jsonRetu.getLong("departid"));
                }else if(ftd.getLv() == 2){
                    jsonRetu.put("seconddepartid",ftd.getDepartid());
                }else if(ftd.getLv() == 3){
                    jsonRetu.put("seconddepartid",ftd.getFatherid());
                }else{
                    jsonRetu.put("description","该部门不允许下挂组织");
                    jsonRetu.put("code","000003");
                    jsonRetu.put("result","falt");
                    jsonRetu.put("fatherid",null);
                    jsonRetu.put("fathername",null);
                    jsonRetu.put("lv",null);
                    jsonRetu.put("departid",null);
                    jsonRetu.put("departname",null);
                    return jsonRetu;
                }

                QueryWrapper<TbPary> Wrapper = new QueryWrapper<>();
                Wrapper.eq("partID",param.getPartid());
                List<TbPary> tbPary = paryMapper.selectList(Wrapper);
                if(tbPary.size() == 0 ){
                    jsonRetu.put("description","该党组织不存在");
                    jsonRetu.put("code","000004");
                    jsonRetu.put("result","falt");
                    jsonRetu.put("fatherid",null);
                    jsonRetu.put("fathername",null);
                    jsonRetu.put("lv",null);
                    jsonRetu.put("departid",null);
                    jsonRetu.put("departname",null);
                    return jsonRetu;
                }else{
                    jsonRetu.put("partid", tbPary.get(0).getPartid());
                    jsonRetu.put("partname", tbPary.get(0).getPartname() );
                }
                jsonRetu.put("description","成功");
                jsonRetu.put("code","000000");
                jsonRetu.put("result","success");
            }
        }
        System.out.println(jsonRetu.toString());
        //json转对象
        TbDepartment td= JSON.parseObject(jsonRetu.toJSONString(),new TypeReference<TbDepartment>(){});
        departnameMapper.insertOne(td);
        return jsonRetu;
    }


    @Override
    public JSONObject updateDepartment(UpdateDepartmentParam param){

        JSONObject json = new JSONObject();
        List<TbDepartment> departments;
        QueryWrapper<TbDepartment> queryWrapper;
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("departID",param.getSuperiordepartid());
        List<TbDepartment> fdepartments = departnameMapper.selectList(queryWrapper);
        if(fdepartments.size() == 0){
            json.put("description","上级部门不存在");
            json.put("code","000002");
            json.put("result","falt");
            json.put("fatherid",null);
            json.put("fathername",null);
            json.put("lv",null);
            json.put("departid",null);
            json.put("departname",null);
            return json;
        }else{
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("departID",param.getDepartid());
            departments = departnameMapper.selectList(queryWrapper);
            if(departments.size() == 0){
                json.put("description","部门不存在");
                json.put("code","000005");
                json.put("result","falt");
                json.put("fatherid",null);
                json.put("fathername",null);
                json.put("lv",null);
                json.put("departid",null);
                json.put("departname",null);
                return json;
            }

            TbDepartment department = departments.get(0);
            if(!department.getDepartname().equals(param.getDepartname())){
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(StringUtils.isNotBlank(param.getDepartname()),"departName",param.getDepartname());
                List<TbDepartment> departmentsfindname = departnameMapper.selectList(queryWrapper);
                if(departmentsfindname.size() > 0){
                    json.put("description","新部门名称已存在");
                    json.put("code","000005");
                    json.put("result","falt");
                    json.put("fatherid",null);
                    json.put("fathername",null);
                    json.put("lv",null);
                    json.put("departid",null);
                    json.put("departname",null);
                    return json;
                }
            }

            json.put("fatherid",fdepartments.get(0).getDepartid());
            json.put("fathername",fdepartments.get(0).getDepartname());
            json.put("lv",fdepartments.get(0).getLv()+1);
            json.put("departid",department.getDepartid());
            json.put("departname",param.getDepartname());

            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            json.put("createtime",format.format(date));

            if(fdepartments.get(0).getLv() == 1){
                json.put("seconddepartid",json.getInteger("departid"));
            }else if(fdepartments.get(0).getLv() == 2){
                json.put("seconddepartid",fdepartments.get(0).getDepartid());
            }
            else if(fdepartments.get(0).getLv() == 3){
                json.put("seconddepartid",fdepartments.get(0).getFatherid());
            }else{
                json.put("description","该部门不允许下挂组织");
                json.put("code","000003");
                json.put("result","falt");
                json.put("fatherid",null);
                json.put("fathername",null);
                json.put("lv",null);
                json.put("departid",null);
                json.put("departname",null);
                return json;
            }

            QueryWrapper<TbPary> Wrapper = new QueryWrapper<>();
            Wrapper.eq("partID",param.getPartid());
            List<TbPary> tbPary = paryMapper.selectList(Wrapper);
            if(tbPary.size() == 0 ){
                json.put("description","该党组织不存在");
                json.put("code","000004");
                json.put("result","falt");
                json.put("fatherid",null);
                json.put("fathername",null);
                json.put("lv",null);
                json.put("departid",null);
                json.put("departname",null);
                return json;
            }else{
                json.put("partid", tbPary.get(0).getPartid());
                json.put("partname", tbPary.get(0).getPartname() );
            }
            json.put("description","成功");
            json.put("code","000000");
            json.put("result","success");
        }

        //json转class
        TbDepartment td= JSON.parseObject(json.toJSONString(),new TypeReference<TbDepartment>(){});
        System.out.println("td:" + td);
        departnameMapper.updateByPrimaryKeySelective(td);
        return json;
    }

    @Override
    public JSONObject deleteDepartment(Long departid){

        JSONObject json = new JSONObject();
        QueryWrapper<TbDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("departID",departid);
        List<TbDepartment> departments = departnameMapper.selectList(queryWrapper);
        if(departments.size() == 0){
            json.put("description", "部门ID：" + departid + "该部门名称不存在");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("fatherID",departid);
        List<TbDepartment> fdepartments = departnameMapper.selectList(queryWrapper);
        if(fdepartments.size() > 0){
            json.put("description","删除失败，部门ID" + departid + "，该部门存在下挂部门，请先处理下挂部门。");
            json.put("code","000002");
            json.put("result","falt");
            return json;
        }
        departnameMapper.deleteByPrimaryKey(departments.get(0).getDepartid());
        json.put("description","成功");
        json.put("code","000000");
        json.put("result","success");
        return json;
    }

}
